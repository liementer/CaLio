package app.calio.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.calio.android.domain.entity.FoodEntry
import app.calio.android.domain.entity.MealType
import app.calio.android.ui.components.*
import app.calio.android.viewmodel.CalorieViewModel

@Composable
fun HomeScreen(
    viewModel: CalorieViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToProfile: () -> Unit,
    bottomNavPadding: PaddingValues = PaddingValues(0.dp)
) {
    val todayStats by viewModel.todayStats.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val weeklyProgress by viewModel.weeklyProgress.collectAsState()
    var showWaterDialog by remember { mutableStateOf(false) }
    
    if (showWaterDialog) {
        WaterIntakeDialog(
            onDismiss = { showWaterDialog = false },
            onAddWater = { amount ->
                viewModel.addWaterEntry(amount)
                showWaterDialog = false
            }
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottomNavPadding)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Hello, ${settings.userName}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = getCurrentGreeting(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                    
                    if (weeklyProgress.currentStreak > 0) {
                        Badge(
                            text = "${weeklyProgress.currentStreak} day streak",
                            variant = BadgeVariant.SUCCESS
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Quick Stats Row
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        StatCard(
                            icon = Icons.Default.LocalFireDepartment,
                            title = "Calories Today",
                            value = "${todayStats.totalCalories}",
                            subtitle = "${todayStats.target} goal",
                            color = androidx.compose.ui.graphics.Color(0xFFEF4444),
                            modifier = Modifier.width(160.dp)
                        )
                    }
                    item {
                        StatCard(
                            icon = Icons.Default.FitnessCenter,
                            title = "Protein",
                            value = "${todayStats.totalProtein.toInt()}g",
                            subtitle = "${settings.dailyProteinTarget.toInt()}g",
                            color = androidx.compose.ui.graphics.Color(0xFF3B82F6),
                            modifier = Modifier.width(140.dp)
                        )
                    }
                    item {
                        StatCard(
                            icon = Icons.Default.WaterDrop,
                            title = "Water",
                            value = "${todayStats.totalWater}ml",
                            subtitle = "${settings.dailyWaterTarget}ml",
                            color = androidx.compose.ui.graphics.Color(0xFF06B6D4),
                            modifier = Modifier.width(140.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
            }
            
            // Daily Progress Card
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    CardHeader(
                        title = "Today's Progress",
                        description = "Track your daily nutrition goals"
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgress(
                            current = todayStats.totalCalories,
                            target = todayStats.target,
                            modifier = Modifier.weight(1f),
                            size = 100
                        )
                        
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            QuickStatCard(
                                label = "Protein",
                                value = "${todayStats.totalProtein.toInt()}g",
                                icon = Icons.Default.FitnessCenter
                            )
                            QuickStatCard(
                                label = "Carbs",
                                value = "${todayStats.totalCarbs.toInt()}g",
                                icon = Icons.Default.BakeryDining
                            )
                            QuickStatCard(
                                label = "Fat",
                                value = "${todayStats.totalFat.toInt()}g",
                                icon = Icons.Default.EggAlt
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Water Intake Card
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    CardHeader(
                        title = "Water Intake",
                        description = "Stay hydrated throughout the day"
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    LinearProgressBar(
                        current = todayStats.totalWater.toFloat(),
                        target = settings.dailyWaterTarget.toFloat(),
                        label = "Water",
                        unit = "ml",
                        color = androidx.compose.ui.graphics.Color(0xFF3B82F6)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(250, 500, 750).forEach { amount ->
                            Button(
                                onClick = { viewModel.addWaterEntry(amount) },
                                text = "+${amount}ml",
                                variant = ButtonVariant.OUTLINE,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Macronutrient Breakdown
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    CardHeader(
                        title = "Macronutrients",
                        description = "Your daily macro breakdown"
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    MacroProgressRow(
                        protein = todayStats.totalProtein,
                        proteinTarget = settings.dailyProteinTarget,
                        carbs = todayStats.totalCarbs,
                        carbsTarget = settings.dailyCarbsTarget,
                        fat = todayStats.totalFat,
                        fatTarget = settings.dailyFatTarget
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Today's Meals Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's Meals",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    Button(
                        onClick = onNavigateToStats,
                        text = "View Stats",
                        variant = ButtonVariant.GHOST
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            if (todayStats.entries.isEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No meals logged yet",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tap the + button to add your first meal",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                    }
                }
            } else {
                items(todayStats.entries.sortedByDescending { it.date }) { entry ->
                    EnhancedFoodEntryCard(
                        entry = entry,
                        onDelete = { viewModel.deleteEntry(entry.id) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            
            item { 
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun EnhancedFoodEntryCard(
    entry: FoodEntry,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    MealTypeIcon(mealType = entry.mealType)
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = entry.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = entry.mealType.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            if (entry.servingSize.isNotEmpty()) {
                                Text(
                                    text = " • ${entry.servingSize}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${entry.calories} kcal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onDelete() }
                    )
                }
            }
            
            if (entry.protein > 0 || entry.carbs > 0 || entry.fat > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (entry.protein > 0) {
                        MacroBadge(label = "P", value = entry.protein, color = androidx.compose.ui.graphics.Color(0xFF3B82F6))
                    }
                    if (entry.carbs > 0) {
                        MacroBadge(label = "C", value = entry.carbs, color = androidx.compose.ui.graphics.Color(0xFF10B981))
                    }
                    if (entry.fat > 0) {
                        MacroBadge(label = "F", value = entry.fat, color = androidx.compose.ui.graphics.Color(0xFFF59E0B))
                    }
                }
            }
        }
    }
}

@Composable
fun MacroBadge(label: String, value: Float, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "${value.toInt()}g",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun WaterIntakeDialog(
    onDismiss: () -> Unit,
    onAddWater: (Int) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    
    CustomDialog(
        onDismiss = onDismiss,
        title = "Add Water",
        description = "Enter the amount of water you drank"
    ) {
        Input(
            value = amount,
            onValueChange = { amount = it.filter { char -> char.isDigit() } },
            placeholder = "Amount in ml",
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onDismiss,
                text = "Cancel",
                variant = ButtonVariant.OUTLINE,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    amount.toIntOrNull()?.let { onAddWater(it) }
                },
                text = "Add",
                modifier = Modifier.weight(1f),
                enabled = amount.toIntOrNull() != null && amount.toInt() > 0
            )
        }
    }
}

@Composable
fun MealTypeIcon(mealType: MealType) {
    val (icon, color) = when (mealType) {
        MealType.BREAKFAST -> Icons.Default.BakeryDining to androidx.compose.ui.graphics.Color(0xFFF59E0B)
        MealType.LUNCH -> Icons.Default.LunchDining to androidx.compose.ui.graphics.Color(0xFF10B981)
        MealType.DINNER -> Icons.Default.DinnerDining to androidx.compose.ui.graphics.Color(0xFF3B82F6)
        MealType.SNACK -> Icons.Default.Fastfood to androidx.compose.ui.graphics.Color(0xFF8B5CF6)
        MealType.OTHER -> Icons.Default.Restaurant to MaterialTheme.colorScheme.primary
    }
    
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = mealType.name,
            tint = color,
            modifier = Modifier.size(28.dp)
        )
    }
}

fun getCurrentGreeting(): String {
    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        else -> "Good evening"
    }
}

