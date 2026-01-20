#!/usr/bin/env python3
"""
Fetch PDB structure descriptions from RCSB PDB API

This script reads pdb_codes.json and fetches the title/description
for each PDB code from the RCSB API.

Output:
- pdb_info.json: Raw fetched data
- pdb_info_updates.txt: Formatted Kotlin entries for PdbInfoArray.kt

Usage:
    python scrape_pdb_info.py

The script reads PDB codes from pdb_codes.json in the same directory.
"""

import json
import sys
import time
from pathlib import Path

try:
    import requests
except ImportError:
    print("Error: requests package not installed.")
    print("Run: pip install requests")
    sys.exit(1)


def fetch_pdb_info(pdb_code: str, retries: int = 3) -> dict:
    """Fetch PDB entry info from RCSB API."""
    url = f"https://data.rcsb.org/rest/v1/core/entry/{pdb_code}"

    for attempt in range(retries):
        try:
            response = requests.get(url, timeout=30)
            if response.status_code == 404:
                return {"pdb_code": pdb_code, "title": f"PDB entry {pdb_code}", "error": "not_found"}
            response.raise_for_status()
            data = response.json()

            # Extract title from the response
            title = data.get("struct", {}).get("title", f"PDB entry {pdb_code}")

            return {
                "pdb_code": pdb_code,
                "title": title,
                "error": None
            }
        except requests.RequestException as e:
            if attempt < retries - 1:
                wait_time = 2 ** attempt
                print(f"  Retry {attempt + 1} for {pdb_code} (waiting {wait_time}s)...")
                time.sleep(wait_time)
            else:
                print(f"  ERROR: Failed to fetch {pdb_code}: {e}")
                return {"pdb_code": pdb_code, "title": f"PDB entry {pdb_code}", "error": str(e)}
    return {"pdb_code": pdb_code, "title": f"PDB entry {pdb_code}", "error": "max_retries"}


def escape_kotlin_string(s: str) -> str:
    """Escape special characters for Kotlin string literals."""
    return s.replace('\\', '\\\\').replace('"', '\\"').replace('\n', ' ').replace('\r', '')


def generate_kotlin_updates(all_info: list) -> str:
    """Generate Kotlin code for PdbInfoArray.kt updates."""
    output = []
    output.append("=" * 60)
    output.append("UPDATES FOR PdbInfoArray.kt")
    output.append("=" * 60)
    output.append("")
    output.append("// Add these entries before the closing ) of pdbInfoList")
    output.append("// (after the last PdbEntryInfo entry, add a comma first)")
    output.append("")

    # Sort by PDB code for consistent ordering
    sorted_info = sorted(all_info, key=lambda x: x["pdb_code"].lower())

    for info in sorted_info:
        pdb = info["pdb_code"].lower()
        title = escape_kotlin_string(info["title"])
        # Truncate very long titles
        if len(title) > 200:
            title = title[:197] + "..."
        output.append(f'            PdbEntryInfo("{pdb}", "{title}"),')

    return "\n".join(output)


def main():
    script_dir = Path(__file__).parent

    # Read PDB codes from pdb_codes.json
    pdb_codes_file = script_dir / "pdb_codes.json"
    if not pdb_codes_file.exists():
        print(f"Error: {pdb_codes_file} not found")
        print("Run scrape_pdb_codes.py first to generate this file")
        sys.exit(1)

    with open(pdb_codes_file, 'r', encoding='utf-8') as f:
        molecules_data = json.load(f)

    # Collect all unique PDB codes
    all_pdb_codes = set()
    for mol in molecules_data:
        for pdb in mol.get("pdb_codes", []):
            all_pdb_codes.add(pdb.lower())

    print(f"Found {len(all_pdb_codes)} unique PDB codes to fetch")
    print()

    # Fetch info for each PDB code
    all_info = []
    pdb_list = sorted(list(all_pdb_codes))

    for i, pdb in enumerate(pdb_list):
        print(f"[{i+1}/{len(pdb_list)}] Fetching {pdb}...", end=" ", flush=True)

        info = fetch_pdb_info(pdb)
        all_info.append(info)

        if info["error"]:
            print(f"ERROR: {info['error']}")
        else:
            # Truncate title for display
            title_preview = info["title"][:50] + "..." if len(info["title"]) > 50 else info["title"]
            print(f"OK - {title_preview}")

        # Rate limiting - RCSB allows reasonable request rates
        time.sleep(0.2)

    print(f"\nSuccessfully fetched {len(all_info)} PDB entries")

    # Count errors
    errors = [i for i in all_info if i["error"]]
    if errors:
        print(f"Errors: {len(errors)}")
        for e in errors:
            print(f"  - {e['pdb_code']}: {e['error']}")

    # Save raw data as JSON
    json_path = script_dir / "pdb_info.json"
    with open(json_path, 'w', encoding='utf-8') as f:
        json.dump(all_info, f, indent=2, ensure_ascii=False)
    print(f"\nSaved raw data to: {json_path}")

    # Generate Kotlin updates
    kotlin_updates = generate_kotlin_updates(all_info)
    kotlin_path = script_dir / "pdb_info_updates.txt"
    with open(kotlin_path, 'w', encoding='utf-8') as f:
        f.write(kotlin_updates)
    print(f"Saved Kotlin updates to: {kotlin_path}")

    print("\nDone! Review pdb_info_updates.txt and apply to PdbInfoArray.kt")


if __name__ == "__main__":
    main()
