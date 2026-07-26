# Robot Fox Desktop Pet

A small Java Swing desktop companion that demonstrates animation, system-tray
integration, and runtime plugin discovery with `ServiceLoader`.

<p align="center">
  <img src="artwork/robot-fox-source.png" alt="Coral-orange robot fox mascot" width="360">
</p>

## Highlights

- Animated transparent desktop window with screen-edge collision
- Start, stop, and exit actions in the operating-system tray
- Pet and tray plugins discovered from external JAR files
- Reproducible Java 17 and Gradle 9.6 build
- Headless unit and plugin-packaging tests in GitHub Actions
- Original robot-fox artwork with reproducible derived assets

## Quick start

Requirements: Java 17 or newer. No system Gradle installation is required.

```bash
./gradlew clean build
./gradlew run
```

The second command launches a graphical desktop application, so it must run in
a desktop session rather than a headless server.

Build outputs include the application distribution and example plugins:

```text
build/distributions/
build/libs/
build/plugins/pet/
build/plugins/tray/
```

## Architecture

```text
PluginInterface/    PetPlugin and TrayPlugin contracts
src/main/           Swing application, animation, tray, and plugin loading
PluginPetMove/      Example sprite-sheet pet plugin
PluginTrayAid/      Example tray plugin installer
picture/            Generated runtime frames and tray icons
artwork/            Original high-resolution mascot source
tools/              Reproducible artwork derivation tool
```

`PluginManager` scans `build/plugins/pet` and `build/plugins/tray`. Each plugin
JAR registers an implementation through `META-INF/services`.

Application preferences are saved to
`~/.desktop-pet/settings.properties`, outside the repository.

## Regenerate artwork

The committed runtime images are derived from the original mascot source with a
small Java 17 tool:

```bash
./gradlew generateArtwork
```

See [ARTWORK.md](ARTWORK.md) for the asset provenance and generation prompt.

## Verification

```bash
./gradlew clean build
```

This compiles the app and both plugin source sets, verifies runtime artwork,
runs unit and integration tests, and packages the example plugin JARs.

## Project status

This is a portfolio-scale desktop experiment, not a production desktop agent.
The graphical window and operating-system tray still require manual validation
on each supported desktop environment.

## License

The repository is public for viewing and portfolio evaluation. No open-source
license is currently granted; see [LICENSE](LICENSE). Original team credits are
listed in [CONTRIBUTORS.md](CONTRIBUTORS.md).
