package app.calio.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.calio.android.domain.entity.*
import app.calio.android.domain.repository.CalorieRepository
import app.calio.android.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CalorieViewModel(
    private val repository: CalorieRepository,
    private val addFoodEntryUseCase: AddFoodEntryUseCase,
    private val deleteFoodEntryUseCase: DeleteFoodEntryUseCase,
    private val addWaterEntryUseCase: AddWaterEntryUseCase,
    private val getDailyStatsUseCase: GetDailyStatsUseCase,
    private val getWeeklyStatsUseCase: GetWeeklyStatsUseCase,
    private val getWeeklyProgressUseCase: GetWeeklyProgressUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val calculateBMIUseCase: CalculateBMIUseCase,
    private val calculateBMRUseCase: CalculateBMRUseCase,
    private val calculateTDEEUseCase: CalculateTDEEUseCase
) : ViewModel() {
    
    val entries: StateFlow<List<FoodEntry>> = repository.getEntries() as StateFlow<List<FoodEntry>>
    val waterEntries: StateFlow<List<WaterEntry>> = repository.getWaterEntries() as StateFlow<List<WaterEntry>>
    val settings: StateFlow<UserSettings> = repository.getSettings() as StateFlow<UserSettings>
    
    private val _todayStats = MutableStateFlow(
        DailyStats(
            System.currentTimeMillis(), 
            0, 
            0f, 
            0f, 
            0f, 
            0, 
            emptyList(), 
            emptyList(), 
            2000
        )
    )
    val todayStats: StateFlow<DailyStats> = _todayStats
    
    private val _weeklyStats = MutableStateFlow<List<DailyStats>>(emptyList())
    val weeklyStats: StateFlow<List<DailyStats>> = _weeklyStats
    
    private val _weeklyProgress = MutableStateFlow(WeeklyProgress(0, 0, 0, 0, 7))
    val weeklyProgress: StateFlow<WeeklyProgress> = _weeklyProgress
    
    init {
        observeData()
    }
    
    private fun observeData() {
        viewModelScope.launch {
            combine(entries, waterEntries, settings) { _, _, _ -> Unit }
                .collect {
                    updateStats()
                }
        }
    }
    
    private fun updateStats() {
        viewModelScope.launch {
            _todayStats.value = getDailyStatsUseCase()
            _weeklyStats.value = getWeeklyStatsUseCase()
            _weeklyProgress.value = getWeeklyProgressUseCase()
        }
    }
    
    fun addEntry(
        name: String, 
        calories: Int, 
        protein: Float, 
        carbs: Float, 
        fat: Float, 
        servingSize: String, 
        mealType: MealType
    ) {
        viewModelScope.launch {
            try {
                addFoodEntryUseCase(name, calories, protein, carbs, fat, servingSize, mealType)
            } catch (e: Exception) {
                // Handle validation errors
                e.printStackTrace()
            }
        }
    }
    
    fun addWaterEntry(amount: Int) {
        viewModelScope.launch {
            try {
                addWaterEntryUseCase(amount)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun deleteEntry(entryId: String) {
        viewModelScope.launch {
            try {
                deleteFoodEntryUseCase(entryId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun updateSettings(settings: UserSettings) {
        viewModelScope.launch {
            try {
                updateSettingsUseCase(settings)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun calculateBMI(): Float {
        return calculateBMIUseCase(settings.value)
    }
    
    fun calculateBMR(): Int {
        return calculateBMRUseCase(settings.value)
    }
    
    fun calculateTDEE(): Int {
        return calculateTDEEUseCase(settings.value)
    }
}
