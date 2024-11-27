fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android bump_patch

```sh
[bundle exec] fastlane android bump_patch
```

Bump patch version (0.0.X)

### android bump_minor

```sh
[bundle exec] fastlane android bump_minor
```

Bump minor version (0.X.0)

### android bump_major

```sh
[bundle exec] fastlane android bump_major
```

Bump major version (X.0.0)

### android upload_to_play_store

```sh
[bundle exec] fastlane android upload_to_play_store
```

Deploy a new version to the Google Play

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
