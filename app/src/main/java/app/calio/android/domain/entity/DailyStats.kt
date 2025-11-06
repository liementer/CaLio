package app.calio.android.domain.entity

data class DailyStats(
    val date: Long,
    val totalCalories: Int,
    val totalProtein: Float,
    val totalCarbs: Float,
    val totalFat: Float,
    val totalWater: Int, // in ml
    val entries: List<FoodEntry>,
    val waterEntries: List<WaterEntry>,
    val target: Int
)

data class WeeklyProgress(
    val currentStreak: Int,
    val longestStreak: Int,
    val weeklyAverage: Int,
    val daysOnTarget: Int,
    val totalDays: Int
)

