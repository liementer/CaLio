package app.calio.android.domain.usecase

import app.calio.android.domain.entity.WeeklyProgress
import app.calio.android.domain.repository.CalorieRepository

class GetWeeklyProgressUseCase(
    private val repository: CalorieRepository
) {
    suspend operator fun invoke(): WeeklyProgress {
        return repository.getWeeklyProgress()
    }
}

