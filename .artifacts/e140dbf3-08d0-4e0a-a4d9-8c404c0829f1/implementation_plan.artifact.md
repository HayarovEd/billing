# Implementation Plan - Push Services Audit and Fixes

Review and fix identified issues in HMS/FCM integration and Push notification handling.

## User Review Required

> [!IMPORTANT]
> I will be renaming some FCM-centric classes and methods to more generic names (e.g., `PushMessageData` instead of `FcmMessageData`) to maintain architectural clarity since we now support both FCM and HMS.

## Proposed Changes

### [Component] Chat Services

#### [MODIFY] [HmsMessageAndroidService.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/service/HmsMessageAndroidService.kt)
- Update `onNewToken` to save `PushProvider.HMS`.
- Ensure `onMessageReceived` correctly handles data vs notification messages.
- Clean up redundant `onNewToken` overloads.

#### [MODIFY] [FCMService.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/service/FCMService.kt)
- Update `onNewToken` to save `PushProvider.FCM`.

#### [MODIFY] [PushNotificationManager.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/service/PushNotificationManager.kt)
- Fix `handleNotification` to use parsed data (title/body) from the payload if explicit title/body are empty (crucial for Data Messages).
- Rename `FcmMessageData` usages to `PushMessageData` (after renaming the file).

#### [NEW] [PushMessageData.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/service/PushMessageData.kt)
#### [DELETE] [FcmMessageData.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/service/FcmMessageData.kt)
- Rename for provider-agnostic architecture.

#### [MODIFY] [ProfileScreenAction.kt](file:///D:/studio_progects/billing/app/src/main/java/com/impuls/billing/ui/profile_screen/ProfileScreenAction.kt)
#### [MODIFY] [ProfileScreenViewModel.kt](file:///D:/studio_progects/billing/app/src/main/java/com/impuls/billing/ui/profile_screen/ProfileScreenViewModel.kt)
- Update `FcmMessageData` references to `PushMessageData`.

### [Component] Push Data Layer

#### [MODIFY] [PushInitializer.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/data/repository/PushInitializer.kt)
- Fix syntax error (trailing comma).
- Replace `Thread` with `serviceScope.launch(Dispatchers.IO)`.
- Ensure `saveToken` is provider-aware if necessary, or rely on `init` setting the provider.

## Verification Plan

### Automated Tests
- N/A (Unit tests could be added for `PushNotificationManager` parsing logic).

### Manual Verification
- Verify build success.
- Verify that `PushNotificationManager` correctly fallback to data-payload titles/bodies via code inspection.
