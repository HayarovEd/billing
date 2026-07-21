# Implementation Plan - Implement FCMService

This plan outlines the steps to implement `FCMService` in the `chat` module to handle Firebase Cloud Messaging notifications for the chat system.

## Proposed Changes

### [chat module]

#### [MODIFY] [DataStoreChatRepository.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/domain/repository/DataStoreChatRepository.kt)
- Add `suspend fun saveFcmToken(token: String)`
- Add `suspend fun getFcmToken(): String?`

#### [MODIFY] [DataStoreRepositoryImpl.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/data/repository/DataStoreRepositoryImpl.kt)
- Implement `saveFcmToken` and `getFcmToken` using DataStore preferences.
- Define `FCM_TOKEN` key.

#### [MODIFY] [AndroidManifest.xml](file:///D:/studio_progects/billing/chat/src/main/AndroidManifest.xml)
- Register `FCMService` as a service with the `com.google.firebase.MESSAGING_EVENT` intent filter.

#### [MODIFY] [FCMService.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/service/FCMService.kt)
- Implement `onNewToken` to save the received FCM token using `DataStoreChatRepository`.
- Implement `onMessageReceived` to:
    - Extract notification data (title, body, chatId).
    - Use `NotificationRepository` to display a system notification.
- Use Koin's `KoinComponent` to inject dependencies.

## Verification Plan

### Automated Tests
- N/A (FCM requires device/emulator with Google Play Services).

### Manual Verification
1. **Token Retrieval**: Add a log statement in `onNewToken` and verify it prints a token when the app starts or data is cleared.
2. **Notification Display**: Send a data message via Firebase console (e.g., with keys `title`, `body`, and `chatId`) and verify the notification appears in the system tray.
