<!-- back to top -->
<a name="readme-top"></a>

[![overview](https://img.shields.io/badge/BookTracker-overview-green.svg)](/README.md)

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/PrzemekTypa/BookTracker-mobile">
    <img src="app\src\main\res\drawable\book.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">BookTracker</h3>

  <p align="center">
    BookTracker-mobile is a Kotlin Android app that allows users to catalog books, rate them, and track their reading progress.
    <br />
    <br />
  </p>
</div>

## Features

- Add books manually or via ISBN
- Track reading status: want to read, reading, read
- Rate and review books
- Reading challenge mode
- Offline progress tracking

## Technology Stack

- Kotlin
- Jetpack Compose
- MVVM architecture
- Hilt (Dependency Injection)
- Retrofit (network)
- StateFlow / ViewModel
- Navigation Compose
- Coil (image loading)

## Project Structure
```
com.example.booktrackermobile
│
├── model
│   ├── Book.kt
│   ├── BooksResponse.kt
│   ├── DescriptionDeserializer.kt
│   ├── WorkDetails.kt
│
├── navigation
│   └── MainNavGraph.kt
│
├── network
│   ├── ApiService.kt
│   └── RetrofitInstance.kt
│
├── screens
│   ├── AllBooksTab.kt
│   ├── BookDetailsScreen.kt
│   ├── BookItem.kt
│   ├── LoginScreen.kt
│   ├── MainScreen.kt
│   ├── RegisterScreen.kt
│   ├── ResetPasswordScreen.kt
│   └── SettingTab.kt
│
├── storage
│   └── BookStorage.kt
│
├── ui.theme
│
└── MainActivity.kt
```




## Running the Project

1. Clone the repository:
```
    git clone https://github.com/yourusername/BookTracker-mobile.git
```
2. Open the project in Android Studio.

3. Requirements:
- Android SDK 33+
- Kotlin 1.9+
- Gradle 8+

4. Run the project on an emulator or connected device.

[↑ Back to top](#readme-top)


