package app.calio.android.domain.usecase

import app.calio.android.domain.entity.Gender
import app.calio.android.domain.entity.UserSettings

class CalculateBMRUseCase {
    operator fun invoke(settings: UserSettings): Int {
        // Mifflin-St Jeor Equation
        return when (settings.gender) {
            Gender.MALE -> (88.362 + (13.397 * settings.weight) + (4.799 * settings.height) - (5.677 * settings.age)).toInt()
            Gender.FEMALE -> (447.593 + (9.247 * settings.weight) + (3.098 * settings.height) - (4.330 * settings.age)).toInt()
            Gender.OTHER -> (88.362 + (13.397 * settings.weight) + (4.799 * settings.height) - (5.677 * settings.age)).toInt()
        }
    }
}

