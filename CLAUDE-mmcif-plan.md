# Plan: Extend pdbparser with mmCIF Format Support

## Context

The pdbparser module currently only parses the legacy PDB format (80-column fixed-width "punchcard" records). The RCSB made mmCIF/PDBx mandatory for new depositions in July 2019, and the legacy format is deprecated. mmCIF uses STAR grammar with flexible key-value and loop-table syntax — no column-width limits, no atom/chain count limits. Adding mmCIF support future-proofs the app and enables access to structures too large for legacy PDB format.

The new parser must:
- Populate the **same `Molecule` object** so all downstream rendering (mollib/OpenGL) works unchanged
- Exist **in parallel** — no modifications to `ParserPdbFile.kt`
- Reuse all post-processing logic (bond matching, chain building, secondary structure annotation, centering)

### Sources Consulted

- https://pdb101.rcsb.org/learn/guide-to-understanding-pdb-data/beginner%E2%80%99s-guide-to-pdbx-mmcif
- https://pdb101.rcsb.org/learn/guide-to-understanding-pdb-data/dealing-with-coordinates
- https://pdb101.rcsb.org/learn/guide-to-understanding-pdb-data/biological-assemblies
- http://mmcif.rcsb.org/docs/tutorials/glossary/pdbx-mmcif-glossary.html
- https://mmcif.wwpdb.org/docs/user-guide/guide.html

---

## mmCIF Format Overview

mmCIF uses STAR grammar. Two data styles:

**Key-value pairs** (single values):
```
_cell.length_a     63.150
_exptl.method      'X-RAY DIFFRACTION'
```

**Loop tables** (multiple values):
```
loop_
_atom_site.group_PDB
_atom_site.id
_atom_site.type_symbol
...
ATOM   1    N  N   MET A 1 1  ?  27.340  24.430  2.614  1.00 9.67  1   MET A N   1
```

Special values: `.` = intentionally omitted/inapplicable, `?` = missing/unknown

Files begin with `data_<entryId>` (e.g., `data_4HHB`).

---

## Critical Files

| File | Role |
|------|------|
| `pdbparser/src/main/java/com/kotmol/pdbParser/ParserPdbFile.kt` | Reference for Builder pattern and post-processing methods to copy |
| `pdbparser/src/main/java/com/kotmol/pdbParser/Molecule.kt` | Shared output container — unchanged |
| `pdbparser/src/main/java/com/kotmol/pdbParser/PdbAtom.kt` | `AtomType` enum, `KotmolVector3` — reused directly |
| `pdbparser/src/main/java/com/kotmol/pdbParser/PdbHelix.kt` | Output struct for secondary structure — reused |
| `pdbparser/src/main/java/com/kotmol/pdbParser/PdbBetaSheet.kt` | Output struct for beta sheets — reused |
| `pdbparser/src/main/java/com/kotmol/pdbParser/BondInfo.kt` | 22-amino-acid + nucleic bond lookup tables — reused unchanged |
| `pdbparser/src/main/java/com/kotmol/pdbParser/ChainRenderingDescriptor.kt` | Ribbon rendering output — reused unchanged |
| `pdbparser/src/test/java/com/kotmol/pdbParser/AtomCoordTest01.kt` | Canonical test pattern to mirror |
| `mollib/src/main/java/com/bammellab/mollib/Util.kt` | Caller site — add `parseMmCifInputStream()` utility |

---

## New Files to Create

All source files in `pdbparser/src/main/java/com/kotmol/pdbParser/`:

| File | Purpose |
|------|---------|
| `MmCifToken.kt` | Sealed class hierarchy for STAR grammar tokens |
| `MmCifTokenizer.kt` | Pull-based tokenizer: reads `BufferedReader`, emits `MmCifToken` |
| `MmCifLoopHandler.kt` | Interface for category-specific loop handlers |
| `MmCifAtomSiteHandler.kt` | Handles `_atom_site` loop → writes `PdbAtom` into `Molecule` |
| `MmCifStructConfHandler.kt` | Handles `_struct_conf` loop → writes `PdbHelix` into `Molecule` |
| `MmCifStructSheetRangeHandler.kt` | Handles `_struct_sheet_range` loop → writes `PdbBetaSheet` into `Molecule` |
| `MmCifParser.kt` | Owns tokenizer; dispatches loops to handlers; includes `MmCifFormatDetector` |
| `ParserMmCifFile.kt` | Public entry point with `Builder` pattern mirroring `ParserPdbFile` |
| `MoleculeParserFactory.kt` | Auto-detects format; routes to correct parser |

Test files in `pdbparser/src/test/java/com/kotmol/pdbParser/`:

| File | Purpose |
|------|---------|
| `MmCifTokenizerTest.kt` | Tokenizer corner cases |
| `MmCifAtomCoordTest01.kt` | Basic atom → `PdbAtom`, coordinates, centering |
| `MmCifAtomCoordTest02.kt` | `auth_*` fallback columns |
| `MmCifAtomCoordTest03.kt` | Alternate location filtering |
| `MmCifAtomCoordTest04.kt` | Model number filtering |
| `MmCifHelixTest01.kt` | `_struct_conf` → `PdbHelix` |
| `MmCifSheetTest01.kt` | `_struct_sheet_range` → `PdbBetaSheet` |
| `MmCifBondTest01.kt` | Bond processing via `BondInfo` |
| `MmCifRibbonTest01.kt` | Chain descriptor population |
| `MmCifFormatDetectionTest.kt` | Format auto-detection |
| `MmCifParserTest.kt` | Builder construction and basic parse |

---

## Implementation Details

### Phase 1 — Tokenizer

**`MmCifToken.kt`** — Sealed class hierarchy:
```kotlin
sealed class MmCifToken {
    data class DataBlock(val name: String) : MmCifToken()   // data_4HHB
    object Loop : MmCifToken()                               // loop_
    data class Tag(val category: String, val attribute: String) : MmCifToken()
    data class Value(val text: String) : MmCifToken()        // plain, '.', '?'
    data class QuotedValue(val text: String) : MmCifToken()  // 'single' or "double"
    data class TextValue(val text: String) : MmCifToken()    // ;...multiline...\n;
    object End : MmCifToken()
}
```

**`MmCifTokenizer.kt`** — Pull-based iterator over `BufferedReader`:
- Lines starting with `#` → skip (comments)
- `data_NAME` → `DataBlock("NAME")`
- `loop_` → `Loop`
- `_category.attribute` → `Tag("category", "attribute")`
- Line starting with `;` → read until next line starting with `;` → `TextValue`
- `'...'` / `"..."` → `QuotedValue`
- `.` and `?` → `Value(".")` / `Value("?")`
- All other whitespace-delimited tokens → `Value`

---

### Phase 2 — Atom Site Handler + Core Parser

**`MmCifLoopHandler.kt`**:
```kotlin
interface MmCifLoopHandler {
    fun handleLoop(columnHeaders: List<Pair<String, String>>, tokenizer: MmCifTokenizer)
}
```

**`MmCifAtomSiteHandler.kt`** — Maps `_atom_site` columns to `PdbAtom`:

| mmCIF field | PdbAtom field | Notes |
|-------------|---------------|-------|
| `group_PDB` | `atomType` | ATOM→`IS_ATOM`, HETATM→`IS_HETATM` |
| `id` | `atomNumber` | Int |
| `type_symbol` | `elementSymbol` | |
| `auth_atom_id` (or `label_atom_id`) | `atomName` | `auth_*` preferred to match legacy PDB |
| `auth_comp_id` (or `label_comp_id`) | `residueName` | |
| `auth_asym_id` (or `label_asym_id`) | `chainId` | first char |
| `auth_seq_id` (or `label_seq_id`) | `residueSeqNumber` | Int |
| `Cartn_x/y/z` | `atomPosition` | Float |
| `label_alt_id` | filter | skip if not `.` or `A` |
| `pdbx_PDB_model_num` | filter | skip if not `1` |
| `pdbx_PDB_ins_code` | `residueInsertionCode` | `.` → `' '` |

- Skip `O5T` and `O3T` atoms (no bond info — mirrors existing PDB parser behaviour)
- Accumulate `sumX/Y/Z` for centroid; expose `getAveragePosition(): KotmolVector3`
- Use `auth_*` columns as primary and `label_*` as fallback for consistency with RCSB chain/residue IDs

**Why `auth_*` preferred:** `auth_asym_id` matches the familiar PDB chain letter (A, B, C…); `label_asym_id` uses internal identifiers that can differ for ligands. Using `auth_*` keeps chain grouping consistent with what the existing legacy PDB parser produces.

**`MmCifParser.kt`** — Loop dispatch:
1. Read tokens in a top-level loop
2. On `Loop`: collect consecutive `Tag` tokens to build column header list
3. Extract category from first tag; look up in `loopHandlers` map
4. If found → call `handler.handleLoop(headers, tokenizer)`
5. If not found → consume all `Value` tokens until next `Tag`/`Loop`/`End`
6. Key-value pairs are silently skipped (not needed for 3D rendering)

Registered handlers: `"atom_site"`, `"struct_conf"`, `"struct_sheet_range"`

Also contains `MmCifFormatDetector`:
```kotlin
object MmCifFormatDetector {
    // Buffer first 4096 bytes; look for "data_" at a line start (mmCIF)
    // vs "ATOM"/"HEADER"/"REMARK" at a line start (PDB)
    // Returns (isMmCif: Boolean, reconstitutedStream: InputStream)
    fun detect(stream: InputStream): Pair<Boolean, InputStream>
}
```

**`ParserMmCifFile.kt`** — Public Builder mirroring `ParserPdbFile`:
```kotlin
ParserMmCifFile.Builder(mol: Molecule)
    .setMoleculeName(String)
    .setMessageStrings(MutableList<String>)
    .loadMmCifFromStream(InputStream)   // NOTE: distinct name from PDB version's loadPdbFromStream()
    .doBondProcessing(Boolean = true)
    .centerTheMolecule(Boolean = true)
    .parse()
```

`parse()` sequence:
1. Run `MmCifParser.parse(stream)` → populates atoms, helices, sheets into `mol`
2. Set `mol.averagePosition` from `atomSiteHandler.getAveragePosition()`
3. If `doBondProcessing`: run all 5 post-processing steps
4. If `centerTheMolecule`: run `centerMolecule()`

---

### Phase 3 — Secondary Structure Handlers

**`MmCifStructConfHandler.kt`** — Maps `_struct_conf` to `PdbHelix`:

| mmCIF field | PdbHelix field | Notes |
|-------------|----------------|-------|
| `conf_type_id` | filter | only rows starting with `HELX` |
| `id` | `helixId` | |
| `beg_label_asym_id` | `initialChainIdChar` | first char |
| `beg_label_seq_id` | `initialResidueNumber` | Int |
| `end_label_asym_id` | `terminalChainIdChar` | first char |
| `end_label_seq_id` | `terminalResidueNumber` | Int |
| (counter) | `serialNumber` | auto-increment per helix |
| (default `1`) | `helixClass` | right-handed alpha; mmCIF doesn't encode numeric class |
| (null) | `initialResidueName`, `terminalResidueName` | not in `_struct_conf` |

`conf_type_id` values encountered in RCSB data: `HELX_P` (protein helix), `HELX_RH_AL_P` (right-handed alpha), `STRN` (strand — skip). Any value starting with `HELX` → accept.

**`MmCifStructSheetRangeHandler.kt`** — Maps `_struct_sheet_range` to `PdbBetaSheet`:

| mmCIF field | PdbBetaSheet field |
|-------------|-------------------|
| `sheet_id` | `sheetIdentification` |
| `id` | `strandNumber` (Int) |
| `beg_label_asym_id` | `initialChainIdChar` |
| `beg_label_seq_id` | `initialResidueNumber` |
| `end_label_asym_id` | `terminalChainIdChar` |
| `end_label_seq_id` | `terminalResidueNumber` |
| (defaults) | `parallelSenseCode=0`, registration fields=`null`/`' '` |

`addSheetSecondaryInformation()` only reads `initialResidueNumber`, `initialChainIdChar`, `terminalResidueNumber`, `terminalChainIdChar` — the missing registration fields are safe.

---

### Phase 4 — Post-processing Reuse Strategy

**Option A (implement first — zero risk):**
Copy the 9 private post-processing methods from `ParserPdbFile` as private methods inside `ParserMmCifFile.Builder`. They operate only on `Molecule`, so copies are behaviourally identical. No changes to any existing file.

Methods to copy: `mapBonds`, `matchBonds`, `findNextBond`, `addBond`, `buildPdbChainLists`, `addHelixSecondaryInformation`, `addSheetSecondaryInformation`, `connectResidues`, `centerMolecule`.

**Option B (follow-up refactor):**
Extract those 9 methods into `MoleculePostProcessor.kt` as `internal` functions; update `ParserPdbFile` to delegate. All 23 existing tests must remain green. This is the clean long-term architecture — deferred to keep initial risk low.

---

### Phase 5 — Format Detection + mollib Integration

**`MoleculeParserFactory.kt`**:
```kotlin
object MoleculeParserFactory {
    fun parse(
        mol: Molecule,
        stream: InputStream,
        name: String = "",
        messages: MutableList<String> = mutableListOf()
    )
    // Calls MmCifFormatDetector.detect(), routes to correct Builder
}
```

**`mollib/Util.kt`** — Add alongside existing `parsePdbInputStream()`:
```kotlin
fun parseMmCifInputStream(stream: InputStream, mol: Molecule, pdbName: String)
```

---

## `_atom_site` Loop Header — Real RCSB Example

```
loop_
_atom_site.group_PDB
_atom_site.id
_atom_site.type_symbol
_atom_site.label_atom_id
_atom_site.label_comp_id
_atom_site.label_asym_id
_atom_site.label_entity_id
_atom_site.label_seq_id
_atom_site.pdbx_PDB_ins_code
_atom_site.Cartn_x
_atom_site.Cartn_y
_atom_site.Cartn_z
_atom_site.occupancy
_atom_site.B_iso_or_equiv
_atom_site.auth_seq_id
_atom_site.auth_comp_id
_atom_site.auth_asym_id
_atom_site.auth_atom_id
_atom_site.pdbx_PDB_model_num
ATOM   1    N  N   MET A 1 1  ?  27.340  24.430  2.614  1.00 9.67  1   MET A N   1
ATOM   2    C  CA  MET A 1 1  ?  26.300  23.500  2.500  1.00 8.30  1   MET A CA  1
```

---

## Testing

All tests use inline mmCIF strings via `String.byteInputStream()` — same pattern as existing tests in `AtomCoordTest01.kt`.

| Test file | What it verifies |
|-----------|-----------------|
| `MmCifTokenizerTest` | Key-value pairs, loop_, semicolon text blocks, quoted strings, `.`/`?` |
| `MmCifAtomCoordTest01` | Basic atom parse → `PdbAtom`, correct coordinates after centering |
| `MmCifAtomCoordTest02` | `auth_*` fallback when `label_*` absent |
| `MmCifAtomCoordTest03` | Alternate location filter (only `.` and `A` kept) |
| `MmCifAtomCoordTest04` | Model number filter (only model `1` kept) |
| `MmCifHelixTest01` | `_struct_conf` → `mol.helixList`; `STRN` rows excluded |
| `MmCifSheetTest01` | `_struct_sheet_range` → `mol.pdbSheetList`, 2 strands |
| `MmCifBondTest01` | Full ALA residue → bond count matches `BondInfo` template |
| `MmCifRibbonTest01` | 9 CA-only atoms → `listofChainDescriptorLists.size == 1`, 9 nodes |
| `MmCifFormatDetectionTest` | `data_4HHB\n` → true; `ATOM ...` → false |
| `MmCifParserTest` | Builder construction, 1-atom parse, message string retention |

```bash
# Run all pdbparser tests (new + existing)
./gradlew.bat :pdbparser:test

# Run a single new test class
./gradlew.bat :pdbparser:test --tests "com.kotmol.pdbParser.MmCifAtomCoordTest01"
```

Regression gate: all 23 existing pdbparser tests must remain green.

---

---

### Phase 6 — Download Layer: Switch from Legacy PDB to mmCIF

This phase wires the completed mmCIF parser into the live download pipeline. The RCSB serves mmCIF files at `https://files.rcsb.org/download/<ID>.cif.gz` — the same base URL and gzip transport as the legacy `.pdb.gz` files. `GZIPInputStream` is already present in `PdbDownload.kt`, so no new decompression code is needed.

#### Files to Change

**`mollib/src/main/java/com/bammellab/mollib/pdbDownload/MollibDefs.kt`**

Add a constant for the new extension (keep the old one until all callers are migrated):
```kotlin
const val RCSB_DOWNLOAD_PATH = "https://files.rcsb.org/download/"
const val RCSB_MMCIF_EXTENSION = ".cif.gz"   // new
// const val RCSB_PDB_EXTENSION = ".pdb.gz"  // legacy — remove when migration complete
```

**`mollib/src/main/java/com/bammellab/mollib/pdbDownload/PdbDownload.kt`**

Three changes:

1. **URL construction** — change suffix from `.pdb.gz` to `.cif.gz`:
   ```kotlin
   // before
   val url = RCSB_DOWNLOAD_PATH + pdbid + ".pdb.gz"
   // after
   val url = RCSB_DOWNLOAD_PATH + pdbid + RCSB_MMCIF_EXTENSION
   ```

2. **Cache file naming** — store and look up as `.cif` (not `.pdb`):
   ```kotlin
   // checkCacheForPdb: "PDB/$pdbid.pdb"  →  "PDB/$pdbid.cif"
   // downloadPdbFromHttp: File(cacheDir, "PDB/$pdbid.pdb")  →  File(cacheDir, "PDB/$pdbid.cif")
   ```
   > **Cache migration note:** Existing `.pdb` files already on device will simply not be found under the new `.cif` name — they will be re-downloaded as `.cif`. No explicit migration is required; old `.pdb` files in `externalCacheDir/PDB/` can be left to expire naturally.

3. **Callback routing** — the `PdbCallback.loadPdbFromStream()` method name is kept as-is (renaming the interface is out of scope), but the implementation in `MollibProcessPdbs` must route to the mmCIF parser:
   ```kotlin
   // before (MollibProcessPdbs.kt line 421)
   override fun loadPdbFromStream(stream: InputStream) {
       parsePdbInputStream(stream, mol, pdbFileNames[nextNameIndex])
       ...
   }
   // after
   override fun loadPdbFromStream(stream: InputStream) {
       parseMmCifInputStream(stream, mol, pdbFileNames[nextNameIndex])
       ...
   }
   ```
   `parseMmCifInputStream()` already exists in `mollib/Util.kt` (added in Phase 5).

#### Caller Summary

| File | Change needed |
|------|---------------|
| `MollibDefs.kt` | Add `RCSB_MMCIF_EXTENSION` constant |
| `PdbDownload.kt` | URL suffix + cache filename: `.pdb` → `.cif` |
| `MollibProcessPdbs.kt` | `parsePdbInputStream` → `parseMmCifInputStream` in callback |

`MoleculeParserFactory.kt` (already routes by format detection) and `mollib/Util.kt` (already has `parseMmCifInputStream`) require no changes.

#### Testing the Download Layer

No new unit tests are needed (network I/O is not unit-tested). Verify manually with a connected device run:
- Launch the app, navigate to any molecule, confirm the 3D structure renders correctly.
- Check Logcat for `"downloading the PDB in GZIP form"` log line and confirm `.cif.gz` URL is used.
- Kill the app, relaunch, navigate to the same molecule — confirm it loads from the `.cif` cache without a network request.

---

## Implementation Sequence

1. Write this file to project root as `CLAUDE-mmcif-plan.md`
2. `MmCifToken.kt` + `MmCifTokenizer.kt` + `MmCifTokenizerTest.kt` → green
3. `MmCifLoopHandler.kt` + `MmCifAtomSiteHandler.kt` + `MmCifParser.kt` (atom_site only) + `ParserMmCifFile.kt` (Option A copy of post-processing)
4. `MmCifAtomCoordTest01-04.kt` + `MmCifParserTest.kt` → green
5. `MmCifStructConfHandler.kt` + `MmCifStructSheetRangeHandler.kt` → register in `MmCifParser`
6. `MmCifHelixTest01.kt` + `MmCifSheetTest01.kt` → green
7. `MmCifBondTest01.kt` + `MmCifRibbonTest01.kt` → green
8. `MmCifFormatDetector` (in `MmCifParser.kt`) + `MoleculeParserFactory.kt` + `MmCifFormatDetectionTest.kt` → green
9. Add `parseMmCifInputStream()` to `mollib/Util.kt`
10. **Phase 6**: Update download layer — `MollibDefs.kt` + `PdbDownload.kt` + `MollibProcessPdbs.kt`
11. (Optional follow-up) Option B refactor: extract `MoleculePostProcessor.kt`, update `ParserPdbFile` to delegate
