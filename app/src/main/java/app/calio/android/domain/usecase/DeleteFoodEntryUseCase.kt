package app.calio.android.domain.usecase

import app.calio.android.domain.repository.CalorieRepository

class DeleteFoodEntryUseCase(
    private val repository: CalorieRepository
) {
    suspend operator fun invoke(entryId: String) {
        require(entryId.isNotBlank()) { "Entry ID cannot be empty" }
        repository.deleteEntry(entryId)
    }
}

