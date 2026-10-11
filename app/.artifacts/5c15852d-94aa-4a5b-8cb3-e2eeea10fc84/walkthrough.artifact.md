# Walkthrough - ProfileScreenViewModel Concurrency Optimization

I have refactored the `ProfileScreenViewModel` to use structured concurrency. This ensures that data is loaded in a predictable order and fixes the issue where navigation from an FCM intent could occur before the necessary account data was available.

## Changes Made

### 1. Suspend Methods
- **`loadData`**: Converted to a `suspend` function. It now fetches the token once using `firstOrNull()` and handles expiration/navigation within the same coroutine scope.
- **Data Fetching**: Methods like `loadAccountData`, `loadVGroups`, and `loadUsbBoxes` are now `suspend` functions. They no longer launch their own internal `viewModelScope.launch` blocks, allowing callers to await their completion.

### 2. Fixed Intent Handling
- **`handleIntent`**: Now awaits the completion of `loadData` (and consequently `loadAccountData`) before attempting to navigate to the Chat screen. This ensures `state.account` is populated when creating the `NavigateChat` event.

### 3. Improved Parallelism
- **`loadVGroups`**: Uses `coroutineScope` and `async`/`await` to fetch USB boxes and VGroup passwords in parallel, but waits for both to finish before returning.

## Code Details

### [ProfileScreenViewModel.kt](file:///D:/studio_progects/billing/app/src/main/java/com/impuls/billing/ui/profile_screen/ProfileScreenViewModel.kt)

The `handleIntent` method now correctly sequences its operations:
```kotlin
fun handleIntent(fcmMessageData: FcmMessageData) {
    viewModelScope.launch {
        // Waits for data to load
        loadData(
            action = { loadAccountData(it) },
            navigate = { ... }
        )
        // Proceeds only after loadData is done
        fcmMessageData.chatId?.let { chatId ->
            _state.value.account?.let { acc ->
                eventChannel.send(ProfileScreenEvent.NavigateChat(...))
            }
        }
    }
}
```

## Verification Results

### Build Success
- The project compiles correctly with the new suspend-based architecture.

### Logic Verification
- Structured concurrency ensures that `state.account` is guaranteed to be present (if the fetch was successful) before the `NavigateChat` event is emitted.
- Token expiration handling remains intact, navigating the user to the starting screen if needed.
