# Fix Duplicate Classes and Missing Documentation in KMP Library

The consumer of the KMP library is experiencing "Duplicate class" errors between `api-android` and `api-jvm` artifacts. Additionally, documentation (KDoc/Sources) is missing in the consumer.

## User Review Required

> [!IMPORTANT]
> The "Duplicate class" issue is often caused by how JitPack publishes Kotlin Multiplatform (KMP) libraries. Using the root artifact coordinate `com.github.BRBXGIT.BrbxMvi:api:VERSION` is mandatory. However, if the metadata is broken, Gradle might pull both platform-specific artifacts.

> [!NOTE]
> I will add **Dokka** to generate proper documentation and ensure it is included in the published artifacts.

## Proposed Changes

### Build Logic Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/PRG/KotlinProjects/BrbxMvi/gradle/libs.versions.toml)
- Add Dokka plugin version and library.

#### [MODIFY] [configurePublish.kt](file:///C:/PRG/KotlinProjects/BrbxMvi/build-logic/convention/src/main/kotlin/com/brbx/convention/config/configurePublish.kt)
- Create a `javadocJar` task.
- Attach the `javadocJar` to all Maven publications.
- Ensure `GenerateModuleMetadata` is enabled (already present, but will double-check).

#### [MODIFY] [KmpLibraryConventionPlugin.kt](file:///C:/PRG/KotlinProjects/BrbxMvi/build-logic/convention/src/main/kotlin/com/brbx/convention/KmpLibraryConventionPlugin.kt)
- Apply the Dokka plugin.
- Ensure `withSourcesJar(publish = true)` is applied correctly.
- Add configuration to ensure the Android publication is correctly identified as a platform variant to avoid conflicts with JVM.

## Verification Plan

### Automated Tests
- Run `./gradlew publishToMavenLocal` and inspect the generated POM and `.module` files in `~/.m2/repository`.
- Check that `-sources.jar` and `-javadoc.jar` are generated for each target.

### Manual Verification
- Verify the contents of the generated `.module` file to ensure `api-android` and `api-jvm` have distinct attributes that prevent them from being pulled together.
