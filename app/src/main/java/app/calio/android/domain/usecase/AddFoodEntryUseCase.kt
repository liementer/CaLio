package app.calio.android.domain.usecase

import app.calio.android.domain.entity.MealType
import app.calio.android.domain.repository.CalorieRepository

class AddFoodEntryUseCase(
    private val repository: CalorieRepository
) {
    suspend operator fun invoke(
        name: String,
        calories: Int,
        protein: Float,
        carbs: Float,
        fat: Float,
        servingSize: String,
        mealType: MealType
    ) {
        require(name.isNotBlank()) { "Food name cannot be empty" }
        require(calories > 0) { "Calories must be greater than 0" }
        require(protein >= 0) { "Protein cannot be negative" }
        require(carbs >= 0) { "Carbs cannot be negative" }
        require(fat >= 0) { "Fat cannot be negative" }
        
        repository.addEntry(name, calories, protein, carbs, fat, servingSize, mealType)
    }
}

