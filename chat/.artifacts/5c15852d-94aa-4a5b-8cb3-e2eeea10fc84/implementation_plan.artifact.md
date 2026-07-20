# Implementation Plan - Chat Bubble with Outward Tail

The goal is to modify the `MessageItem` background to have a tail sticking out of the bottom-right corner, making it look like a classic chat bubble.

## Proposed Changes

### [chat](file:///D:/studio_progects/billing/chat)

#### [MODIFY] [MessageItem.kt](file:///D:/studio_progects/billing/chat/src/main/java/com/impuls/chat/ui/chat_screen/MessageItem.kt)
- Add horizontal padding to the root `Column` to ensure the "outward" tail doesn't hit the screen edge.
- Replace the simple `drawRoundRect` with a `drawPath` call.
- Implement a `Path` that:
    - Draws a rounded rectangle for most of the bubble.
    - Adds an "outward" tail at the bottom-right corner for user messages (`!message.isSupport`).
    - (Optional/Consistent) Adds an "outward" tail at the bottom-left corner for support messages (`message.isSupport`). *The user specifically mentioned the right corner, so I will prioritize that.*

## Verification Plan

### Manual Verification
- Render the `MessageItem` in a preview or run the app to verify the tail shape and alignment.
- Ensure the tail does not overlap with message text or status indicators.
- Ensure the tail is not clipped by the screen edges.
