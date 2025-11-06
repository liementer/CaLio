package app.calio.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.calio.android.domain.entity.FoodEntry
import app.calio.android.domain.entity.MealType
import app.calio.android.ui.components.*
import app.calio.android.viewmodel.CalorieViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: CalorieViewModel,
    onNavigateBack: () -> Unit
) {
    val entries by viewModel.entries.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf<MealType?>(null) }
    
    val filteredEntries = entries.filter { entry ->
        val matchesSearch = entry.name.contains(searchQuery, ignoreCase = true)
        val matchesMealType = selectedMealType == null || entry.mealType == selectedMealType
        matchesSearch && matchesMealType
    }.sortedByDescending { it.date }
    
    val groupedEntries = filteredEntries.groupBy { entry ->
        val cal = Calendar.getInstance()
        cal.timeInMillis = entry.date
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.timeInMillis
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Food History",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Search and Filter Section
            Card(modifier = Modifier.fillMaxWidth()) {
                Input(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Search foods...",
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Meal Type Filter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        text = "All",
                        isSelected = selectedMealType == null,
                        onClick = { selectedMealType = null }
                    )
                    
                    MealType.values().forEach { type ->
                        FilterChip(
                            text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                            isSelected = selectedMealType == type,
                            onClick = { selectedMealType = if (selectedMealType == type) null else type }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stats Summary
            if (filteredEntries.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickStatCard(
                        label = "Total Entries",
                        value = "${filteredEntries.size}",
                        icon = Icons.Default.List,
                        modifier = Modifier.weight(1f)
                    )
                    QuickStatCard(
                        label = "Total Calories",
                        value = "${filteredEntries.sumOf { it.calories }}",
                        icon = Icons.Default.LocalFireDepartment,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Entries List
            if (filteredEntries.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (searchQuery.isNotEmpty() || selectedMealType != null)
                                "No matching entries found"
                            else
                                "No food entries yet",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty() || selectedMealType != null)
                                "Try adjusting your filters"
                            else
                                "Start logging your meals",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    groupedEntries.forEach { (date, dayEntries) ->
                        item {
                            val dayCalories = dayEntries.sumOf { it.calories }
                            val dayProtein = dayEntries.sumOf { it.protein.toInt() }
                            
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = formatDateHeader(date),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Badge(
                                        text = "$dayCalories kcal",
                                        variant = BadgeVariant.INFO
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                        
                        items(dayEntries) { entry ->
                            EnhancedFoodEntryCard(
                                entry = entry,
                                onDelete = { viewModel.deleteEntry(entry.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        text = text,
        variant = if (isSelected) ButtonVariant.DEFAULT else ButtonVariant.OUTLINE
    )
}

fun formatDateHeader(timestamp: Long): String {
    val cal = Calendar.getInstance()
    cal.timeInMillis = timestamp
    
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance()
    yesterday.add(Calendar.DAY_OF_YEAR, -1)
    
    return when {
        isSameDay(cal, today) -> "Today"
        isSameDay(cal, yesterday) -> "Yesterday"
        else -> SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault()).format(cal.time)
    }
}

fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

