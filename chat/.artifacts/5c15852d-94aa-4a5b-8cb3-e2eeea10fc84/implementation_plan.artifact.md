# Implementation Plan - Fix Notification Click Action

The application does not open on notification click because `FCMService` is trying to pass an `Activity` object to the notification builder, but a background service cannot reliably obtain an `Activity` reference. We will revert to using `Intent` extras and `getLaunchIntentForPackage`.

## User Review Required

> [!IMPORTANT]
> I will be removing the `Activity` parameter from `NotificationRepository.showNotification` as it is not the correct way to handle notifications from a `Service`. Instead, I will use the app's launch intent.

## Proposed Changes

### [chat module]

#### [MODIFY] [NotificationRepository.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/domain/repository/NotificationRepository.kt)
- Remove `activity: Activity?` parameter.
- Add `data: Map<String, String>? = null` parameter to pass FCM payload for routing.

#### [MODIFY] [NotificationRepositoryImpl.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/data/repository/NotificationRepositoryImpl.kt)
- Update `showNotification` implementation:
    - Use `context.packageManager.getLaunchIntentForPackage(context.packageName)` to get the main entry point of the app.
    - Loop through the `data` map and add each entry as an extra to the intent.
    - Set up a `PendingIntent` with the correct flags for the notification.

#### [MODIFY] [FCMNotificationHandler.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/service/FCMNotificationHandler.kt)
- Update `handleNotification` signature and call to `notificationRepository.showNotification`.

#### [MODIFY] [FCMService.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/service/FCMService.kt)
- Remove the `getActivity` helper.
- Update `onMessageReceived` to pass `message.data` to the handler.

#### [MODIFY] [ChatScreenViewModel.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/ui/chat_screen/ChatScreenViewModel.kt)
- Update the notification logic to match the new `NotificationRepository` signature.

## Verification Plan

### Manual Verification
1. **Trigger Notification**: Send a push message (or trigger via WebSocket).
2. **Verify Click**: Click the notification. The app should open.
3. **Verify Routing**: Ensure that the `chatId` is passed correctly and the app navigates to the chat screen (handled by `ProfileScreen.kt`).
