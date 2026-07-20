# Walkthrough - ChatScreenViewModel Optimization

I have optimized the `ChatScreenViewModel` to improve concurrency, reduce redundant network calls, and fix potential WebSocket connection issues.

## Changes Made

### 1. Concurrency & Structured Concurrency
- **Suspend Helper**: Introduced `withAuth` suspend function to handle authentication and loading state in a centralized way.
- **Suspend Methods**: Converted all data-fetching and action-performing methods to `suspend` functions. This ensures they can be composed naturally and run within the `viewModelScope` without creating separate coroutine bubbles for each sub-step.
- **Removed Hardcoded Delays**: The `delay(500.milliseconds)` in `loadData` was removed in favor of proper initialization logic.

### 2. State Management
- **Initialization**: Refactored `onStart` to call a single `initializeData()` method. This ensures that the chat data is fetched first, and only then the WebSocket connection is established using the same authenticated context.
- **Single Source of Truth**: The `_state.update` blocks are now more consistent, and loading states are handled more predictably within `withAuth`.

### 3. WebSocket Lifecycle
- **Job Management**: Added `webSocketJob: Job?` to track the live chat connection.
- **Avoid Duplicates**: `startWebSocket` now cancels any existing connection before starting a new one, preventing duplicate message processing.
- **Cleanup**: Ensured `webSocketJob` is cancelled in `onCleared`.

### 4. Code Cleanup
- Improved logging and error reporting by using a consistent tag (`ChatScreenVM`).
- Simplified `onAction` by grouping logic and using a single `viewModelScope.launch` per user interaction.

## Verification Results

### Code Integrity
- All repository methods are now called correctly using structured concurrency.
- The `AuthUseCase` is utilized properly to ensure valid tokens are present for all operations.

> [!TIP]
> The optimization significantly reduces the number of coroutines created during typical usage, which should lead to better performance and fewer edge-case crashes related to initialization race conditions.
