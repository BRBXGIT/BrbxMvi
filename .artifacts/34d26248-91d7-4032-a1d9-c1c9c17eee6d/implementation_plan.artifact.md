# Implementation Plan - Local (Screen) and Shared Effects

The goal is to propagate the `ScreenEffect` functionality (currently present only in `MviViewModel`) to the entire MVI core logic, including scopes, containers, delegates, and helpers. This involves adding a new generic type parameter `ScreenEffect` to several core interfaces and classes and updating the associated flows and methods.

## Proposed Changes

### MVI Core API

#### [MODIFY] [MviScope.kt](file:///C:/PRG/KotlinProjects/BrbxMvi/mvi-core/api/src/main/java/com/brbx/mvicore/contracts/MviScope.kt)
- Add `ScreenEffect` type parameter to `MviScope`.
- Add `screenEffects: SharedFlow<ScreenEffect>` property.
- Add `postScreenEffect(effect: ScreenEffect)` method.
- Update `DefaultMviScope` and `mviScope` factory function to handle `ScreenEffect`.

#### [MODIFY] [MviContainer.kt](file:///C:/PRG/KotlinProjects/BrbxMvi/mvi-core/api/src/main/java/com/brbx/mvicore/contracts/MviContainer.kt)
- Add `ScreenEffect` type parameter to `MviContainer`.
- Update `scope` property type to `MviScope<State, Effect, ScreenEffect, Intent>`.

#### [MODIFY] [MviDelegate.kt](file:///C:/PRG/KotlinProjects/BrbxMvi/mvi-core/api/src/main/java/com/brbx/mvicore/contracts/MviDelegate.kt)
- Add `ScreenEffect` type parameter to `MviDelegate`.
- Update `scope` property type to `MviScope<State, Effect, ScreenEffect, Intent>`.

#### [MODIFY] [MviViewModel.kt](file:///C:/PRG/KotlinProjects/BrbxMvi/mvi-core/api/src/main/java/com/brbx/mvicore/base/MviViewModel.kt)
- Update `mviScope()` implementation to include `screenEffects` and `postScreenEffect` in the returned `MviScope`.

#### [MODIFY] [ContainedMviViewModel.kt](file:///C:/PRG/KotlinProjects/BrbxMvi/mvi-core/api/src/main/java/com/brbx/mvicore/base/ContainedMviViewModel.kt)
- Add `ScreenEffect` type parameter.
- Update inheritance: `MviViewModel<State, Effect, ScreenEffect, Intent>` and `MviContainer<State, Effect, ScreenEffect, Intent>`.
- Add `screenEffectReplay` parameter to constructor.

#### [MODIFY] [EffectHelpers.kt](file:///C:/PRG/KotlinProjects/BrbxMvi/mvi-core/api/src/main/java/com/brbx/mvicore/helpers/EffectHelpers.kt)
- Add `postScreenEffect` and `postScreenEffectIf` extension functions for `MviDelegate`.

---

### Tests

#### [NEW] [TestScreenEffect.kt](file:///C:/PRG/KotlinProjects/BrbxMvi/mvi-core/api/src/test/kotlin/com/brbx/mvicore/view_model/vm/TestScreenEffect.kt)
- Define a simple sealed interface for screen effects in tests.

#### [MODIFY] [TestViewModel.kt](file:///C:/PRG/KotlinProjects/BrbxMvi/mvi-core/api/src/test/kotlin/com/brbx/mvicore/view_model/vm/TestViewModel.kt)
- Update inheritance and constructor to include `TestScreenEffect`.

#### [MODIFY] [TestDelegateFactory.kt](file:///C:/PRG/KotlinProjects/BrbxMvi/mvi-core/api/src/test/kotlin/com/brbx/mvicore/view_model/delegate/TestDelegateFactory.kt)
- Update `DelegateMviScope` type alias and interface methods.

#### [MODIFY] [IntDelegate.kt](file:///C:/PRG/KotlinProjects/BrbxMvi/mvi-core/api/src/test/kotlin/com/brbx/mvicore/view_model/delegate/IntDelegate.kt) and [StringDelegate.kt](file:///C:/PRG/KotlinProjects/BrbxMvi/mvi-core/api/src/test/kotlin/com/brbx/mvicore/view_model/delegate/StringDelegate.kt)
- Update interface and implementation to handle `TestScreenEffect`.

#### [MODIFY] [ContainedMviViewModelTest.kt](file:///C:/PRG/KotlinProjects/BrbxMvi/mvi-core/api/src/test/kotlin/com/brbx/mvicore/view_model/ContainedMviViewModelTest.kt) and other test files
- Update all occurrences where `MviScope`, `MviDelegate`, or `MviViewModel` are instantiated or referenced.

## Verification Plan

### Automated Tests
- Run `:mvi-core:api:test` to ensure all MVI logic is correct and the new `ScreenEffect` is handled properly.
- Verify that both `Effect` (shared) and `ScreenEffect` (local) can be posted and observed.

### Manual Verification
- N/A (Core library logic).
