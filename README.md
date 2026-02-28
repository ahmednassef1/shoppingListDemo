# Shopping List Demo

An Android shopping list app built to demonstrate modern Android development tools. Items can be added, edited, categorized, and removed via swipe-to-delete. The list persists locally using Room.

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose, Material 3 |
| State management | ViewModel, StateFlow, Kotlin Flow |
| Dependency injection | Hilt |
| Local database | Room |
| Architecture | Clean Architecture (4 modules) |
| Language | Kotlin |

## Module Structure

```
shoppingListDemo/
├── app/       # UI layer — Compose screens, ViewModels, Hilt DI wiring
├── domain/    # Business logic — entities, use cases, repository interfaces
├── data/      # Data layer — Room database, DAOs, repository implementations, mappers
└── core/      # Shared infrastructure — base use case, error handling, Resource wrapper
```

The dependency rule is strictly enforced: `domain` has no Android dependencies, `data` depends on `domain`, and `app` wires everything together via Hilt modules.

## Requirements

- Android Studio Meerkat or later
- JDK 11
- Android SDK — `minSdk 24`, `targetSdk 36`
- Gradle 9.x (wrapper included, no local install needed)

## Build & Run

**1. Clone the repository**
```bash
git clone https://github.com/nassef/shoppingListDemo.git
cd shoppingListDemo
```

**2. Open in Android Studio**

`File → Open` and select the project root folder. Android Studio will detect the Gradle wrapper automatically.

**3. Sync Gradle**

Android Studio will prompt you to sync. Click **Sync Now**, or go to `File → Sync Project with Gradle Files`.

**4. Run the app**

Select a device (emulator or physical) and click **Run**

**From the command line (without Android Studio):**
```bash
# Debug build
./gradlew assembleDebug

# Install directly to connected device/emulator
./gradlew installDebug
```

The APK will be output to `app/build/outputs/apk/debug/`.
