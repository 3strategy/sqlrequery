# sqlrequery project guidance

## Companion tutorial location

- The learning-site repository is BeautifulMivney.
- SQLite/Requery tutorial plans and future lesson pages belong at:
  - WSL: `/home/stra/repos/BeautifulMivney/android/sqlite`
  - Windows UNC: `\\wsl.localhost\Ubuntu\home\stra\repos\BeautifulMivney\android\sqlite`
- Do not recreate a `BeautifulMivney/` directory inside this Android project. When a request refers
  to the BeautifulMivney SQLite/Requery tutorial, read or edit the companion WSL location above.
- Follow `BeautifulMivney/AGENTS.md` when changing files in that repository.

## Role of this project

- This project is the compact, completed, runnable source of truth for the Android Java + Requery +
  SQLite “Class Netflix” demo.
- Keep the package, namespace, and application ID as `com.example.sqlrequery` unless the repository
  owner explicitly requests a change.
- Build and validate final tutorial code here before translating it into incremental lesson pages.

## Manual step-by-step twin

- The repository owner manually applies and verifies tutorial steps in:
  - Windows: `C:\Users\3stra\AndroidStudioProjects\sqlrequerySteps`
  - WSL mount: `/mnt/c/Users/3stra/AndroidStudioProjects/sqlrequerySteps`
- The twin deliberately uses the same `com.example.sqlrequery` package, namespace, and application
  ID. Installing one APK on a device/emulator replaces the other; confirm which project produced an
  APK before interpreting runtime state.
- Treat `sqlrequerySteps` as the student's current incremental state, not as a second final copy.
  Do not overwrite it with this completed project or introduce later-step code unless explicitly
  asked to advance or repair a particular tutorial step.
