package app.calio.android.domain.usecase

import app.calio.android.domain.entity.UserSettings
import app.calio.android.domain.repository.CalorieRepository

class UpdateSettingsUseCase(
    private val repository: CalorieRepository
) {
    suspend operator fun invoke(settings: UserSettings) {
        require(settings.dailyCalorieTarget > 0) { "Daily calorie target must be greater than 0" }
        require(settings.dailyProteinTarget >= 0) { "Protein target cannot be negative" }
        require(settings.dailyCarbsTarget >= 0) { "Carbs target cannot be negative" }
        require(settings.dailyFatTarget >= 0) { "Fat target cannot be negative" }
        require(settings.dailyWaterTarget > 0) { "Water target must be greater than 0" }
        require(settings.userName.isNotBlank()) { "User name cannot be empty" }
        require(settings.weight > 0) { "Weight must be greater than 0" }
        require(settings.height > 0) { "Height must be greater than 0" }
        require(settings.age > 0) { "Age must be greater than 0" }
        
        repository.updateSettings(settings)
    }
}

