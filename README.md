# <img src="docs/logo.svg" width="40" height="40" valign="middle"/> BrbxMvi

**BrbxMvi** is a lightweight, scalable, and developer-friendly MVI (Model-View-Intent) framework for Android. It is designed to minimize boilerplate while providing a powerful DSL for state management and side effects, with a strong focus on **logic composition through delegates**.

---

## 🚀 Key Features

-   **Logic Delegation:** Break down massive ViewModels into small, testable, and reusable `MviDelegate` components.
-   **Reactive DSL:** Clean and expressive API for state updates (`reduce`), side effects (`postEffect`), and flow binding (`bind`).
-   **Transitive ViewModel Access:** Delegates automatically get access to the `viewModelScope` and MVI loop.
-   **DI Ready:** Seamless integration with Hilt, Dagger, or Koin.
-   **Lightweight:** No heavy dependencies, built purely on Kotlin Coroutines and Flow.

---

## 🏗 Core Architecture

In BrbxMvi, your ViewModel doesn't have to be a monolith. You can split business logic into **Delegates**.

### 1. The ViewModel (Container)
The ViewModel holds the state and acts as the entry point for intents.

```kotlin
class MyViewModel(
    private val authDelegate: AuthDelegate // Injected delegate
) : ContainedMviViewModel<MyState, MyEffect, MyIntent>(
    initialState = MyState()
) {
    override fun dispatchIntent(intent: MyIntent) {
        when (intent) {
            is MyIntent.Auth -> authDelegate.process(intent)
            // handle other intents
        }
    }
}
```

### 2. The Delegate (Logic Unit)
Delegates implement `MviDelegate`. They share the same `MviScope` as the ViewModel, meaning they can update state and post effects as if they were part of the ViewModel itself.

```kotlin
class AuthDelegate(
    override val scope: MviScope<MyState, MyEffect, MyIntent>
) : MviDelegate<MyState, MyEffect, MyIntent> {

    override fun process(intent: MyIntent) {
        if (intent is MyIntent.Auth.Login) {
            login(intent.user, intent.pass)
        }
    }

    private fun login(u: String, p: String) = launchAction {
        reduce { copy(isLoading = true) }
        val result = repository.login(u, p)
        reduce { copy(isLoading = false, user = result) }
        postEffect(MyEffect.ShowToast("Welcome!"))
    }
}
```

---

## 💉 Dependency Injection

### Hilt / Dagger
When using Hilt or Dagger, you typically use a factory or assisted injection to provide the `MviScope` to your delegates.

```kotlin
class MyViewModel @Inject constructor(
    delegateFactory: MyDelegate.Factory
) : ContainedMviViewModel<State, Effect, Intent>(...) {
    
    private val delegate = delegateFactory.create(scope) // 'scope' is provided by ContainedMviViewModel
}
```

### Koin
Koin makes it even easier. You can define your delegates and ViewModels in a module:

```kotlin
val myModule = module {
    factory { (scope: MviScope<S, E, I>) -> MyDelegate(scope) }
    
    viewModel { 
        val vm = MyViewModel()
        val delegate = get { parametersOf(vm.scope) }
        vm.init(delegate)
        vm
    }
}
```

---

## 🛠 Powerful Helpers (The DSL)

BrbxMvi provides a set of extension functions to make your code more readable.

### State Management
```kotlin
// Update state
reduce { copy(count = count + 1) }

// Conditional update (experimental)
reduceIf(condition) { copy(someFlag = true) }

// Typed reduction for sealed classes
reduceIfType<State.Success> { copy(data = newData) }
```

### Reactive Binding
Easily bind external `Flows` to your state:
```kotlin
// Automatically updates state whenever the flow emits
repository.observeData() bind { data -> 
    copy(items = data) 
}

// Bind only the latest emission
searchQueryFlow bindLatest { query ->
    copy(results = performSearch(query))
}
```

### Side Effects & Actions
```kotlin
// Post a one-time event (Effect)
postEffect(Effect.NavigateToProfile)

// Launch a coroutine in ViewModelScope
launchAction {
    // suspension point
}

// Async computation
val result = asyncAction { fetchData() }.await()
```

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
    implementation("com.github.BRBXGIT.BrbxMvi:brbx-mvi-api:latest_tag")
}
```