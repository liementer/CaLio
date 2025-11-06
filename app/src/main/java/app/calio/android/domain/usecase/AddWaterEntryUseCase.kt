package app.calio.android.domain.usecase

import app.calio.android.domain.repository.CalorieRepository

class AddWaterEntryUseCase(
    private val repository: CalorieRepository
) {
    suspend operator fun invoke(amount: Int) {
        require(amount > 0) { "Water amount must be greater than 0" }
        repository.addWaterEntry(amount)
    }
}

