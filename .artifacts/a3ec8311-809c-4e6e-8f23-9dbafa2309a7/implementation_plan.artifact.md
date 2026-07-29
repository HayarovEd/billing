# Implementation Plan - `download_install` Module Enhancements

Improve the reliability, safety, and modern Android integration of the `download_install` library module.

## Proposed Changes

### 1. Storage Safety & Cleanup
Add checks to ensure the device has enough space and clean up old artifacts.

#### [MODIFY] [DownloadRepositoryImpl.kt](file:///D:/studio_progects/billing/download_install/src/main/java/com/edurda77/download_install/downloader/DownloadRepositoryImpl.kt)
- Add `checkAvailableSpace(bytes: Long): Boolean` helper.
- In `downloadFile`, delete existing files in the download directory before starting.
- Implement progress throttling (e.g., only emit if percentage changed) to reduce UI overhead.

### 2. Installer & Permission Handling
Improve the installation flow by handling modern Android requirements.

#### [MODIFY] [Installer.kt](file:///D:/studio_progects/billing/download_install/src/main/java/com/edurda77/download_install/installer/Installer.kt) & [InstallerImpl.kt](file:///D:/studio_progects/billing/download_install/src/main/java/com/edurda77/download_install/installer/InstallerImpl.kt)
- Add `canRequestPackageInstalls(): Boolean` to the interface.
- Implement the check using `packageManager.canRequestPackageInstalls()` for API 26+.
- Update `installAPK` to log more descriptive errors or return a result.

### 3. Resilience & Configuration
Improve network reliability.

#### [MODIFY] [DownloadProvider.kt](file:///D:/studio_progects/billing/download_install/src/main/java/com/edurda77/download_install/DownloadProvider.kt)
- Provide a default `HttpClient` configuration with timeouts (Connect/Request/Socket) and retry capabilities if not provided by the app.

### 4. Code Quality & UX
Cleanup and consistency.

#### [MODIFY] [DownloadUpdateWorker.kt](file:///D:/studio_progects/billing/download_install/src/main/java/com/edurda77/download_install/worker/DownloadUpdateWorker.kt)
- Add a check for available space before starting the download.
- Ensure `Toast` doesn't crash if the worker is cancelled.

#### [MODIFY] [InstallerImpl.kt](file:///D:/studio_progects/billing/download_install/src/main/java/com/edurda77/download_install/installer/InstallerImpl.kt)
- Use a consistent `FileProvider` authority string.

## Verification Plan

### Automated Tests
- Run existing unit tests for the module.
- Add a unit test for the `checkAvailableSpace` logic.

### Manual Verification
- Test `in-app` update: observe smooth progress bar movement.
- Test `background` update: verify that old APKs are deleted from cache.
- Test on Android 10+: verify permission check works (simulated by revoking permission).
