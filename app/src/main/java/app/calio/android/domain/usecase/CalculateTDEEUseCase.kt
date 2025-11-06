package app.calio.android.domain.usecase

import app.calio.android.domain.entity.ActivityLevel
import app.calio.android.domain.entity.UserSettings

class CalculateTDEEUseCase(
    private val calculateBMRUseCase: CalculateBMRUseCase
) {
    operator fun invoke(settings: UserSettings): Int {
        val bmr = calculateBMRUseCase(settings)
        val multiplier = when (settings.activityLevel) {
            ActivityLevel.SEDENTARY -> 1.2
            ActivityLevel.LIGHT -> 1.375
            ActivityLevel.MODERATE -> 1.55
            ActivityLevel.ACTIVE -> 1.725
            ActivityLevel.VERY_ACTIVE -> 1.9
        }
        return (bmr * multiplier).toInt()
    }
}

