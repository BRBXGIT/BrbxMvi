# <img src="docs/logo.svg" width="50" height="50" valign="middle"/> BrbxMvi

**BrbxMvi** is a simple and straightforward MVI (Model-View-Intent) library for Android. It is designed to be convenient and easy to use, providing a small set of tools to manage state and side effects without excessive boilerplate. 

Its main focus is on **logic composition**, allowing you to split your business logic into small, reusable delegates that handle specific parts of your intent hierarchy.

[![](https://jitpack.io/v/BRBXGIT/BrbxMvi.svg)](https://jitpack.io/#BRBXGIT/BrbxMvi)

---

## 📦 Installation

This library is hosted on [JitPack](https://jitpack.io).

### 1. Add the JitPack repository
Add it to your `settings.gradle.kts` file:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. Add the dependency
Add the following to your module's `build.gradle.kts`:

```kotlin
dependencies {
    // Replace 'latest_tag' with the actual version (e.g., 1.0.0)
    implementation("com.github.BRBXGIT.BrbxMvi:brbx-mvi-api:latest_tag")
}
```

---

## Highlights

-   **Logic Delegation:** Easily split large ViewModels into small, testable `MviDelegate` components.
-   **Sealed Intent Hierarchy:** Use nested sealed interfaces to organize intents into logical groups.
-   **Simple DSL:** Concise API for state updates (`reduce`), side effects (`postEffect`), and flow binding (`bind`).
-   **ViewModel Integration:** Delegates automatically get access to the `viewModelScope` and the shared MVI loop.
-   **DI Friendly:** Designed to work seamlessly with Hilt, Dagger, or Koin using interface-based delegation.

---

## How it works

The core idea is to keep your ViewModel clean by moving business logic into **Delegates**. Each delegate handles a specific branch of your intent hierarchy.

### 1. The Intent Hierarchy
Organize your intents using nested sealed interfaces. This creates a "ladder" that allows delegates to handle only what they need.

```kotlin
sealed interface MainIntent {
    sealed interface Auth : MainIntent {
        data class Login(val user: String, val pass: String) : Auth
        data object Logout : Auth
    }
    // Other sub-intents...
}
```

### 2. The Delegate (Interface & Implementation)
Define your delegate as an interface. This makes it easy to mock for tests and inject via DI.

```kotlin
interface AuthDelegate : MviDelegate<MyState, MyEffect, MainIntent.Auth>

class AuthDelegateImpl(
    override val scope: MviScope<MyState, MyEffect, MainIntent>
) : AuthDelegate {

    override fun process(intent: MainIntent.Auth) {
        when (intent) {
            is MainIntent.Auth.Login -> login(intent.user, intent.pass)
            is MainIntent.Auth.Logout -> { /* handle logout */ }
        }
    }

    private fun login(u: String, p: String) = launchAction {
        reduce { copy(isLoading = true) }
        // ... business logic ...
        reduce { copy(isLoading = false) }
        postEffect(MyEffect.ShowToast("Success"))
    }
}
```

### 3. The ViewModel (Container)
The ViewModel acts as the container. It dispatches top-level intents to the appropriate delegates by checking their type.

```kotlin
class MyViewModel(
    authDelegateFactory: AuthDelegateFactory // Custom factory for the interface
) : ContainedMviViewModel<MyState, MyEffect, MainIntent>(
    initialState = MyState()
) {
    // Create the delegate implementation through the factory using the VM scope
    private val authDelegate = authDelegateFactory.create(scope)

    override fun dispatchIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.Auth -> authDelegate.process(intent)
            // handle other intent branches...
        }
    }
}
```

---

## Dependency Injection

### Hilt / Dagger
Use a factory or assisted injection to provide the `MviScope` to your delegate implementation.

```kotlin
class MyViewModel @Inject constructor(
    authDelegateFactory: AuthDelegateFactory
) : ContainedMviViewModel<State, Effect, MainIntent>(...) {
    
    private val authDelegate = authDelegateFactory.create(scope) 
}
```

### Koin
In Koin, you can provide the delegate as an interface and inject it lazily into your ViewModel by passing the scope as a parameter.

```kotlin
val myModule = module {
    factory<AuthDelegate> { (scope: MviScope<S, E, I>) -> AuthDelegateImpl(scope) }
    viewModel { MyViewModel() }
}

class MyViewModel : ContainedMviViewModel<State, Effect, MainIntent>(...), KoinComponent {
    // Lazily inject the delegate interface using the ViewModel's scope
    private val authDelegate: AuthDelegate by inject { parametersOf(scope) }

    override fun dispatchIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.Auth -> authDelegate.process(intent)
        }
    }
}
```

---

## Useful Helpers

BrbxMvi includes a few extensions to make common tasks a bit easier here is some of them:

### State Updates
```kotlin
// Basic update
reduce { copy(count = count + 1) }

// Type-safe reduction (useful for sealed state classes)
reduceIfType<State.Success> { copy(data = newData) }

// Access current state
val current = currentState
withState { state -> /* do something with state */ }
```

### Coroutine Actions
```kotlin
// Launch a coroutine in the ViewModel scope
launchAction {
    val data = repository.fetchData()
    reduce { copy(data = data) }
}

// Conditionally launch an action
launchActionIf(condition) {
    // ...
}

// Compute an asynchronous value
val deferred = asyncAction { repository.computeValue() }
val result = deferred.await()
```

### Flow Binding
```kotlin
// Automatically updates state for every flow emission
repository.observeData() bind { data -> 
    copy(items = data) 
}

// Only collects the latest emission, cancelling previous ones
searchQueryFlow bindLatest { query ->
    copy(results = performSearch(query))
}
```