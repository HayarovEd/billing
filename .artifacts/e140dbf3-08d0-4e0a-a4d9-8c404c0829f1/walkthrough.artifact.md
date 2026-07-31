# Walkthrough - Chat UI Scroll Optimization

I have optimized the scrolling behavior in the chat screen to make it more intuitive, performant, and consistent with modern messaging applications.

## Changes Made

### 1. Smart Autoscroll Logic
The chat list now handles new messages intelligently to avoid disrupting the user's reading experience:
- **Initial Load**: Upon opening a chat, the list scrolls to the first unread message from support or to the very bottom once.
- **New Messages**: If a new message arrives while the user is reading history (scrolled up), the list **will not** jump to the bottom. It only scrolls to the bottom if the user is already near the bottom (within 3 items) or if the user sends a message themselves.

### 2. Performance Optimizations
- **Derived State**: Replaced `LaunchedEffect` monitoring for the "scroll to bottom" arrow with `derivedStateOf`. This ensures that the arrow's visibility is re-evaluated only when the scroll state actually changes its "can scroll forward" status, reducing unnecessary recompositions.
- **Efficient Recalculations**: Refined `remember` blocks for flattened message lists and unread message detection to ensure they only re-run when relevant state changes.

### 3. Smooth Interactions
- **Animated Scroll**: The manual "scroll to bottom" button now uses `animateScrollToItem` for a smoother visual transition.
- **Correct Indexing**: Fixed the calculation for the total number of items (headers + messages) in the manual scroll logic.

## Verification Results

### Logic Audit
- **One-time Scroll**: Verified that `initialScrollDone` (using `rememberSaveable`) correctly prevents repeated scrolling to unread messages during a single session.
- **Near-Bottom Check**: The logic `lastVisibleItem >= totalItemsCount - 3` accurately identifies if the user is actively following the conversation.
- **Sender Check**: Confirmed that `isMyMessage` correctly triggers an immediate scroll for messages sent by the current user.
