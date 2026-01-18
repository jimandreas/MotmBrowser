#!/usr/bin/env python3
"""
Scrape Molecule of the Month data from pdb101.rcsb.org

This script fetches molecule pages and extracts:
- Title
- Tagline/description
- Categories from "Related PDB-101 Resources" section

Output files:
- molecule_data.json: Raw scraped data
- category_updates.txt: Formatted updates for MotmByCategory.kt
- corpus_updates.txt: Formatted updates for Corpus.kt

Usage:
    pip install requests beautifulsoup4
    python scrape_motm_categories.py [start_num] [end_num]

Example:
    python scrape_motm_categories.py 258 313
"""

import json
import re
import sys
import time
from pathlib import Path

try:
    import requests
    from bs4 import BeautifulSoup
except ImportError:
    print("Error: Required packages not installed.")
    print("Run: pip install requests beautifulsoup4")
    sys.exit(1)

# Category to section mapping
CATEGORY_SECTIONS = {
    # Health and Disease categories
    "You and Your Health": "Health",
    "Immune System": "Health",
    "HIV and AIDS": "Health",
    "Diabetes": "Health",
    "Cancer": "Health",
    "Viruses": "Health",
    "Toxins and Poisons": "Health",
    "Drug Action": "Health",
    "Antimicrobial Resistance": "Health",
    "Drugs and the Brain": "Health",
    "Coronavirus": "Health",
    "Infectious Disease": "Health",
    "Peak Performance": "Health",
    "Vaccines": "Health",

    # Molecules of Life categories
    "Protein Synthesis": "Life",
    "Enzymes": "Life",
    "Molecular Infrastructure": "Life",
    "Transport": "Life",
    "Biological Energy": "Life",
    "Molecules and the Environment": "Life",
    "Biology of Plants": "Life",
    "Molecular Motors": "Life",
    "Cellular Signaling": "Life",
    "Nucleic Acids": "Life",
    "Bioluminescence and Fluorescence": "Life",
    "Molecular Evolution": "Life",
    "Central Dogma": "Life",
    "Molecules for a Sustainable Future": "Life",

    # Biotech and Nanotech categories
    "Recombinant DNA": "Biotech",
    "Biotechnology": "Biotech",
    "Nanotechnology": "Biotech",
    "Renewable Energy": "Biotech",

    # Structures categories
    "Biomolecules": "Structures",
    "Biomolecular Structural Biology": "Structures",
    "Hybrid Methods": "Structures",
    "Integrative/Hybrid Methods": "Structures",
    "Nobel Prizes and PDB Structures": "Structures",
    "Nobel Prizes and PDB structures": "Structures",
    "PDB Data": "Structures",
    "Protein Structure Prediction, Design, and Computed Structure Models": "Structures",
    "Protein Structure Prediction": "Structures",
}

# Existing categories in MotmByCategory.kt (for reference)
EXISTING_HEALTH_CATEGORIES = [
    "You and Your Health", "Immune System", "HIV and AIDS", "Diabetes",
    "Cancer", "Viruses", "Toxins and Poisons", "Drug Action",
    "Antimicrobial Resistance", "Drugs and the Brain", "Coronavirus"
]
EXISTING_LIFE_CATEGORIES = [
    "Protein Synthesis", "Enzymes", "Molecular Infrastructure", "Transport",
    "Biological Energy", "Molecules and the Environment", "Biology of Plants",
    "Molecular Motors", "Cellular Signaling", "Nucleic Acids",
    "Bioluminescence and Fluorescence", "Molecular Evolution", "Central Dogma"
]
EXISTING_BIOTECH_CATEGORIES = [
    "Recombinant DNA", "Biotechnology", "Nanotechnology", "Renewable Energy"
]
EXISTING_STRUCTURES_CATEGORIES = [
    "Biomolecules", "Biomolecular Structural Biology", "Hybrid Methods",
    "Nobel Prizes and PDB Structures"
]


def fetch_molecule_page(molecule_num: int, retries: int = 3) -> str:
    """Fetch HTML for a molecule page with retry logic."""
    url = f"https://pdb101.rcsb.org/motm/{molecule_num}"

    for attempt in range(retries):
        try:
            response = requests.get(url, timeout=30)
            response.raise_for_status()
            return response.text
        except requests.RequestException as e:
            if attempt < retries - 1:
                wait_time = 2 ** attempt
                print(f"  Retry {attempt + 1} for molecule {molecule_num} (waiting {wait_time}s)...")
                time.sleep(wait_time)
            else:
                print(f"  ERROR: Failed to fetch molecule {molecule_num}: {e}")
                return None
    return None


def extract_molecule_data(html: str, molecule_num: int) -> dict:
    """Extract title, tagline, and categories from molecule page."""
    soup = BeautifulSoup(html, 'html.parser')

    data = {
        "number": molecule_num,
        "title": "",
        "tagline": "",
        "categories": [],
        "thumbnail_hint": ""
    }

    # Extract title from h1 or page title
    h1 = soup.find('h1')
    if h1:
        # Title format is usually "Molecule of the Month: Title"
        title_text = h1.get_text(strip=True)
        if ":" in title_text:
            data["title"] = title_text.split(":", 1)[1].strip()
        else:
            data["title"] = title_text
    else:
        # Fallback to page title
        title_tag = soup.find('title')
        if title_tag:
            title_text = title_tag.get_text(strip=True)
            # Remove common suffixes
            title_text = re.sub(r'\s*-\s*PDB-101.*$', '', title_text)
            title_text = re.sub(r'^Molecule of the Month:\s*', '', title_text)
            data["title"] = title_text

    # Extract tagline/description from meta description or first paragraph
    meta_desc = soup.find('meta', attrs={'name': 'description'})
    if meta_desc and meta_desc.get('content'):
        data["tagline"] = meta_desc['content'].strip()
    else:
        # Try to get the first meaningful paragraph
        intro = soup.find('div', class_='introduction')
        if intro:
            p = intro.find('p')
            if p:
                data["tagline"] = p.get_text(strip=True)[:200]

    # Extract categories from "Related PDB-101 Resources" section
    # Look for browse links
    categories = set()

    # Method 1: Look for links containing "/browse/"
    for link in soup.find_all('a', href=True):
        href = link['href']
        if '/browse/' in href:
            # Extract category name from URL or link text
            category_text = link.get_text(strip=True)
            # Clean up "Browse " prefix if present
            category_text = re.sub(r'^Browse\s+', '', category_text, flags=re.IGNORECASE)
            if category_text:
                categories.add(category_text)

    # Method 2: Look in specific sections
    related_section = soup.find('div', class_='related-resources')
    if related_section:
        for link in related_section.find_all('a'):
            category_text = link.get_text(strip=True)
            category_text = re.sub(r'^Browse\s+', '', category_text, flags=re.IGNORECASE)
            if category_text and len(category_text) < 100:  # Sanity check
                categories.add(category_text)

    data["categories"] = sorted(list(categories))

    # Try to find thumbnail image hint
    og_image = soup.find('meta', property='og:image')
    if og_image and og_image.get('content'):
        data["thumbnail_hint"] = og_image['content']

    return data


def categorize_molecules(all_molecules: list) -> dict:
    """Organize molecules by category section."""
    sections = {
        "Health": {},
        "Life": {},
        "Biotech": {},
        "Structures": {},
        "Unknown": {}
    }

    new_categories = set()

    for mol in all_molecules:
        num = str(mol["number"])
        for cat in mol["categories"]:
            section = CATEGORY_SECTIONS.get(cat)
            if section:
                if cat not in sections[section]:
                    sections[section][cat] = []
                sections[section][cat].append(num)
            else:
                # Unknown category
                if cat not in sections["Unknown"]:
                    sections["Unknown"][cat] = []
                sections["Unknown"][cat].append(num)
                new_categories.add(cat)

    return sections, new_categories


def generate_kotlin_category_updates(sections: dict) -> str:
    """Generate Kotlin code snippets for MotmByCategory.kt updates."""
    output = []
    output.append("=" * 60)
    output.append("UPDATES FOR MotmByCategory.kt")
    output.append("=" * 60)
    output.append("")

    section_order = ["Health", "Life", "Biotech", "Structures"]
    section_names = {
        "Health": "MotmCategoryHealth",
        "Life": "MotmCategoryLife",
        "Biotech": "MotmCategoryBiotech",
        "Structures": "MotmCategoryStructures"
    }

    existing_categories = {
        "Health": EXISTING_HEALTH_CATEGORIES,
        "Life": EXISTING_LIFE_CATEGORIES,
        "Biotech": EXISTING_BIOTECH_CATEGORIES,
        "Structures": EXISTING_STRUCTURES_CATEGORIES
    }

    for section in section_order:
        if sections[section]:
            output.append(f"\n// === {section_names[section]} ===")
            output.append(f"// Add these molecule numbers to the appropriate categories\n")

            for cat, molecules in sorted(sections[section].items()):
                is_new = cat not in existing_categories.get(section, [])
                new_marker = " // NEW CATEGORY" if is_new else ""
                output.append(f'            "{cat}",{new_marker}')
                for mol in sorted(molecules, key=int):
                    output.append(f'            "{mol}",')
                output.append("")

    if sections.get("Unknown"):
        output.append("\n// === UNKNOWN CATEGORIES (need manual mapping) ===")
        for cat, molecules in sorted(sections["Unknown"].items()):
            output.append(f'// Category: "{cat}"')
            output.append(f'// Molecules: {", ".join(sorted(molecules, key=int))}')
            output.append("")

    return "\n".join(output)


def generate_kotlin_corpus_updates(all_molecules: list) -> str:
    """Generate Kotlin code snippets for Corpus.kt updates."""
    output = []
    output.append("=" * 60)
    output.append("UPDATES FOR Corpus.kt")
    output.append("=" * 60)

    # numMonths update
    max_num = max(m["number"] for m in all_molecules)
    output.append(f"\n// Update numMonths constant:")
    output.append(f"private const val numMonths = {max_num}")

    # corpus list entries
    output.append("\n// Add to corpus list:")
    for mol in sorted(all_molecules, key=lambda x: x["number"]):
        title = mol["title"].replace('"', '\\"')
        output.append(f'            /* //motm/{mol["number"]} */ "{title}",')

    # taglines
    output.append("\n// Add to motmTagLines array:")
    for mol in sorted(all_molecules, key=lambda x: x["number"]):
        tagline = mol["tagline"][:150].replace('"', '\\"')  # Truncate long taglines
        if tagline:
            output.append(f'            "{tagline}",')
        else:
            output.append(f'            "",  // TODO: Add tagline for {mol["number"]}')

    # thumbnail hints
    output.append("\n// Add to motmThumbnailImageList:")
    output.append("// NOTE: These are hints - actual filenames may differ")
    for mol in sorted(all_molecules, key=lambda x: x["number"]):
        # Generate a placeholder filename based on title
        safe_title = re.sub(r'[^a-zA-Z0-9]', '_', mol["title"])[:30]
        output.append(f'            "{mol["number"]}-{safe_title}-homepage-tn.png",')

    return "\n".join(output)


def main():
    # Parse command line arguments
    start_num = 258
    end_num = 313

    if len(sys.argv) >= 3:
        start_num = int(sys.argv[1])
        end_num = int(sys.argv[2])
    elif len(sys.argv) == 2:
        end_num = int(sys.argv[1])

    print(f"Scraping molecules {start_num} to {end_num}...")
    print(f"Total: {end_num - start_num + 1} molecules")
    print()

    # Scrape all molecules
    all_molecules = []

    for num in range(start_num, end_num + 1):
        print(f"Fetching molecule {num}...", end=" ", flush=True)

        html = fetch_molecule_page(num)
        if html:
            data = extract_molecule_data(html, num)
            all_molecules.append(data)
            print(f"OK - {data['title']} ({len(data['categories'])} categories)")
        else:
            print("FAILED")

        # Rate limiting - be nice to the server
        time.sleep(1)

    print(f"\nSuccessfully scraped {len(all_molecules)} molecules")

    # Create output directory
    output_dir = Path(__file__).parent

    # Save raw data as JSON
    json_path = output_dir / "molecule_data.json"
    with open(json_path, 'w', encoding='utf-8') as f:
        json.dump(all_molecules, f, indent=2, ensure_ascii=False)
    print(f"Saved raw data to: {json_path}")

    # Categorize molecules
    sections, new_categories = categorize_molecules(all_molecules)

    if new_categories:
        print(f"\nNew categories found (not in current mapping):")
        for cat in sorted(new_categories):
            print(f"  - {cat}")

    # Generate Kotlin updates
    category_updates = generate_kotlin_category_updates(sections)
    category_path = output_dir / "category_updates.txt"
    with open(category_path, 'w', encoding='utf-8') as f:
        f.write(category_updates)
    print(f"Saved category updates to: {category_path}")

    corpus_updates = generate_kotlin_corpus_updates(all_molecules)
    corpus_path = output_dir / "corpus_updates.txt"
    with open(corpus_path, 'w', encoding='utf-8') as f:
        f.write(corpus_updates)
    print(f"Saved corpus updates to: {corpus_path}")

    # Print summary
    print("\n" + "=" * 60)
    print("SUMMARY")
    print("=" * 60)
    for section in ["Health", "Life", "Biotech", "Structures"]:
        total = sum(len(mols) for mols in sections[section].values())
        cats = len(sections[section])
        print(f"{section}: {total} entries across {cats} categories")

    if sections["Unknown"]:
        total = sum(len(mols) for mols in sections["Unknown"].values())
        print(f"Unknown: {total} entries need manual mapping")

    print("\nDone! Review the output files and apply updates to Kotlin files.")


if __name__ == "__main__":
    main()
