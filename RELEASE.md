# Release Build Instructions

## Prerequisites

1. **Signing Configuration**: Create `gradle/signing.properties` with:
   ```properties
   STORE_FILE=/path/to/your/keystore.jks
   STORE_PASSWORD=your_store_password
   KEY_ALIAS=your_key_alias
   KEY_PASSWORD=your_key_password
   ```

## Build Commands

### Debug APK
```bash
./gradlew.bat :motmbrowser:assembleDebug
```

### Release APK
```bash
./gradlew.bat :motmbrowser:assembleRelease
```

### Release AAB (for Google Play)
```bash
./gradlew.bat :motmbrowser:bundleRelease
```

## Output Locations

| Build Type | Output Path |
|------------|-------------|
| Debug APK | `motmbrowser/build/outputs/apk/debug/` |
| Release APK | `motmbrowser/build/outputs/apk/release/` |
| Release AAB | `motmbrowser/build/outputs/bundle/release/` |
| Mapping File | `motmbrowser/build/outputs/mapping/release/mapping.txt` |

## R8 Obfuscation

Release builds use R8 for code shrinking and obfuscation:
- `isMinifyEnabled = true` - Enables R8 code shrinking and obfuscation
- `isShrinkResources = true` - Removes unused resources
- ProGuard rules: `motmbrowser/src/main/proguard-motmbrowser.pro`

## Crash Report Deobfuscation

With AGP 8.13.2+, the mapping file is automatically embedded in the AAB under `BUNDLE-METADATA/com.android.tools.build.obfuscation/proguard.map`.

When uploading to Google Play:
- Google Play extracts the mapping file automatically
- Crash reports in Play Console are deobfuscated automatically
- No manual mapping file upload required

For local debugging of obfuscated crash logs, use the mapping file at:
`motmbrowser/build/outputs/mapping/release/mapping.txt`

## Version Management

Version numbers are configured in `motmbrowser/build.gradle.kts`:
```kotlin
val versionMajor = 2
val versionMinor = 9
val versionPatch = 0
val versionBuild = 2901
```

Update these values before creating a new release.
