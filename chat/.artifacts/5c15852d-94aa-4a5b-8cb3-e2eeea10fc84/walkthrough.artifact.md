# Walkthrough - FCMService Implementation

I have implemented the `FCMService` to handle push notifications for the chat module. This includes saving the FCM token and displaying notifications when messages are received.

## Changes Made

### 1. Data Persistence
- **[DataStoreChatRepository](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/domain/repository/DataStoreChatRepository.kt)**: Added `saveFcmToken` and `getFcmToken` methods.
- **[DataStoreRepositoryImpl](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/data/repository/DataStoreRepositoryImpl.kt)**: Implemented these methods using Jetpack DataStore to persistently store the Firebase token.

### 2. Service Registration
- **[AndroidManifest.xml](file:///D:/studio_progects/billing/chat/src/main/AndroidManifest.xml)**: Registered `FCMService` in the `chat` module so the system can deliver FCM events to the app.

### 3. Service Logic
- **[FCMService](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/service/FCMService.kt)**:
    - **onNewToken**: Automatically saves the new FCM token to `DataStore` whenever it's updated by Google Play Services.
    - **onMessageReceived**: Handles incoming data and notification messages. It extracts the `title`, `body`, and optional `chatId` to show a system notification via the `NotificationRepository`.
    - **Koin Integration**: Uses `KoinComponent` to inject required repositories into the service.

## Verification Results

### Code Structure
- The service is correctly decoupled from the UI and uses the existing repository layer for notifications and data storage.
- It handles both `RemoteMessage.Notification` and custom `data` payloads for maximum flexibility.

> [!NOTE]
> To test this, you'll need to send a push message from the Firebase Console or your backend. Ensure the payload contains `title` and `body` (or use the corresponding keys in the `data` object).
