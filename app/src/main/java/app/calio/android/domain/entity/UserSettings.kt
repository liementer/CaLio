package app.calio.android.domain.entity

data class UserSettings(
    val dailyCalorieTarget: Int = 2000,
    val dailyProteinTarget: Float = 150f, // in grams
    val dailyCarbsTarget: Float = 250f, // in grams
    val dailyFatTarget: Float = 70f, // in grams
    val dailyWaterTarget: Int = 2000, // in ml
    val userName: String = "User",
    val weight: Float = 70f, // in kg
    val height: Float = 170f, // in cm
    val age: Int = 25,
    val gender: Gender = Gender.OTHER,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE
)

enum class Gender {
    MALE,
    FEMALE,
    OTHER
}

enum class ActivityLevel {
    SEDENTARY,
    LIGHT,
    MODERATE,
    ACTIVE,
    VERY_ACTIVE
}

