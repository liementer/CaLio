package app.calio.android.domain.entity

data class FoodEntry(
    val id: String,
    val name: String,
    val calories: Int,
    val protein: Float = 0f, // in grams
    val carbs: Float = 0f, // in grams
    val fat: Float = 0f, // in grams
    val servingSize: String = "", // e.g., "1 cup", "100g"
    val date: Long, // Timestamp in milliseconds
    val mealType: MealType = MealType.OTHER
)

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
    OTHER
}

data class WaterEntry(
    val id: String,
    val amount: Int, // in ml
    val date: Long
)

