# Public release preparation · 17 October 2026

Source reviewed: `606763f78672387a698de51feb4676d0bf261259`.
The release preparation changes documentation, licensing, screenshots, and ignore
rules only; no application code or dependency versions were changed.

## Checks performed

- `:app:assembleDebug :app:lintDebug`: successful with Android Studio JBR 25.
  Lint reported **no issues**.
- Installed the debug APK on an isolated, read-only Pixel 6 Pro emulator session,
  **Android 13 / API 33**, without clearing existing device/app data.
- Checked the initial fictional seed rows and captured the README screenshot.
- Added a synthetic student, movie, and rated Watching relationship through the UI.
- Relaunched the app and verified that the new joined row persisted.
- Deleted that Watching row, relaunched, and verified that deletion persisted.
- Verified that the synthetic student and movie remained selectable afterward.
- Gitleaks 8.30.1 scanned **all four reachable commits** and the release worktree:
  no secrets detected. The remote default branch matched the reviewed local HEAD;
  the GitHub repository had only the `master` branch at review time.
- Checked Markdown relative links and `git diff --check`.

The companion tutorial and project setup were checked against the current source.
These are release-preparation checks, not a claim of exhaustive testing.

## Deliberate limits

- This project currently has no configured automated application test suite.
- The UI smoke check used one API 33 emulator; this run did not qualify physical
  devices, every supported Android version, accessibility, or all input errors.
- Requery calls are synchronous and intentionally small for this lesson.
- No update/edit operation is implemented. Delete removes only a Watching link.
- The manual lesson twin shares the package and database; installing either app
  replaces the other. Use a dedicated emulator to avoid mixing tutorial states.
- Automated secret detection cannot prove that every historical byte is safe.

This report records the checks on the reviewed source; consult GitHub for
the current release commit and repository visibility.
