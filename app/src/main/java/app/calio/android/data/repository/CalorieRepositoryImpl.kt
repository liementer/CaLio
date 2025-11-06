package app.calio.android.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import app.calio.android.domain.entity.*
import app.calio.android.domain.repository.CalorieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import java.util.UUID

class CalorieRepositoryImpl(context: Context) : CalorieRepository {
    private val prefs: SharedPreferences = context.getSharedPreferences("calio_prefs", Context.MODE_PRIVATE)
    
    private val _entries = MutableStateFlow<List<FoodEntry>>(emptyList())
    private val _waterEntries = MutableStateFlow<List<WaterEntry>>(emptyList())
    private val _settings = MutableStateFlow(UserSettings())
    
    init {
        loadEntries()
        loadWaterEntries()
        loadSettings()
    }
    
    override fun getEntries(): Flow<List<FoodEntry>> = _entries
    
    override fun getWaterEntries(): Flow<List<WaterEntry>> = _waterEntries
    
    override fun getSettings(): Flow<UserSettings> = _settings
    
    override suspend fun addEntry(
        name: String,
        calories: Int,
        protein: Float,
        carbs: Float,
        fat: Float,
        servingSize: String,
        mealType: MealType
    ) {
        val entry = FoodEntry(
            id = UUID.randomUUID().toString(),
            name = name,
            calories = calories,
            protein = protein,
            carbs = carbs,
            fat = fat,
            servingSize = servingSize,
            date = System.currentTimeMillis(),
            mealType = mealType
        )
        val newList = _entries.value + entry
        _entries.value = newList
        saveEntries(newList)
    }
    
    override suspend fun deleteEntry(entryId: String) {
        val newList = _entries.value.filter { it.id != entryId }
        _entries.value = newList
        saveEntries(newList)
    }
    
    override suspend fun addWaterEntry(amount: Int) {
        val entry = WaterEntry(
            id = UUID.randomUUID().toString(),
            amount = amount,
            date = System.currentTimeMillis()
        )
        val newList = _waterEntries.value + entry
        _waterEntries.value = newList
        saveWaterEntries(newList)
    }
    
    override suspend fun updateSettings(settings: UserSettings) {
        _settings.value = settings
        prefs.edit {
            putInt("daily_target", settings.dailyCalorieTarget)
            putFloat("protein_target", settings.dailyProteinTarget)
            putFloat("carbs_target", settings.dailyCarbsTarget)
            putFloat("fat_target", settings.dailyFatTarget)
            putInt("water_target", settings.dailyWaterTarget)
            putString("user_name", settings.userName)
            putFloat("weight", settings.weight)
            putFloat("height", settings.height)
            putInt("age", settings.age)
            putString("gender", settings.gender.name)
            putString("activity_level", settings.activityLevel.name)
        }
    }
    
    override suspend fun getDailyStats(date: Long): DailyStats {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        val startOfDay = getStartOfDay(calendar)
        val endOfDay = getEndOfDay(calendar)
        
        val dailyEntries = _entries.value.filter { it.date in startOfDay..endOfDay }
        val dailyWater = _waterEntries.value.filter { it.date in startOfDay..endOfDay }
        
        val totalCalories = dailyEntries.sumOf { it.calories }
        val totalProtein = dailyEntries.sumOf { it.protein.toDouble() }.toFloat()
        val totalCarbs = dailyEntries.sumOf { it.carbs.toDouble() }.toFloat()
        val totalFat = dailyEntries.sumOf { it.fat.toDouble() }.toFloat()
        val totalWater = dailyWater.sumOf { it.amount }
        
        return DailyStats(
            date = date,
            totalCalories = totalCalories,
            totalProtein = totalProtein,
            totalCarbs = totalCarbs,
            totalFat = totalFat,
            totalWater = totalWater,
            entries = dailyEntries,
            waterEntries = dailyWater,
            target = _settings.value.dailyCalorieTarget
        )
    }
    
    override suspend fun getWeeklyStats(): List<DailyStats> {
        val stats = mutableListOf<DailyStats>()
        val calendar = Calendar.getInstance()
        
        for (i in 6 downTo 0) {
            val tempCal = calendar.clone() as Calendar
            tempCal.add(Calendar.DAY_OF_YEAR, -i)
            stats.add(getDailyStats(tempCal.timeInMillis))
        }
        
        return stats
    }
    
    override suspend fun getWeeklyProgress(): WeeklyProgress {
        val target = _settings.value.dailyCalorieTarget
        
        // Calculate streak
        var currentStreak = 0
        var longestStreak = 0
        var tempStreak = 0
        
        val calendar = Calendar.getInstance()
        for (i in 0 until 30) {
            val tempCal = calendar.clone() as Calendar
            tempCal.add(Calendar.DAY_OF_YEAR, -i)
            val stats = getDailyStats(tempCal.timeInMillis)
            
            if (stats.totalCalories in 1..target) {
                tempStreak++
                if (i < 7) currentStreak = tempStreak
            } else {
                longestStreak = maxOf(longestStreak, tempStreak)
                tempStreak = 0
            }
        }
        longestStreak = maxOf(longestStreak, tempStreak)
        
        val weeklyStats = getWeeklyStats()
        val weeklyAverage = if (weeklyStats.isNotEmpty()) {
            weeklyStats.sumOf { it.totalCalories } / weeklyStats.size
        } else {
            0
        }
        
        val daysOnTarget = weeklyStats.count { it.totalCalories in 1..target }
        
        return WeeklyProgress(
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            weeklyAverage = weeklyAverage,
            daysOnTarget = daysOnTarget,
            totalDays = weeklyStats.size
        )
    }
    
    private fun loadEntries() {
        val entriesJson = prefs.getString("entries", null)
        if (entriesJson != null) {
            _entries.value = parseEntries(entriesJson)
        }
    }
    
    private fun loadWaterEntries() {
        val waterJson = prefs.getString("water_entries", null)
        if (waterJson != null) {
            _waterEntries.value = parseWaterEntries(waterJson)
        }
    }
    
    private fun loadSettings() {
        _settings.value = UserSettings(
            dailyCalorieTarget = prefs.getInt("daily_target", 2000),
            dailyProteinTarget = prefs.getFloat("protein_target", 150f),
            dailyCarbsTarget = prefs.getFloat("carbs_target", 250f),
            dailyFatTarget = prefs.getFloat("fat_target", 70f),
            dailyWaterTarget = prefs.getInt("water_target", 2000),
            userName = prefs.getString("user_name", "User") ?: "User",
            weight = prefs.getFloat("weight", 70f),
            height = prefs.getFloat("height", 170f),
            age = prefs.getInt("age", 25),
            gender = Gender.valueOf(prefs.getString("gender", "OTHER") ?: "OTHER"),
            activityLevel = ActivityLevel.valueOf(prefs.getString("activity_level", "MODERATE") ?: "MODERATE")
        )
    }
    
    private fun saveEntries(entries: List<FoodEntry>) {
        val json = serializeEntries(entries)
        prefs.edit { putString("entries", json) }
    }
    
    private fun saveWaterEntries(entries: List<WaterEntry>) {
        val json = serializeWaterEntries(entries)
        prefs.edit { putString("water_entries", json) }
    }
    
    private fun serializeEntries(entries: List<FoodEntry>): String {
        return entries.joinToString("|||") { entry ->
            "${entry.id}::${entry.name}::${entry.calories}::${entry.protein}::${entry.carbs}::${entry.fat}::${entry.servingSize}::${entry.date}::${entry.mealType.name}"
        }
    }
    
    private fun parseEntries(json: String): List<FoodEntry> {
        if (json.isEmpty()) return emptyList()
        return json.split("|||").mapNotNull { entryStr ->
            try {
                val parts = entryStr.split("::")
                FoodEntry(
                    id = parts[0],
                    name = parts[1],
                    calories = parts[2].toInt(),
                    protein = parts.getOrNull(3)?.toFloatOrNull() ?: 0f,
                    carbs = parts.getOrNull(4)?.toFloatOrNull() ?: 0f,
                    fat = parts.getOrNull(5)?.toFloatOrNull() ?: 0f,
                    servingSize = parts.getOrNull(6) ?: "",
                    date = parts.getOrNull(7)?.toLongOrNull() ?: System.currentTimeMillis(),
                    mealType = MealType.valueOf(parts.getOrNull(8) ?: "OTHER")
                )
            } catch (e: Exception) {
                null
            }
        }
    }
    
    private fun serializeWaterEntries(entries: List<WaterEntry>): String {
        return entries.joinToString("|||") { entry ->
            "${entry.id}::${entry.amount}::${entry.date}"
        }
    }
    
    private fun parseWaterEntries(json: String): List<WaterEntry> {
        if (json.isEmpty()) return emptyList()
        return json.split("|||").mapNotNull { entryStr ->
            try {
                val parts = entryStr.split("::")
                WaterEntry(
                    id = parts[0],
                    amount = parts[1].toInt(),
                    date = parts[2].toLong()
                )
            } catch (e: Exception) {
                null
            }
        }
    }
    
    private fun getStartOfDay(calendar: Calendar): Long {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
    
    private fun getEndOfDay(calendar: Calendar): Long {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }
}

