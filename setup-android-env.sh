#!/usr/bin/env bash
# Run this ONCE inside your GitHub Codespace, from the maze-game project root.
# It installs JDK 17, the Android command-line SDK tools, and generates the
# Gradle wrapper so you can build the APK with ./gradlew afterward.
set -e

echo "==> Installing OpenJDK 17..."
sudo apt update
sudo apt install -y openjdk-17-jdk unzip

echo "==> Setting up Android SDK..."
export ANDROID_HOME="$HOME/android-sdk"
mkdir -p "$ANDROID_HOME/cmdline-tools"
cd "$ANDROID_HOME/cmdline-tools"

if [ ! -d "latest" ]; then
  curl -o tools.zip https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
  unzip -q tools.zip
  mv cmdline-tools latest
  rm tools.zip
fi

export PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools"

echo "==> Accepting licenses and installing SDK packages..."
yes | sdkmanager --licenses > /dev/null
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

echo "==> Persisting environment variables to ~/.bashrc..."
{
  echo ""
  echo "# Android SDK (added by setup-android-env.sh)"
  echo "export ANDROID_HOME=\"$ANDROID_HOME\""
  echo "export PATH=\"\$PATH:\$ANDROID_HOME/cmdline-tools/latest/bin:\$ANDROID_HOME/platform-tools\""
} >> "$HOME/.bashrc"

echo "==> Installing Gradle (temporary, only to generate the wrapper)..."
cd "$HOME"
if [ ! -d "gradle-8.7" ]; then
  curl -L -o gradle.zip https://services.gradle.org/distributions/gradle-8.7-bin.zip
  unzip -q gradle.zip
  rm gradle.zip
fi

echo "==> Generating Gradle wrapper in project..."
cd - > /dev/null
"$HOME/gradle-8.7/bin/gradle" wrapper --gradle-version 8.7

echo "==> Done! Run 'source ~/.bashrc' then './gradlew assembleDebug' to build the APK."
