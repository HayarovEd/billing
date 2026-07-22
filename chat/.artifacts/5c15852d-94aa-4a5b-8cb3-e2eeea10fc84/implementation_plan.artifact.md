# Implementation Plan - FCMNotificationHandler Optimization

The current `FCMNotificationHandler` implementation has a logic flaw where it triggers a redundant notification when parsing an `Intent` (usually from a notification click). It also uses hardcoded strings and unsafe casts.

## User Review Required

> [!WARNING]
> I am changing the behavior of `handleNotification(intent)`. It will now **only extract data** and **not show** a new notification. This is because this method is typically called when the user has already clicked a notification, so showing a second one is redundant and confusing.

## Proposed Changes

### [chat](file:///D:/studio_progects/billing/chat)

#### [MODIFY] [FCMNotificationHandler.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/service/FCMNotificationHandler.kt)
- Extract all FCM keys into private constants to avoid typos and improve maintainability.
- Add a helper data class `ParsedFcmData` (internal) to carry title, body, and chatId.
- Refactor logic into a unified `parseMap(data: Map<String, String?>, defaultHeader: String)` method.
- **`handleNotification(data)`**: Uses `parseMap` and then calls `notificationRepository.showNotification`. Used in `FCMService` for incoming messages.
- **`handleIntent(intent)`** (formerly `handleNotification(intent)`): Uses `parseMap` but **does not** call `showNotification`. Used in UI components to react to notification clicks.

#### [MODIFY] [FCMService.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/service/FCMService.kt)
- Ensure it uses the explicit `handleNotification` method.

### [app](file:///D:/studio_progects/billing/app)

#### [MODIFY] [ProfileScreen.kt](file:///D:/studio_progects/billing/app/src/main/java/com/impuls/billing/ui/profile_screen/ProfileScreen.kt)
- Update to use the new `handleIntent` method name.

## Verification Plan

### Automated Tests
- N/A (FCM logic depends on Android system services and Koin).

### Manual Verification
1. **Incoming Message**: Send a message while the app is in foreground/background. Verify a notification appears.
2. **Notification Click**: Click the notification. Verify that `ProfileScreen` receives the `chatId` and navigates to the chat, **without** showing a second notification.
