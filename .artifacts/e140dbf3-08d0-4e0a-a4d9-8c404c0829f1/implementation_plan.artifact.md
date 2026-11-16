# Implementation Plan - Chat UI Scroll Optimization

Optimize the chat list scrolling behavior to prevent unexpected "jumps" and improve overall performance and responsiveness.

## User Review Required

> [!NOTE]
> I will be changing the autoscroll logic: now the list will only jump to the bottom if a new message is received AND the user is already near the bottom, OR if the user themselves sent a message. This is standard behavior for modern chat apps.

## Proposed Changes

### [Component] Chat UI Layer

#### [MODIFY] [ChatScreen.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/ui/chat_screen/ChatScreen.kt)
- **Use `derivedStateOf`**:
    - Replace `LaunchedEffect` for `isShowScrollArrow` with `derivedStateOf`.
    - Use `derivedStateOf` for `allMessagesFlat` and `firstUnreadSupportMsg` to optimize recompositions.
- **Smart Autoscroll**:
    - Add logic to check if the list is at the bottom before auto-scrolling to new messages.
    - Prevent automatic scrolling to the unread marker multiple times; it should only happen on initial load.
- **Scroll Improvements**:
    - Use `animateScrollToItem` for the "scroll to bottom" button for a smoother experience.
    - Fix the item count calculation in the scroll-to-bottom logic.

## Verification Plan

### Manual Verification
- Verify that entering a chat with unread messages correctly scrolls to the first unread message.
- Verify that receiving a new message while reading history does NOT cause the list to jump to the bottom.
- Verify that sending a message yourself DOES trigger a scroll to the bottom.
- Verify that the "scroll to bottom" arrow button appears correctly and scrolls smoothly.
