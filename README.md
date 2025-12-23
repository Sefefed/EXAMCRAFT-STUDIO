# QuizMasterFX

An interactive JavaFX quiz platform with Admin/Student portals, in-memory storage, smooth animations, and a polished UI to wow during demos.

## Features

- Admin Dashboard: create quizzes, view active quizzes, recent results.
- Student Portal: join with password, name/ID, take quiz.
- Quiz Taking: timer with visual alerts, progress, flagging, bubble navigation.
- Results: celebratory confetti, score breakdown, review option (stubbed), admin results table.
- In-memory data via `DataManager`; optional JSON save/load via Gson (stubbed).

## Requirements

- JDK 17 or 21 (recommended 17)
- Gradle (if you don't have it, install from gradle.org) or use IDE support
- Windows: This build uses JavaFX with `:win` classifier by default.
  - For macOS: change `:win` to `:mac` in `build.gradle`.
  - For Linux: change `:win` to `:linux` in `build.gradle`.

## Run (Windows)

```bash
# From the project root
gradle run
```

If Gradle is not on PATH, use your IDE to run the `Main` class or install Gradle.

## Project Layout

- `src/main/java/com/quizmasterfx` — Java sources
- `src/main/resources/view` — FXML files
- `src/main/resources/styles` — CSS
- `src/main/resources/images` — images/icons

## Demo Flow (5 minutes)

1. Admin creates a quiz (use sample data or create new) and shows active quizzes.
2. Student logs in with password (e.g., `JAVA2024`), takes quiz, uses timer and bubbles.
3. Submit → see results with confetti and score.
4. Admin views results list and stats (stubbed charts/exports).
5. Show smooth transitions, timer color change, resize responsiveness, theme toggle.

## Notes

- Persistence is in-memory; restarting the app resets state. JSON save/load utility is included as a stub.
- The UI is FXML-based with controllers and utility managers for animations and styles.
