# PDB Parser Module

The `pdbparser` module is a pure Kotlin/JVM library for parsing
[Protein Data Bank](https://www.rcsb.org/) (PDB) files. It extracts atomic
coordinates, covalent bond information, and secondary structure (helices and
beta sheets) from PDB format text files.

The source originates from the
[KotmolPdbParser](https://github.com/kotmol/KotmolPdbParser) project
(previously consumed as a Maven library) and is maintained here as a local
module for full in-repo access and debuggability.

## What it parses

Given a PDB file, the parser reads:

- **ATOM / HETATM records** — atomic coordinates (x, y, z in Ångströms),
  element symbol, residue name and number, chain ID, insertion code
- **CONECT records** — explicit covalent bonds defined in the file
- **HELIX records** — right/left-handed alpha helices and other helix classes
- **SHEET records** — beta sheet strands with parallel/antiparallel sense

Bond information for standard amino acids and nucleotides comes from a
hardcoded database derived from the
[EBI PDBe Bonds API](https://www.ebi.ac.uk/pdbe/api/pdb/compound/bonds/).
This covers all 20 standard amino acids plus nucleotides (A, G, C, T, U).

## Core classes

| Class | Purpose |
|---|---|
| `ParserPdbFile` | Main entry point — Builder pattern, call `.loadPdbFromStream(stream).parse()` |
| `Molecule` | Output container: atom map, bond list, helix list, sheet list, chain lists |
| `PdbAtom` | Single atom: position (`KotmolVector3`), element, residue, chain, `AtomType` enum |
| `KotmolVector3` | Float x/y/z vector with `distanceTo()` |
| `BondInfo` | ~80 KB hardcoded bond table for all standard residues (from EBI data) |
| `AtomInformationTable` | Periodic table: Van der Waals radii and RGBA display colors for all 118 elements |
| `ChainRenderingDescriptor` | Per-residue rendering metadata: `SecondaryStructureType` (RIBBON, ALPHA_HELIX, BETA_SHEET, NUCLEIC) and `NucleicType` (PURINE, PYRIMIDINE) |
| `PdbHelix` | Data from a `HELIX` record |
| `PdbBetaSheet` | Data from a `SHEET` record |

## Typical usage

```kotlin
val mol = Molecule()
ParserPdbFile.Builder(mol)
    .setMoleculeName("1BGL")
    .loadPdbFromStream(inputStream)
    .doBondProcessing(true)
    .centerAtomCoordinates(true)
    .parse()

// Access results
val atoms  = mol.atomNumberToAtomInfoHash   // HashMap<Int, PdbAtom>
val bonds  = mol.bondList                   // List<Bond>
val helices = mol.helixList                 // List<PdbHelix>
val sheets  = mol.pdbSheetList              // List<PdbBetaSheet>
val chains  = mol.listofChainDescriptorLists // List<List<ChainRenderingDescriptor>>
```

Builder options:

| Method | Default | Effect |
|---|---|---|
| `setMoleculeName(name)` | `""` | Tags the molecule for logging |
| `setMessageStrings(list)` | none | Collects parser log messages |
| `doBondProcessing(bool)` | `true` | Resolve bonds from `BondInfo` table |
| `centerAtomCoordinates(bool)` | `false` | Translate atoms so centroid = origin |

## Module location

```
pdbparser/
├── build.gradle.kts                           # Pure Kotlin/JVM, JUnit 5, Java 17
└── src/
    ├── main/java/com/kotmol/pdbParser/        # 8 source files
    └── test/java/com/kotmol/pdbParser/        # 23 unit tests
```

## Build and test

```bash
./gradlew.bat :pdbparser:test               # Run all 23 unit tests
./gradlew.bat :pdbparser:build              # Compile and test
./gradlew.bat :pdbparser:test --tests "com.kotmol.pdbParser.AtomCoordTest01"
```

## Updating from upstream

The upstream project lives at `C:\a\j\kotlinIdea\KotmolPdbParser`.
To pull in changes, copy updated `.kt` files from:

```
KotmolPdbParser/kotmolpdbparser/src/main/java/com/kotmol/pdbParser/
```

into:

```
pdbparser/src/main/java/com/kotmol/pdbParser/
```

Then run `:pdbparser:test` to verify nothing regressed. Note: the project now
targets Kotlin 2.x / Java 17 — any deprecated API use (e.g. `toLowerCase()`)
must be updated to current equivalents (`lowercase()`, `lowercaseChar()`).

## Test PDB files used in tests

| PDB ID | Description |
|---|---|
| 1AEW | Small — cadmium with explicit CONECT bonds |
| 1BGL | Large — two complexes, exercises bond table |
| 1IDR, 1B89 | Edge cases — orphan atoms |
