package app.calio.android.domain.usecase

import app.calio.android.domain.entity.DailyStats
import app.calio.android.domain.repository.CalorieRepository

class GetDailyStatsUseCase(
    private val repository: CalorieRepository
) {
    suspend operator fun invoke(date: Long = System.currentTimeMillis()): DailyStats {
        return repository.getDailyStats(date)
    }
}

