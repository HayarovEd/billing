# Implementation Plan - Unit Tests for AccountRepository

Generate unit tests for `AccountRepositoryImpl` to ensure the data layer correctly handles API responses, error states, and data mapping.

## User Review Required

> [!IMPORTANT]
> The project currently lacks some essential unit testing libraries for coroutines and Ktor. I propose adding the following dependencies:
> - `io.mockk:mockk`: For mocking dependencies.
> - `org.jetbrains.kotlinx:kotlinx-coroutines-test`: For testing suspend functions and coroutines.
> - `io.ktor:ktor-client-mock`: For mocking the `HttpClient` behavior.

> [!NOTE]
> `AccountRepositoryImpl` currently uses `Dispatchers.IO` directly. For better testability, I will refactor it to allow injecting a `CoroutineDispatcher`.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///D:/studio_progects/billing/gradle/libs.versions.toml)
- Add versions for `mockk`, `coroutines-test`, and `ktor-client-mock`.
- Add library definitions for these dependencies.

#### [MODIFY] [build.gradle.kts](file:///D:/studio_progects/billing/app/build.gradle.kts)
- Add the new test dependencies to the `app` module.

### Data Layer

#### [MODIFY] [AccountRepositoryImpl.kt](file:///D:/studio_progects/billing/app/src/main/java/com/impuls/billing/data/repository/AccountRepositoryImpl.kt)
- Update constructor to accept a `CoroutineDispatcher` (defaulting to `Dispatchers.IO`).
- Use the injected dispatcher instead of hardcoded `Dispatchers.IO`.

### Unit Tests

#### [NEW] [AccountRepositoryImplTest.kt](file:///D:/studio_progects/billing/app/src/test/java/com/impuls/billing/data/repository/AccountRepositoryImplTest.kt)
- Implement tests for key methods:
    - `getClientAccount`: Success (200 OK) and Error (401 Unauthorized, 400 Bad Request) cases.
    - `setClientMobilePhone`: Success and Failure cases.
    - `getClientInfo`: Data mapping verification.
    - `getClientSharedPosts`: Verification of query parameters and list mapping.
- Use `MockEngine` to simulate server responses.

## Verification Plan

### Automated Tests
- Run the newly created unit tests using `./gradlew :app:testDebugUnitTest`.
- Verify that all tests pass and cover both success and failure scenarios.
