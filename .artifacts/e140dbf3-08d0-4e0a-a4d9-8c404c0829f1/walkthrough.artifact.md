# Walkthrough - Push Services Audit and Fixes

I have audited and fixed the HMS/FCM integration. The changes focus on architectural consistency, bug fixes for Data Messages, and ensuring provider-token synchronization.

## Changes Made

### 1. Provider-Agnostic Refactoring
- Renamed `FcmMessageData` to `PushMessageData`.
- Updated `DataStoreChatRepository` methods to use generic naming:
    - `saveFcmToken` -> `savePushToken`
    - `isSentFcmToken` -> `isSentPushToken`
    - etc.
- Renamed internal request class `FcmTokenRequest` to `PushTokenRequest`.

### 2. Critical Fix: Push Notification Fallback
Modified `PushNotificationManager.kt` to handle **Data Messages** correctly. If the system notification `title` or `body` is empty (common for data-only payloads), it now falls back to the values parsed from the data map.

```kotlin
// In PushNotificationManager.kt
val finalTitle = title.ifEmpty { parsedData.title }
val finalBody = body.ifEmpty { parsedData.body }
```

### 3. Token & Provider Synchronization
Updated `onNewToken` in both `FCMService` and `HmsMessageAndroidService` to explicitly update the current `PushProvider` in the DataStore. This ensures that the backend always knows which provider the current token belongs to.

### 4. Lifecycle and Resource Management
- Added `onDestroy` with `serviceScope.cancel()` to `FCMService` and `HmsMessageAndroidService` to prevent potential coroutine leaks.
- Simplified `onNewToken` overloads in `HmsMessageAndroidService`.

### 5. Push Initializer Fixes
- Fixed a syntax error (trailing comma).
- Replaced the manual `Thread` usage with a `CoroutineScope` using `Dispatchers.IO` for better consistency with the project's architecture.

## Verification Results

### Manual Verification
- **Code Audit**: Verified that all `Fcm`-prefixed methods in the data layer were replaced with `Push`-prefixed ones.
- **Logic Check**: Confirmed that `handleNotification` fallback logic correctly handles empty title/body strings.
- **Build**: All class renames and interface updates have been applied consistently across the `:chat` and `:app` modules.
