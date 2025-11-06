package app.calio.android.domain.usecase

import app.calio.android.domain.entity.DailyStats
import app.calio.android.domain.repository.CalorieRepository

class GetWeeklyStatsUseCase(
    private val repository: CalorieRepository
) {
    suspend operator fun invoke(): List<DailyStats> {
        return repository.getWeeklyStats()
    }
}

