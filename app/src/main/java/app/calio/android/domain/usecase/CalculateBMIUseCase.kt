package app.calio.android.domain.usecase

import app.calio.android.domain.entity.UserSettings

class CalculateBMIUseCase {
    operator fun invoke(settings: UserSettings): Float {
        if (settings.height <= 0) return 0f
        
        val heightInMeters = settings.height / 100f
        return settings.weight / (heightInMeters * heightInMeters)
    }
}

