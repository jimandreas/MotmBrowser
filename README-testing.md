# Long-Running Integration Tests

This document describes tests that are excluded from the normal `./gradlew test` run because they require network access and take significant time (10–30 minutes).

---

## MmCifDownloadParseAllTest

**File:** `mollib/src/test/java/com/bammellab/mollib/data/MmCifDownloadParseAllTest.kt`

**What it does:**

1. Collects every PDB ID from `mollib/src/main/java/com/bammellab/mollib/data/PDBs.kt`
2. Deduplicates them (~500–600 unique IDs from ~1 700 list entries)
3. Downloads each one from RCSB as `https://files.rcsb.org/download/<ID>.cif.gz`
4. Decompresses with `GZIPInputStream` and parses with `ParserMmCifFile`
5. Reports a per-run summary: pass / fail / HTTP 404 counts
6. Fails the test if any structure parses with 0 atoms or throws an exception (404s are warnings only)

**How to run:**

```bash
./gradlew.bat :mollib:testLongRunning
```

Output appears in the Gradle console and in the HTML report:

```
mollib/build/reports/tests/testLongRunning/index.html
```

**Skipping behavior:**

- If the network is unreachable the test prints a skip message and exits cleanly (no failure).
- HTTP 404 responses are logged but do not count as failures — some legacy PDB entries have been removed from RCSB.

**Why it is excluded from the default test run:**

The test is tagged `@Tag("long-running")`. The default `:mollib:test` Gradle task is configured to `excludeTags("long-running")` in `mollib/build.gradle.kts`.

---

## Running all tests (unit + long-running)

```bash
# Fast: unit tests only (default)
./gradlew.bat :mollib:test

# Slow: long-running network tests only
./gradlew.bat :mollib:testLongRunning
```
