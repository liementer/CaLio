package app.calio.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.calio.android.domain.entity.MealType
import app.calio.android.ui.components.*
import app.calio.android.viewmodel.CalorieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedAddEntryScreen(
    viewModel: CalorieViewModel,
    onNavigateBack: () -> Unit
) {
    var foodName by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var servingSize by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf(MealType.BREAKFAST) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Food Entry",
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val calorieValue = calories.toIntOrNull()
                    val proteinValue = protein.toFloatOrNull() ?: 0f
                    val carbsValue = carbs.toFloatOrNull() ?: 0f
                    val fatValue = fat.toFloatOrNull() ?: 0f
                    
                    if (foodName.isNotBlank() && calorieValue != null && calorieValue > 0) {
                        viewModel.addEntry(
                            name = foodName,
                            calories = calorieValue,
                            protein = proteinValue,
                            carbs = carbsValue,
                            fat = fatValue,
                            servingSize = servingSize,
                            mealType = selectedMealType
                        )
                        onNavigateBack()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
                .padding(bottom = 80.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Food Name
            Card(modifier = Modifier.fillMaxWidth()) {
                Input(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = "Food Name",
                    placeholder = "Grilled Chicken Breast",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Calories and Serving
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Nutrition Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Input(
                        value = calories,
                        onValueChange = { calories = it.filter { char -> char.isDigit() } },
                        label = "Calories",
                        placeholder = "350",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Input(
                        value = servingSize,
                        onValueChange = { servingSize = it },
                        label = "Serving",
                        placeholder = "100g",
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Macronutrients (grams)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Input(
                        value = protein,
                        onValueChange = { protein = it.filter { char -> char.isDigit() || char == '.' } },
                        label = "Protein",
                        placeholder = "30",
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Input(
                        value = carbs,
                        onValueChange = { carbs = it.filter { char -> char.isDigit() || char == '.' } },
                        label = "Carbs",
                        placeholder = "15",
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Input(
                        value = fat,
                        onValueChange = { fat = it.filter { char -> char.isDigit() || char == '.' } },
                        label = "Fat",
                        placeholder = "8",
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Meal Type
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Meal Type",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MealType.values().forEach { mealType ->
                        EnhancedMealTypeOption(
                            mealType = mealType,
                            isSelected = selectedMealType == mealType,
                            onSelect = { selectedMealType = mealType }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun EnhancedMealTypeOption(
    mealType: MealType,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val (icon, color) = when (mealType) {
        MealType.BREAKFAST -> Icons.Default.BakeryDining to androidx.compose.ui.graphics.Color(0xFFF59E0B)
        MealType.LUNCH -> Icons.Default.LunchDining to androidx.compose.ui.graphics.Color(0xFF10B981)
        MealType.DINNER -> Icons.Default.DinnerDining to androidx.compose.ui.graphics.Color(0xFF3B82F6)
        MealType.SNACK -> Icons.Default.Fastfood to androidx.compose.ui.graphics.Color(0xFF8B5CF6)
        MealType.OTHER -> Icons.Default.Restaurant to MaterialTheme.colorScheme.primary
    }
    
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.surface
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onSelect() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = mealType.name,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = mealType.name.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
        
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

