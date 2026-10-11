# Walkthrough - Unit Tests for AccountRepository

I have implemented unit tests for `AccountRepositoryImpl` and improved its testability by refactoring dispatcher injection.

## Changes Made

### Build System
- Updated [libs.versions.toml](file:///D:/studio_progects/billing/gradle/libs.versions.toml) with new test dependencies:
    - `mockk`: for mocking (though primarily `ktor-client-mock` was used for the network layer).
    - `kotlinx-coroutines-test`: for `runTest` and `UnconfinedTestDispatcher`.
    - `ktor-client-mock`: for mocking the `HttpClient` engine.
- Added these dependencies to [app/build.gradle.kts](file:///D:/studio_progects/billing/app/build.gradle.kts).

### Data Layer
- Refactored [AccountRepositoryImpl.kt](file:///D:/studio_progects/billing/app/src/main/java/com/impuls/billing/data/repository/AccountRepositoryImpl.kt) to accept a `CoroutineDispatcher` in its constructor. This allows injecting `UnconfinedTestDispatcher` during tests, ensuring that coroutines execute predictably on the test thread.

### Unit Tests
- Created [AccountRepositoryImplTest.kt](file:///D:/studio_progects/billing/app/src/test/java/com/impuls/billing/data/repository/AccountRepositoryImplTest.kt) with the following tests:
    - **`getClientAccount returns Success when API returns 200`**: Verifies successful data retrieval and mapping.
    - **`getClientAccount returns Unauthorized error when API returns 401`**: Verifies error handling for authentication issues.
    - **`setClientMobilePhone returns true when API returns success`**: Verifies POST request handling and simple boolean mapping.
    - **`getClientInfo maps data correctly`**: Verifies detailed mapping of `ClientInfo` fields.
    - **`getClientSharedPosts returns list and sends date parameter`**: Verifies query parameter formatting (specifically `YYYY-MM-DD` for `date_from`) and list mapping.

## Verification Results

### Automated Tests
Ran `./gradlew :app:testDebugUnitTest` and all tests passed:
```
BUILD SUCCESSFUL in 10s
6 tests completed, 0 failed
```

> [!TIP]
> You can run these tests anytime from the IDE by right-clicking `AccountRepositoryImplTest.kt` or using the Gradle task.
