# Maze Game (Android, Kotlin + Jetpack Compose)

A simple 2D top-down maze game. A maze is generated with the recursive
backtracker algorithm, you move a player dot with on-screen arrow buttons
or swipe gestures, and you win by reaching the green exit cell.

## Project structure

```
maze-game/
├── build.gradle.kts                 (root Gradle config)
├── settings.gradle.kts
├── gradle.properties
├── setup-android-env.sh             (run this first, once)
└── app/
    ├── build.gradle.kts             (app module config)
    └── src/main/
        ├── AndroidManifest.xml
        ├── res/values/themes.xml
        └── java/com/example/mazegame/
            ├── MazeGenerator.kt     (maze generation algorithm)
            ├── GameViewModel.kt     (player position, moves, win state)
            ├── GameScreen.kt        (Canvas drawing + controls UI)
            └── MainActivity.kt      (entry point)
```

## Steps to build the APK in GitHub Codespaces

### 1. Get the project into your Codespace
Upload/extract this `maze-game` folder at the root of your Codespace
(or push it to a repo and open a Codespace on that repo).

### 2. Run the one-time environment setup
```bash
cd maze-game
chmod +x setup-android-env.sh
./setup-android-env.sh
```
This installs JDK 17, the Android SDK command-line tools, accepts the
licenses, installs `platform-tools`, `android-34` platform and build-tools,
and generates the Gradle wrapper (`gradlew`) for you. It takes a few
minutes the first time.

### 3. Reload your shell environment
```bash
source ~/.bashrc
```

### 4. Build the debug APK
```bash
./gradlew assembleDebug
```
The first build downloads Gradle dependencies and can take a few minutes.

Your installable APK will be at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### 5. Get the APK onto your phone
Pick whichever is easiest:
- **VS Code UI:** right-click `app-debug.apk` in the Explorer → **Download**.
- **gh CLI:** `gh codespace cp` from your local machine.
- **Direct install via ADB** if your phone is connected with USB debugging
  and forwarded (`adb install app/build/outputs/apk/debug/app-debug.apk`).

Then on your Android phone, allow "install from unknown sources" for the
file source you use, and tap the APK to install.

### 6. (Optional) Build a release APK
```bash
./gradlew assembleRelease
```
Note: an unsigned release APK won't install on most devices as-is — you'd
need to sign it with a keystore. For personal testing, the debug APK
(step 4) is the simplest path and installs fine as-is.

## How to play
- **On-screen arrows** or **swipe** on the maze area to move.
- Reach the **green dot** (bottom-right corner) to win.
- Move counter tracks efficiency; "Play Again" generates a fresh maze.

## Customizing
- Change maze size: edit `rows` / `cols` in `GameViewModel.kt`.
- Change colors: edit the `Color(0x...)` values in `GameScreen.kt`.
- Change controls: `Controls()` composable in `GameScreen.kt` handles the
  D-pad; swipe logic lives in `MazeCanvas()`.
