#!/bin/sh
# Gradle wrapper stub - downloads and runs gradle
GRADLE_VERSION=8.5
GRADLE_URL="https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip"
GRADLE_DIR="$HOME/.gradle/wrapper/dists/gradle-${GRADLE_VERSION}-bin"

if [ ! -f "$GRADLE_DIR/gradle-${GRADLE_VERSION}/bin/gradle" ]; then
    mkdir -p "$GRADLE_DIR"
    curl -sL "$GRADLE_URL" -o "/tmp/gradle-${GRADLE_VERSION}-bin.zip"
    unzip -qo "/tmp/gradle-${GRADLE_VERSION}-bin.zip" -d "$GRADLE_DIR"
fi

exec "$GRADLE_DIR/gradle-${GRADLE_VERSION}/bin/gradle" "$@"
