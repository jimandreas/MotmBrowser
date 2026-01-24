# Testing Guide

This document describes the testing infrastructure and test suites for the MotmBrowser project.

## Quick Start

```bash
# Run all tests
./gradlew.bat test

# Run mollib tests only
./gradlew.bat :mollib:test

# Run motmbrowser tests only
./gradlew.bat :motmbrowser:testDebugUnitTest

# Run a specific test class
./gradlew.bat :mollib:test --tests "com.bammellab.mollib.data.CorpusTest"

# Run a specific test method
./gradlew.bat :mollib:test --tests "com.bammellab.mollib.data.CorpusTest.testCorpusSize"
```

## Test Framework

- **JUnit 5 (Jupiter)** - Test framework
- **Truth** - Fluent assertions from Google
- **JUnit Platform Launcher** - Required for Gradle 9.x compatibility

## Test Suites

### mollib Tests

Located in `mollib/src/test/java/com/bammellab/mollib/data/`

#### CorpusTest

Validates the Molecule of the Month corpus data.

| Test | Description |
|------|-------------|
| `testMonthMap` | Verifies MOTM key to date string conversion |
| `motmImageListGet` | Checks motmThumbnailImageList ordering and numbering |
| `verifyCorpusTitlesMatchWebsite` | **Website verification** - Compares corpus titles against https://pdb101.rcsb.org/motm/motm-by-date |

#### PDBsTest

Validates PDB code mappings and data consistency.

| Test | Description |
|------|-------------|
| `pullTheMapTest` | Basic structure test of MOTM-to-PDB mapping |
| `matchPdbGroupedListWithPdbInfoList` | Verifies PDB codes have corresponding info entries |
| `matchPdbInfoListToMotmMapList` | Verifies all PdbInfo entries have MOTM mappings |

#### MotmByCategoryTest

Validates category data against the RCSB website.

| Test | Description |
|------|-------------|
| `verifyMotmCategoryHealthMatchesWebsite` | **Website verification** - Validates MotmCategoryHealth against https://pdb101.rcsb.org/motm/motm-by-category |

#### MotmImageDownloadTest

Tests image download URL generation.

| Test | Description |
|------|-------------|
| `testGetFirstTiffImageURL` | Tests TIFF image URL lookup function |
| `buildMotmImageList` | Verifies screensaver image list generation |

### motmbrowser Tests

Located in `motmbrowser/src/test/java/com/bammellab/motm/build/`

#### ReleaseBuildTest

Validates release build configuration for Google Play deployment. These tests are **skipped** if no release AAB has been built.

| Test | Description |
|------|-------------|
| `verifyReleaseBundleContainsMappingFile` | Checks AAB contains the R8 mapping file at the correct path |
| `verifyReleaseBundleIsMinified` | Verifies DEX files are present in the bundle |
| `verifyMappingFileFormat` | Validates mapping file format for Google Play compatibility |

**To run these tests:**

```bash
# First build the release AAB
./gradlew.bat :motmbrowser:bundleRelease

# Then run the verification tests
./gradlew.bat :motmbrowser:testDebugUnitTest --tests "com.bammellab.motm.release.ReleaseBuildTest"
```

## Website Verification Tests

Several tests fetch data from the RCSB PDB101 website to verify local data is up-to-date:

- `CorpusTest.verifyCorpusTitlesMatchWebsite` - Verifies MOTM titles
- `MotmByCategoryTest.verifyMotmCategoryHealthMatchesWebsite` - Verifies category data

These tests require network access. If the website is unreachable, the tests are skipped gracefully.

## Release Build Verification

The `ReleaseBuildTest` suite verifies that release builds are properly configured for Google Play:

1. **Mapping File Presence** - The AAB must contain `BUNDLE-METADATA/com.android.tools.build.obfuscation/proguard.map`

2. **Mapping File Content** - The file must:
   - Be at least 1KB in size
   - Contain R8 mapping syntax (`->`)
   - Reference the `com.bammellab` package
   - Have at least 10 class mappings
   - Have at least 50 member mappings

3. **DEX Files** - The AAB must contain compiled DEX files

This ensures crash reports in Google Play Console can be automatically deobfuscated.

## Adding New Tests

### mollib Module

1. Create test class in `mollib/src/test/java/com/bammellab/mollib/`
2. Use JUnit 5 annotations (`@Test`, `@DisplayName`, `@BeforeEach`, etc.)
3. Use Truth assertions (`assertThat(...)`)

Example:
```kotlin
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MyNewTest {
    @Test
    @DisplayName("descriptive test name")
    fun testSomething() {
        assertThat(actual).isEqualTo(expected)
    }
}
```

### motmbrowser Module

1. Create test class in `motmbrowser/src/test/java/com/bammellab/motm/`
2. Follow the same patterns as mollib tests

## Continuous Integration

Tests should be run before:
- Creating a pull request
- Building a release
- Updating MOTM data files

The website verification tests help catch data drift between local files and the authoritative RCSB source.

## Troubleshooting

### JUnit Platform Launcher Error

If you see "Failed to load JUnit Platform", ensure `junit-platform-launcher` is in `testRuntimeOnly`:

```kotlin
testRuntimeOnly(libs.junit.platform.launcher)
```

### Website Tests Failing

Website verification tests may fail if:
- Network is unavailable (tests are skipped)
- RCSB website structure changed (update parsing logic)
- Local data is out of sync (update local data files)

### Release Build Tests Skipped

Release build tests are skipped when no AAB exists. Build first:

```bash
./gradlew.bat :motmbrowser:bundleRelease
```

Note: Release builds require `gradle/signing.properties` with valid signing credentials.
