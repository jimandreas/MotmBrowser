#!/usr/bin/env python3
"""
Scrape PDB codes from Molecule of the Month pages on pdb101.rcsb.org

This script fetches molecule pages and extracts PDB codes from:
- Links to RCSB structure pages (/structure/XXXX)
- Direct PDB code references in the text

Output:
- pdb_codes.json: Raw scraped data
- pdb_updates.txt: Formatted Kotlin entries for PDBs.kt

Usage:
    python scrape_pdb_codes.py [start_num] [end_num]

Example:
    python scrape_pdb_codes.py 278 313
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


def extract_pdb_codes(html: str, molecule_num: int) -> dict:
    """Extract PDB codes from molecule page."""
    soup = BeautifulSoup(html, 'html.parser')

    data = {
        "number": molecule_num,
        "pdb_codes": []
    }

    pdb_codes = set()

    # Method 1: Look for links to RCSB structure pages
    # Pattern: /structure/XXXX or rcsb.org/structure/XXXX
    for link in soup.find_all('a', href=True):
        href = link['href']

        # Match /structure/XXXX patterns
        match = re.search(r'/structure/([0-9A-Za-z]{4})(?:[^0-9A-Za-z]|$)', href)
        if match:
            pdb_code = match.group(1).lower()
            pdb_codes.add(pdb_code)
            continue

        # Match rcsb.org/3d-view/XXXX patterns
        match = re.search(r'/3d-view/([0-9A-Za-z]{4})(?:[^0-9A-Za-z]|$)', href)
        if match:
            pdb_code = match.group(1).lower()
            pdb_codes.add(pdb_code)

    # Method 2: Look for PDB codes in text (4 alphanumeric, starting with digit)
    # PDB codes typically start with a digit and have 4 characters
    text = soup.get_text()

    # Pattern for PDB codes: digit followed by 3 alphanumerics
    # Common patterns: "PDB entry 1abc", "structure 7xyz", etc.
    pdb_pattern = re.compile(r'\b([0-9][0-9A-Za-z]{3})\b')

    for match in pdb_pattern.finditer(text):
        potential_code = match.group(1).lower()
        # Filter out obvious non-PDB codes (years, common numbers)
        if not is_likely_pdb_code(potential_code):
            continue
        pdb_codes.add(potential_code)

    # Method 3: Look for specific PDB mention patterns
    # "PDB ID: XXXX" or "PDB: XXXX" or "PDB entry XXXX"
    pdb_explicit = re.compile(r'PDB[:\s]+(?:ID[:\s]+)?(?:entry[:\s]+)?([0-9][0-9A-Za-z]{3})\b', re.IGNORECASE)
    for match in pdb_explicit.finditer(text):
        pdb_code = match.group(1).lower()
        pdb_codes.add(pdb_code)

    data["pdb_codes"] = sorted(list(pdb_codes))
    return data


def is_likely_pdb_code(code: str) -> bool:
    """Filter out unlikely PDB codes."""
    code = code.lower()

    # Exclude years (1900-2099)
    if re.match(r'^(19|20)\d{2}$', code):
        return False

    # Exclude common number patterns
    excluded = {'1000', '2000', '3000', '4000', '5000', '100k', '200k'}
    if code in excluded:
        return False

    # PDB codes should have at least one letter after the first digit
    if code[1:].isdigit():
        return False

    return True


def generate_kotlin_updates(all_molecules: list) -> str:
    """Generate Kotlin code for PDBs.kt updates."""
    output = []
    output.append("=" * 60)
    output.append("UPDATES FOR PDBs.kt")
    output.append("=" * 60)
    output.append("")
    output.append("// Add these entries before the closing ) of pdbList")
    output.append("// (after the last MotmToPdbMap entry, add a comma first)")
    output.append("")

    for mol in sorted(all_molecules, key=lambda x: x["number"]):
        if mol["pdb_codes"]:
            output.append(f"            // Molecule {mol['number']}")
            for i, pdb in enumerate(mol["pdb_codes"]):
                # Add comma after each entry
                output.append(f'            MotmToPdbMap({mol["number"]}, "{pdb}"),')
            output.append("")

    return "\n".join(output)


def main():
    # Parse command line arguments
    start_num = 278
    end_num = 313

    if len(sys.argv) >= 3:
        start_num = int(sys.argv[1])
        end_num = int(sys.argv[2])
    elif len(sys.argv) == 2:
        end_num = int(sys.argv[1])

    print(f"Scraping PDB codes for molecules {start_num} to {end_num}...")
    print(f"Total: {end_num - start_num + 1} molecules")
    print()

    # Scrape all molecules
    all_molecules = []

    for num in range(start_num, end_num + 1):
        print(f"Fetching molecule {num}...", end=" ", flush=True)

        html = fetch_molecule_page(num)
        if html:
            data = extract_pdb_codes(html, num)
            all_molecules.append(data)
            print(f"OK - {len(data['pdb_codes'])} PDB codes found")
        else:
            print("FAILED")
            all_molecules.append({"number": num, "pdb_codes": []})

        # Rate limiting
        time.sleep(1)

    print(f"\nSuccessfully scraped {len(all_molecules)} molecules")

    # Calculate stats
    total_codes = sum(len(m["pdb_codes"]) for m in all_molecules)
    print(f"Total PDB codes found: {total_codes}")

    # Create output directory
    output_dir = Path(__file__).parent

    # Save raw data as JSON
    json_path = output_dir / "pdb_codes.json"
    with open(json_path, 'w', encoding='utf-8') as f:
        json.dump(all_molecules, f, indent=2, ensure_ascii=False)
    print(f"Saved raw data to: {json_path}")

    # Generate Kotlin updates
    kotlin_updates = generate_kotlin_updates(all_molecules)
    kotlin_path = output_dir / "pdb_updates.txt"
    with open(kotlin_path, 'w', encoding='utf-8') as f:
        f.write(kotlin_updates)
    print(f"Saved Kotlin updates to: {kotlin_path}")

    # Print summary
    print("\n" + "=" * 60)
    print("SUMMARY")
    print("=" * 60)
    for mol in all_molecules:
        codes = ", ".join(mol["pdb_codes"][:5])
        if len(mol["pdb_codes"]) > 5:
            codes += f"... (+{len(mol['pdb_codes']) - 5} more)"
        print(f"Molecule {mol['number']}: {len(mol['pdb_codes'])} codes - {codes}")

    print("\nDone! Review pdb_updates.txt and apply to PDBs.kt")


if __name__ == "__main__":
    main()
