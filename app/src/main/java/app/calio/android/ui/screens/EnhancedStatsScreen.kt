package app.calio.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.calio.android.ui.components.*
import app.calio.android.viewmodel.CalorieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedStatsScreen(
    viewModel: CalorieViewModel,
    onNavigateBack: () -> Unit
) {
    val weeklyStats by viewModel.weeklyStats.collectAsState()
    val todayStats by viewModel.todayStats.collectAsState()
    val weeklyProgress by viewModel.weeklyProgress.collectAsState()
    val settings by viewModel.settings.collectAsState()
    
    val averageCalories = if (weeklyStats.isNotEmpty()) {
        weeklyStats.sumOf { it.totalCalories } / weeklyStats.size
    } else {
        0
    }
    
    val averageProtein = if (weeklyStats.isNotEmpty()) {
        weeklyStats.sumOf { it.totalProtein.toInt() } / weeklyStats.size
    } else {
        0
    }
    
    val averageWater = if (weeklyStats.isNotEmpty()) {
        weeklyStats.sumOf { it.totalWater } / weeklyStats.size
    } else {
        0
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Statistics & Insights",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Weekly Overview",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Achievement Badges
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        icon = Icons.Default.Whatshot,
                        title = "Current Streak",
                        value = "${weeklyProgress.currentStreak}",
                        subtitle = "days",
                        color = androidx.compose.ui.graphics.Color(0xFFEF4444),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = Icons.Default.EmojiEvents,
                        title = "Best Streak",
                        value = "${weeklyProgress.longestStreak}",
                        subtitle = "days",
                        color = androidx.compose.ui.graphics.Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        icon = Icons.Default.TrackChanges,
                        title = "On Target",
                        value = "${weeklyProgress.daysOnTarget}/${weeklyProgress.totalDays}",
                        subtitle = "days",
                        color = androidx.compose.ui.graphics.Color(0xFF10B981),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = Icons.Default.ShowChart,
                        title = "Avg Calories",
                        value = "$averageCalories",
                        subtitle = "kcal/day",
                        color = androidx.compose.ui.graphics.Color(0xFF3B82F6),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Calorie Chart
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    CardHeader(
                        title = "Calorie Trend",
                        description = "Your daily calorie intake over the past week"
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    BarChart(
                        data = weeklyStats.map { stats ->
                            BarChartData(
                                label = getDayOfWeek(stats.date),
                                value = stats.totalCalories.toFloat(),
                                target = stats.target.toFloat(),
                                date = stats.date
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Average Macros
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    CardHeader(
                        title = "Average Macros",
                        description = "Your weekly average macronutrient intake"
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val avgProtein = if (weeklyStats.isNotEmpty()) {
                        weeklyStats.sumOf { it.totalProtein.toDouble() }.toFloat() / weeklyStats.size
                    } else {
                        0f
                    }
                    
                    val avgCarbs = if (weeklyStats.isNotEmpty()) {
                        weeklyStats.sumOf { it.totalCarbs.toDouble() }.toFloat() / weeklyStats.size
                    } else {
                        0f
                    }
                    
                    val avgFat = if (weeklyStats.isNotEmpty()) {
                        weeklyStats.sumOf { it.totalFat.toDouble() }.toFloat() / weeklyStats.size
                    } else {
                        0f
                    }
                    
                    MacroProgressRow(
                        protein = avgProtein,
                        proteinTarget = settings.dailyProteinTarget,
                        carbs = avgCarbs,
                        carbsTarget = settings.dailyCarbsTarget,
                        fat = avgFat,
                        fatTarget = settings.dailyFatTarget
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Water Intake
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    CardHeader(
                        title = "Hydration Stats",
                        description = "Your water intake patterns"
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${todayStats.totalWater}ml",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = androidx.compose.ui.graphics.Color(0xFF3B82F6)
                            )
                            Text(
                                text = "Today",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        
                        HorizontalDivider(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp),
                            color = MaterialTheme.colorScheme.outline
                        )
                        
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${averageWater}ml",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Weekly Avg",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Daily Breakdown
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    CardHeader(
                        title = "Daily Breakdown",
                        description = "Detailed view of each day this week"
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        weeklyStats.reversed().forEach { stats ->
                            DetailedDayBreakdownItem(
                                day = getDayOfWeek(stats.date),
                                date = formatDate(stats.date, "MMM dd"),
                                calories = stats.totalCalories,
                                target = stats.target,
                                protein = stats.totalProtein,
                                carbs = stats.totalCarbs,
                                fat = stats.totalFat,
                                water = stats.totalWater,
                                entries = stats.entries.size
                            )
                            
                            if (stats != weeklyStats.last()) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Insights
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    CardHeader(
                        title = "Insights & Tips",
                        description = "Personalized recommendations"
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (weeklyProgress.currentStreak >= 7) {
                            InsightItem(
                                icon = Icons.Default.CheckCircle,
                                text = "Excellent! You've logged for ${weeklyProgress.currentStreak} days straight!",
                                variant = BadgeVariant.SUCCESS
                            )
                        }
                        
                        if (averageCalories > settings.dailyCalorieTarget * 1.1) {
                            InsightItem(
                                icon = Icons.Default.Warning,
                                text = "Your average intake is above your target. Consider reducing portion sizes.",
                                variant = BadgeVariant.WARNING
                            )
                        } else if (averageCalories > 0 && averageCalories < settings.dailyCalorieTarget * 0.8) {
                            InsightItem(
                                icon = Icons.Default.Info,
                                text = "You're consistently under your goal. Make sure you're eating enough!",
                                variant = BadgeVariant.INFO
                            )
                        }
                        
                        if (averageWater < settings.dailyWaterTarget * 0.7) {
                            InsightItem(
                                icon = Icons.Default.WaterDrop,
                                text = "Try to drink more water. Aim for at least ${settings.dailyWaterTarget}ml per day.",
                                variant = BadgeVariant.INFO
                            )
                        }
                        
                        if (averageProtein < settings.dailyProteinTarget * 0.8) {
                            InsightItem(
                                icon = Icons.Default.FitnessCenter,
                                text = "Consider adding more protein to your meals for better muscle maintenance.",
                                variant = BadgeVariant.INFO
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun DetailedDayBreakdownItem(
    day: String,
    date: String,
    calories: Int,
    target: Int,
    protein: Float,
    carbs: Float,
    fat: Float,
    water: Int,
    entries: Int
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$calories kcal",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (calories <= target && calories > 0) {
                        androidx.compose.ui.graphics.Color(0xFF10B981)
                    } else if (calories > target) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    }
                )
                Text(
                    text = "$entries meal${if (entries != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (protein > 0) {
                MacroBadge(label = "P", value = protein, color = androidx.compose.ui.graphics.Color(0xFF3B82F6))
            }
            if (carbs > 0) {
                MacroBadge(label = "C", value = carbs, color = androidx.compose.ui.graphics.Color(0xFF10B981))
            }
            if (fat > 0) {
                MacroBadge(label = "F", value = fat, color = androidx.compose.ui.graphics.Color(0xFFF59E0B))
            }
            if (water > 0) {
                MacroBadge(label = "W", value = water.toFloat(), color = androidx.compose.ui.graphics.Color(0xFF3B82F6))
            }
        }
    }
}

@Composable
fun InsightItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    variant: BadgeVariant
) {
    val iconColor = when (variant) {
        BadgeVariant.SUCCESS -> androidx.compose.ui.graphics.Color(0xFF22C55E)
        BadgeVariant.WARNING -> androidx.compose.ui.graphics.Color(0xFFFFA500)
        BadgeVariant.ERROR -> MaterialTheme.colorScheme.error
        BadgeVariant.INFO -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                when (variant) {
                    BadgeVariant.SUCCESS -> androidx.compose.ui.graphics.Color(0xFF22C55E).copy(alpha = 0.05f)
                    BadgeVariant.WARNING -> androidx.compose.ui.graphics.Color(0xFFFFA500).copy(alpha = 0.05f)
                    BadgeVariant.ERROR -> MaterialTheme.colorScheme.error.copy(alpha = 0.05f)
                    BadgeVariant.INFO -> MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    else -> MaterialTheme.colorScheme.secondary
                },
                androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

