#!/usr/bin/env bash
set -euo pipefail

# Simple wrapper for the working javac/java command
ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

JFX="C:/java/javafx-sdk-17.0.17"
GSON=".javafx/libs/gson-2.10.1.jar"
SOURCES_TXT="out/sources.txt"

# Rebuild sources.txt if missing
if [ ! -f "$SOURCES_TXT" ]; then
  mkdir -p out
  find src/main/java -name '*.java' -print0 | xargs -0 -I{} printf '"%s"\n' "{}" > "$SOURCES_TXT"
fi

javac -cp "$GSON" -d out --module-path "$JFX/lib" --add-modules javafx.controls,javafx.fxml @"$SOURCES_TXT"
java \
  --module-path "$JFX/lib;./.javafx/win-jars" \
  --add-modules javafx.graphics,javafx.controls,javafx.fxml \
  -Djava.library.path="$JFX/bin" \
  -cp "$GSON;out" \
  com.quizmasterfx.Main
