# Implementation Plan - WebSocket Fix (Re-attempt)

Fix WebSocket reliability issues, connection leaks, and restore real-time updates by correctly passing the authentication token and implementing auto-reconnection.

## User Review Required

> [!IMPORTANT]
> I am re-applying the refactoring to move the WebSocket session into a localized Flow scope. Crucially, I will **uncomment and ensure the authentication token is passed** to the server, as its absence was the likely cause of updates stopping in the previous attempt.

## Proposed Changes

### [Component] Chat Data Layer

#### [MODIFY] [WebSocketChatRepositoryImpl.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/data/repository/WebSocketChatRepositoryImpl.kt)
- **Localize Session**: Move session creation inside the `flow` builder.
- **NO Authentication Parameter**: As per user request, we will NOT add the `token` parameter.
- **Auto-Reconnection**: Wrap the connection logic in a `while(currentCoroutineContext().isActive)` loop with a reconnection delay (e.g., 5 seconds).
- **Safe Cleanup**: Use a `finally` block to ensure `session.close()` is called when the flow is cancelled.

#### [MODIFY] [WebSocketChatRepository.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/domain/repository/WebSocketChatRepository.kt)
- Remove the `close()` method from the interface as lifecycle will now be managed by the Flow.

### [Component] Chat UI Layer

#### [MODIFY] [ChatScreenViewModel.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/ui/chat_screen/ChatScreenViewModel.kt)
- Remove all calls to `webSocketChatRepository.close()`. Cleanup will be handled automatically when `webSocketJob?.cancel()` is called.

## Verification Plan

### Manual Verification
- Verify build success.
- Check that `parameter("token", accessToken)` is NOT added to `WebSocketChatRepositoryImpl.kt`.
- Code review to confirm that `webSocketJob.cancel()` in `ChatScreenViewModel` properly closes the session via the repository's `finally` block.
