# App Heroes

## Overview

This app features an extensive list of hero characters, with the ability to bookmark favorites, view expanded details, and access information offline.
Initially connected to Marvel's Api but extensible to other Api (DC, Naruto, DB) due to its modularization.
The project follows the MVVM and Compose architecture pattern, ensuring organized and reusable code.

## Functionalities

- **Character Exploration**: A scrollable list of Marvel characters paginated.
- **Favorites**: Ability to favorite characters in the list and in the details view.
- **Offline Access**: Favorite characters are saved on the device for offline access.
- **Search**: Search bar to filter characters by name.
- **Detail View**: Detailed page for each character with larger image and description.
- **Error Handling**: Interfaces for empty lists, errors and internet outage scenarios.

## API

The app uses the Marvel API "Characters" endpoint to get character data. More information can be found in [official Marvel documentation](https://developer.marvel.com/docs).

## User Interface

The interface is divided into three main sections:

1. **Search - Characters**: List of characters with options to favorite them and a search bar.
2. **Character Details**: Detailed view with image sharing options.
3. **Favorites**: List of favorite characters and button to remove favorites.


## Project Execution

1. Clone the repository to your local machine.
2. Open the project in Android Studio or your preferred IDE.
3. Create your api key at https://developer.marvel.com/account
4. Save your private api with the name MARVEL_API_KEY in local.properties.
5. Save your public api with the name MARVEL_PUB_API_KEY in local.properties.
6. Synchronize the project with the Gradle files through Android Studio or use the terminal with `./gradlew build --refresh-dependencies` and then .`/gradlew assembleDebug`.
7. Run the application on an emulator or physical device.

## Documentation and Design Decisions

This app was build with:

- **[Jetpack Compose](https://developer.android.com/develop/ui/compose)** for layout creation.
- **[Room](https://developer.android.com/training/data-storage/room)** to save data.
- **[Hilt](https://developer.android.com/training/dependency-injection/hilt-android)** for dependency injection.
- **[Retrofit](https://square.github.io/retrofit/)** for http requests.
- **[Coroutines](https://developer.android.com/kotlin/coroutines)** for asynchronous services.
- **[Flow](https://developer.android.com/kotlin/flow)** and **[LiveData](https://developer.android.com/topic/libraries/architecture/livedata)** for state control.

Some considerations:

- There are 23 unit tests that can be easily run from Android Studio or from the root of the project by running `./gradlew :app:test`
- To make a request with flow in the repository use the function `requestByFlow { }` it will return a flow with [kotlin's `Result` class](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin /-result/)
- Every Activity in this project inherits `BaseActivity` for initialization.



