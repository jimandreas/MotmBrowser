# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build all modules
./gradlew.bat build

# Build specific module
./gradlew.bat :motmbrowser:assembleDebug
./gradlew.bat :mollib:build

# Clean build
./gradlew.bat clean build

# Build release (requires gradle/signing.properties)
./gradlew.bat :motmbrowser:assembleRelease

# Build release AAB (for Google Play)
./gradlew.bat :motmbrowser:bundleRelease
```

## Release Build with R8 Obfuscation

Release builds use R8 for code shrinking and obfuscation. R8 scrambles function names (e.g., `calculateUserScore()` becomes `a()`) to reduce file size and protect intellectual property.

**Mapping files** are automatically included in the AAB under `BUNDLE-METADATA/` (AGP 8.13.2+). Google Play uses these to deobfuscate crash reports automatically - no manual upload required.

For local debugging, mapping files are generated at:
- `motmbrowser/build/outputs/mapping/release/mapping.txt`

ProGuard rules are in `motmbrowser/src/main/proguard-motmbrowser.pro`.

## Testing

```bash
# Run all tests
./gradlew.bat test

# Run mollib unit tests only
./gradlew.bat :mollib:test
```

Tests use JUnit 5 (Jupiter) with Truth assertions. Test files are in `mollib/src/test/`.

## Linting

```bash
./gradlew.bat lint
```

Lint configuration is in `lint.xml` at the repository root.

## Architecture

This is a multi-module Android project written in Kotlin that displays 3D molecular structures from the RCSB Protein Data Bank "Molecule of the Month" series.

### Modules

- **motmbrowser/** - Main Android application
  - MVVM architecture with ViewModels and Fragments
  - AndroidX Navigation for fragment navigation
  - Key packages: `browse/`, `search/`, `detail/`, `graphics/`, `settings/`

- **mollib/** - Core library (shared across all apps)
  - `objects/` - OpenGL ES rendering (RendererDisplayPdbFile, BufferManager, RenderRibbon, etc.)
  - `data/` - Static molecule data (PDBs.kt, Corpus.kt, MotmByCategory.kt)
  - `common/math/` - 3D math utilities (Matrix4, Quaternion, MotmVector3)
  - `pdbDownload/` - Network download with OkHttp3

- **standalone/** - Test app for graphics development (loads PDB files from `/storage/emulated/0/PDB/`)

- **captureimages/** - Utility for generating PDB thumbnail images

- **screensaver/** - Muzei live wallpaper plugin

### Key Technical Details

- OpenGL ES 2.0/3.0 for 3D molecule rendering
- PDB file parsing via `kotmolpdbparser` library
- Target SDK 34, min SDK 21, Java 17
- Networking: OkHttp3 + Retrofit2
- Image loading: Glide

### Test PDB Files

- Small: 1AEW (cadmium with bonds)
- Large: 1BGL (two complexes)
- Edge cases: 1IDR, 1B89 (orphan atoms)
