# KotmolPdbParser Integration

## Overview

The PDB parser library (`com.kotmol.kotmolpdbparser:kotmolpdbparser`) has been moved from
a Maven Central dependency into the repo as a local Gradle module at `pdbparser/`.

This gives full control over the source, enables in-repo debugging, and eliminates the
external Maven dependency.

## Module Location

```
pdbparser/
├── build.gradle.kts                           # Pure Kotlin/JVM module, JUnit 5
└── src/
    ├── main/java/com/kotmol/pdbParser/        # 8 core source files
    │   ├── AtomInformationTable.kt            # Periodic table (118 elements, VdW radii, colors)
    │   ├── BondInfo.kt                        # ~80KB hardcoded bond database from EBI
    │   ├── ChainRenderingDescriptor.kt        # Chain rendering metadata (RIBBON, ALPHA_HELIX, etc.)
    │   ├── Molecule.kt                        # Output container (atoms, bonds, helices, sheets)
    │   ├── ParserPdbFile.kt                   # Main parser (Builder pattern)
    │   ├── PdbAtom.kt                         # Atom data + KotmolVector3
    │   ├── PdbBetaSheet.kt                    # SHEET record data
    │   └── PdbHelix.kt                        # HELIX record data
    └── test/java/com/kotmol/pdbParser/        # 23 unit test files
```

## Usage

The package name `com.kotmol.pdbParser` is unchanged — existing imports in `mollib/` work
without modification.

```kotlin
val mol = Molecule()
ParserPdbFile.Builder(mol)
    .loadPdbFromStream(pdbByteStream)
    .parse()
// Access mol.atomNumberToAtomInfoHash, mol.bondList, mol.helixList, etc.
```

## Build Commands

```bash
# Run pdbparser unit tests only
./gradlew.bat :pdbparser:test

# Build mollib (depends on pdbparser)
./gradlew.bat :mollib:build

# Full build
./gradlew.bat build
```

## Updating the Parser Source

The upstream source is at `C:\a\j\kotlinIdea\KotmolPdbParser`.
To pull in changes, copy the updated `.kt` files from:
```
KotmolPdbParser/kotmolpdbparser/src/main/java/com/kotmol/pdbParser/
```
into:
```
pdbparser/src/main/java/com/kotmol/pdbParser/
```
Then run `:pdbparser:test` to confirm nothing broke.

## Modules That Depend on pdbparser

| Module         | Dependency                  |
|----------------|-----------------------------|
| mollib         | `project(":pdbparser")`     |
| motmbrowser    | `project(":pdbparser")`     |
| standalone     | `project(":pdbparser")`     |
| captureimages  | `project(":pdbparser")`     |

## Original Source

The parser was originally published to Maven Central as:
`com.kotmol.kotmolpdbparser:kotmolpdbparser:1.0.5`

Source repo: `C:\a\j\kotlinIdea\KotmolPdbParser` (local) / GitHub: kotmol/KotmolPdbParser
