# Walkthrough - FCM Optimization and Fixes

I have optimized the `FCMNotificationHandler` and fixed the notification click behavior to ensure the app opens and navigates correctly.

## Changes Made

### 1. FCMNotificationHandler Optimization
- **Separated Parsing from Action**: Introduced a private `parseMap` method that handles the extraction of title, body, and chatId from various possible keys (`title`, `header`, `body`, `text`, `chatId`, `chat_id`).
- **New `handleIntent` Method**: Created specifically for UI components. It extracts data from the `Intent` but **does not** trigger a system notification.
- **Improved `handleNotification`**: Now used exclusively for background/foreground message reception, ensuring notifications are shown only when necessary.
- **Constants for Keys**: Replaced hardcoded string keys with private constants (`KEY_TITLE`, `KEY_CHAT_ID`, etc.) to prevent typos.

### 2. Fixed Notification Click Action
- **Reliable Launch Logic**: Updated `NotificationRepositoryImpl` to use `context.packageManager.getLaunchIntentForPackage`. This is the standard way to open the app from a background `Service`, replacing the unreliable attempt to obtain an `Activity` reference.
- **Data Transfer**: All FCM data (like `chatId`) is now added to the launch intent as extras.
- **Intent Flags**: Configured `FLAG_ACTIVITY_SINGLE_TOP` and `FLAG_ACTIVITY_CLEAR_TOP` to ensure the app opens smoothly without restarting if it's already running.

### 3. API Refinement & Cleanup
- **Decoupled Repositories**: Updated `NotificationRepository` to accept a `data: Map<String, String>?` instead of an `Activity?`, making the API cleaner and more suitable for background services.
- **Service Cleanup**: Removed the `getActivity` helper from `FCMService`, making it more robust.
- **Restored Notifications**: Re-enabled and updated the WebSocket notification logic in `ChatScreenViewModel`.

## Logic Flow

1.  **Message Received**: FCM or WebSocket detects a new message.
2.  **Notification Created**: `NotificationRepositoryImpl` creates a notification with a `PendingIntent`. This intent points to the app's main entry point and carries the message data.
3.  **User Clicks**: The user taps the notification.
4.  **App Opens**: Android launches the app using the `PendingIntent`.
5.  **Navigation**: `ProfileScreen` reads the `chatId` from the intent extras and navigates the user directly to the chat.

> [!TIP]
> This approach ensures that when a user clicks a notification, the app opens reliably and navigates to the correct chat, regardless of its previous state (foreground, background, or killed).
