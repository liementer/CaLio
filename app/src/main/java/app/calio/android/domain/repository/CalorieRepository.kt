package app.calio.android.domain.repository

import app.calio.android.domain.entity.*
import kotlinx.coroutines.flow.Flow

interface CalorieRepository {
    
    // Food Entries
    fun getEntries(): Flow<List<FoodEntry>>
    suspend fun addEntry(
        name: String,
        calories: Int,
        protein: Float,
        carbs: Float,
        fat: Float,
        servingSize: String,
        mealType: MealType
    )
    suspend fun deleteEntry(entryId: String)
    
    // Water Entries
    fun getWaterEntries(): Flow<List<WaterEntry>>
    suspend fun addWaterEntry(amount: Int)
    
    // Settings
    fun getSettings(): Flow<UserSettings>
    suspend fun updateSettings(settings: UserSettings)
    
    // Stats
    suspend fun getDailyStats(date: Long): DailyStats
    suspend fun getWeeklyStats(): List<DailyStats>
    suspend fun getWeeklyProgress(): WeeklyProgress
}

