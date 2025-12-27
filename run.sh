#!/usr/bin/env bash
set -euo pipefail

# Run QuizMasterFX without Gradle by downloading a local JavaFX SDK,
# compiling sources, copying resources, and launching the app.

ROOT_DIR="$(pwd)"
JFX_VER="17.0.17"
JFX_BASE="$ROOT_DIR/.javafx"
# Prefer a system-installed JavaFX SDK if present
JFX_SYSTEM_DIR="C:/java/javafx-sdk-${JFX_VER}"
if [ -d "$JFX_SYSTEM_DIR/lib" ]; then
  JFX_DIR="$JFX_SYSTEM_DIR"
else
  JFX_DIR="$JFX_BASE/javafx-sdk-$JFX_VER"
fi
JFX_ZIP="$JFX_BASE/openjfx-${JFX_VER}_windows-x64_bin-sdk.zip"
JFX_URL="https://download2.gluonhq.com/openjfx/${JFX_VER}/openjfx-${JFX_VER}_windows-x64_bin-sdk.zip"
JFX_WIN_DIR="$JFX_BASE/win-jars"
GSON_DIR="$JFX_BASE/libs"
GSON_JAR="$GSON_DIR/gson-2.10.1.jar"
GSON_URL="https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar"

mkdir -p "$JFX_BASE"

if [ ! -d "$JFX_DIR/lib" ]; then
  echo "[1/4] Downloading JavaFX SDK $JFX_VER ..."
  if command -v curl >/dev/null 2>&1; then
    curl -L -o "$JFX_ZIP" "$JFX_URL"
  else
    powershell -NoProfile -Command "Invoke-WebRequest -Uri '$JFX_URL' -OutFile '$JFX_ZIP'"
  fi

  echo "[2/4] Extracting JavaFX SDK ..."
  if command -v unzip >/dev/null 2>&1; then
    unzip -q -d "$JFX_BASE" "$JFX_ZIP"
  else
    tar -xf "$JFX_ZIP" -C "$JFX_BASE" || powershell -NoProfile -Command "Expand-Archive -Path '$JFX_ZIP' -DestinationPath '$JFX_BASE' -Force"
  fi
fi

OUT_DIR="$ROOT_DIR/out"
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

echo "[3/4] Compiling sources ..."
find "src/main/java" -name "*.java" -print0 | xargs -0 -I{} printf '"%s"\n' "{}" > "$OUT_DIR/sources.txt"
mkdir -p "$GSON_DIR"
if [ ! -f "$GSON_JAR" ]; then
  echo "[~] Downloading Gson ..."
  if command -v curl >/dev/null 2>&1; then
    curl -L -o "$GSON_JAR" "$GSON_URL"
  else
    powershell -NoProfile -Command "Invoke-WebRequest -Uri '$GSON_URL' -OutFile '$GSON_JAR'"
  fi
fi

javac -cp "$GSON_JAR" -d "$OUT_DIR" --module-path "$JFX_DIR/lib" --add-modules javafx.controls,javafx.fxml @"$OUT_DIR/sources.txt"

echo "[3.5/4] Copying resources ..."
if [ -d "src/main/resources" ]; then
  cp -r "src/main/resources/." "$OUT_DIR/"
fi

echo "[4/4] Launching QuizMasterFX ..."
mkdir -p "$JFX_WIN_DIR"
# Fetch platform-specific JavaFX jars for Windows to ensure Glass classes are available
BASE_MVN="https://repo1.maven.org/maven2/org/openjfx"
curl -L -o "$JFX_WIN_DIR/javafx-base-$JFX_VER-win.jar"    "$BASE_MVN/javafx-base/$JFX_VER/javafx-base-$JFX_VER-win.jar"
curl -L -o "$JFX_WIN_DIR/javafx-graphics-$JFX_VER-win.jar" "$BASE_MVN/javafx-graphics/$JFX_VER/javafx-graphics-$JFX_VER-win.jar"
curl -L -o "$JFX_WIN_DIR/javafx-controls-$JFX_VER-win.jar" "$BASE_MVN/javafx-controls/$JFX_VER/javafx-controls-$JFX_VER-win.jar"
curl -L -o "$JFX_WIN_DIR/javafx-fxml-$JFX_VER-win.jar"     "$BASE_MVN/javafx-fxml/$JFX_VER/javafx-fxml-$JFX_VER-win.jar"

export PATH="$JFX_DIR/bin:$PATH"
MPATH="$JFX_DIR/lib;$JFX_WIN_DIR"
CP="$GSON_JAR;$OUT_DIR"
# Convert POSIX (/c/...) paths to Windows-style (C:/...) when running under MSYS/Cygwin
if command -v cygpath >/dev/null 2>&1; then
  WIN_MPATH="$(cygpath -w "$JFX_DIR/lib")"";""$(cygpath -w "$JFX_WIN_DIR")"
  WIN_CP="$(cygpath -w "$GSON_JAR")"";""$(cygpath -w "$OUT_DIR")"
else
  WIN_MPATH="$MPATH"
  WIN_CP="$CP"
fi
exec java \
  -Dprism.order=sw \
  -Dprism.verbose=true \
  -Dglass.platform=Win \
  -Djava.library.path="$JFX_DIR/bin" \
  --module-path "$WIN_MPATH" \
  --add-modules javafx.graphics,javafx.controls,javafx.fxml \
  -cp "$WIN_CP" com.quizmasterfx.Main
