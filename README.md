# CaLio - Calorie Tracker App

A minimal Android app for tracking daily calorie intake with a beautiful shadcn-inspired UI.

## Features

- 📊 **Daily Calorie Tracking**: Log your meals and track calories consumed
- 📈 **Weekly Statistics**: View your progress over the last 7 days with custom bar charts
- 🎯 **Daily Goals**: Set and track your daily calorie targets
- 🍽️ **Meal Types**: Categorize entries by meal type (Breakfast, Lunch, Dinner, Snack, Other)
- 🎨 **Modern UI**: Shadcn-inspired neutral theme with custom components
- 💾 **Local Storage**: All data stored locally using SharedPreferences

## Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture:

### Data Layer
- **Models**: `FoodEntry`, `DailyStats`, `UserSettings`
- **Repository**: `CalorieRepository` - Manages data persistence using SharedPreferences

### ViewModel Layer
- **CalorieViewModel**: Handles business logic and exposes data to UI via StateFlow

### UI Layer
- **Custom Components** (shadcn-inspired):
  - `Card` - Container component with border and shadow
  - `Button` - Customizable button with variants (default, secondary, outline, ghost, destructive)
  - `Input` - Text input field with label support
  - `Dialog` - Custom dialog component
  - `BarChart` - Custom chart component for weekly statistics
  - `CircularProgress` - Circular progress indicator for daily goals

- **Screens**:
  - `HomeScreen` - Main dashboard with today's progress and meal list
  - `AddEntryScreen` - Add new food entries
  - `StatsScreen` - View weekly statistics and trends
  - `SettingsScreen` - Configure user settings and daily goals

### Theme
- Neutral color palette inspired by shadcn/ui
- Support for both light and dark modes
- Consistent spacing and typography

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM
- **State Management**: StateFlow
- **Data Persistence**: SharedPreferences
- **Dependencies**: 
  - AndroidX Core KTX
  - Lifecycle Runtime KTX
  - Lifecycle ViewModel Compose
  - Compose UI, Material3, Graphics
  - Activity Compose

## No External Dependencies

This app is built with **zero external dependencies** for core functionality:
- ✅ No Room database - using SharedPreferences
- ✅ No Hilt/Dagger - manual dependency injection
- ✅ No Navigation Component - custom navigation
- ✅ No chart libraries - custom canvas drawing
- ✅ Only standard Android libraries

## Project Structure

```
app/src/main/java/app/calio/android/
├── data/
│   ├── model/
│   │   ├── FoodEntry.kt
│   │   └── DailyStats.kt
│   └── repository/
│       └── CalorieRepository.kt
├── viewmodel/
│   └── CalorieViewModel.kt
├── ui/
│   ├── components/
│   │   ├── Card.kt
│   │   ├── Button.kt
│   │   ├── Input.kt
│   │   ├── Dialog.kt
│   │   └── BarChart.kt
│   ├── screens/
│   │   ├── HomeScreen.kt
│   │   ├── AddEntryScreen.kt
│   │   ├── StatsScreen.kt
│   │   └── SettingsScreen.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── MainActivity.kt
```

## Getting Started

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle
4. Run the app on an emulator or device (Min SDK: 24)

## Usage

1. **Add a Meal**: Tap the + button to add a new food entry with calories and meal type
2. **View Progress**: See your daily progress on the home screen with a circular progress indicator
3. **Check Stats**: Tap "View Stats" to see your weekly breakdown and trends
4. **Set Goals**: Configure your daily calorie target in the settings

## Design Philosophy

The app follows a minimal, clean design inspired by shadcn/ui:
- Neutral color palette for reduced eye strain
- Subtle borders and shadows for depth
- Clear hierarchy and spacing
- Focus on usability and clarity

## License

This project is open source and available for educational purposes.


