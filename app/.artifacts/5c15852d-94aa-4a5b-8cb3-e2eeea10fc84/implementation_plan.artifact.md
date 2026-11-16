# Implementation Plan - Optimize ProfileScreenViewModel Concurrency

The goal is to refactor `ProfileScreenViewModel.kt` to use structured concurrency with `suspend` functions. This will allow the `handleIntent` method to wait for data loading to complete before attempting to navigate, fixing the issue where account data was accessed before being loaded.

## User Review Required

> [!IMPORTANT]
> I will be converting `loadData` and all sub-loading methods (like `loadAccountData`) into `suspend` functions. This is a significant refactoring of how data is fetched in this ViewModel, but it ensures predictable execution order.

## Proposed Changes

### [app](file:///D:/studio_progects/billing/app)

#### [MODIFY] [ProfileScreenViewModel.kt](file:///D:/studio_progects/billing/app/src/main/java/com/impuls/billing/ui/profile_screen/ProfileScreenViewModel.kt)

- **Convert to `suspend`**:
    - `loadData`
    - `loadAccountData`
    - `loadVGroups`
    - `loadUsbBoxes`
    - `loadVgroupPassword`
    - `loadSharePosts`
- **Logic Updates**:
    - `loadData` will now use `dataStoreRepository.getBearer().firstOrNull()` to fetch the token once, instead of continuously collecting.
    - `handleIntent` will await `loadData` completion before proceeding to use the loaded account data for navigation.
- **Caller Updates**:
    - `onStart` and `onAction` will be updated to launch coroutines that call these new suspend functions.

## Verification Plan

### Automated Tests
- Build the project to ensure no syntax errors or unresolved references.

### Manual Verification
- Open the Profile screen and ensure all data (Account, Agreements, VGroups, etc.) loads correctly.
- Simulate an FCM intent (via `handleIntent`) and verify that it waits for account data to load before navigating to the Chat screen.
- Verify that token expiration (handled in `loadData`) still correctly navigates the user back to the login/main screen.
