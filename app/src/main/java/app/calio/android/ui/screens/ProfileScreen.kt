package app.calio.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.calio.android.domain.entity.*
import app.calio.android.ui.components.*
import app.calio.android.viewmodel.CalorieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: CalorieViewModel,
    onNavigateBack: () -> Unit,
    bottomNavPadding: PaddingValues = PaddingValues(0.dp)
) {
    val settings by viewModel.settings.collectAsState()
    
    var userName by remember(settings) { mutableStateOf(settings.userName) }
    var weight by remember(settings) { mutableStateOf(settings.weight.toString()) }
    var height by remember(settings) { mutableStateOf(settings.height.toString()) }
    var age by remember(settings) { mutableStateOf(settings.age.toString()) }
    var gender by remember(settings) { mutableStateOf(settings.gender) }
    var activityLevel by remember(settings) { mutableStateOf(settings.activityLevel) }
    var dailyTarget by remember(settings) { mutableStateOf(settings.dailyCalorieTarget.toString()) }
    var proteinTarget by remember(settings) { mutableStateOf(settings.dailyProteinTarget.toString()) }
    var carbsTarget by remember(settings) { mutableStateOf(settings.dailyCarbsTarget.toString()) }
    var fatTarget by remember(settings) { mutableStateOf(settings.dailyFatTarget.toString()) }
    var waterTarget by remember(settings) { mutableStateOf(settings.dailyWaterTarget.toString()) }
    
    val bmi = viewModel.calculateBMI()
    val bmr = viewModel.calculateBMR()
    val tdee = viewModel.calculateTDEE()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile & Settings",
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
                .padding(bottom = bottomNavPadding.calculateBottomPadding() + 16.dp)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // User Profile Section
            Card(modifier = Modifier.fillMaxWidth()) {
                CardHeader(
                    title = "Personal Information",
                    description = "Update your profile details"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Input(
                    value = userName,
                    onValueChange = { userName = it },
                    label = "Name",
                    placeholder = "Enter your name",
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Input(
                        value = age,
                        onValueChange = { age = it.filter { char -> char.isDigit() } },
                        label = "Age",
                        placeholder = "25",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Gender",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        GenderSelector(
                            selected = gender,
                            onSelect = { gender = it }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Body Metrics
            Card(modifier = Modifier.fillMaxWidth()) {
                CardHeader(
                    title = "Body Metrics",
                    description = "Track your physical measurements"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Input(
                        value = weight,
                        onValueChange = { weight = it.filter { char -> char.isDigit() || char == '.' } },
                        label = "Weight (kg)",
                        placeholder = "70",
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Input(
                        value = height,
                        onValueChange = { height = it.filter { char -> char.isDigit() || char == '.' } },
                        label = "Height (cm)",
                        placeholder = "170",
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // BMI Display
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "BMI (Body Mass Index)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
                        )
                        Text(
                            text = String.format("%.1f", bmi),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    
                    Badge(
                        text = getBMICategory(bmi),
                        variant = getBMIVariant(bmi)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Activity Level
            Card(modifier = Modifier.fillMaxWidth()) {
                CardHeader(
                    title = "Activity Level",
                    description = "How active are you on a typical day?"
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ActivityLevel.values().forEach { level ->
                        ActivityLevelOption(
                            level = level,
                            isSelected = activityLevel == level,
                            onSelect = { activityLevel = level }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Calorie Recommendations
            Card(modifier = Modifier.fillMaxWidth()) {
                CardHeader(
                    title = "Calorie Recommendations",
                    description = "Based on your profile and activity level"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$bmr",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "BMR",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "(at rest)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                    
                    HorizontalDivider(
                        modifier = Modifier
                            .width(1.dp)
                            .height(60.dp),
                        color = MaterialTheme.colorScheme.outline
                    )
                    
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$tdee",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color(0xFF10B981)
                        )
                        Text(
                            text = "TDEE",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "(daily need)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Button(
                    onClick = { dailyTarget = tdee.toString() },
                    text = "Use TDEE as Target",
                    variant = ButtonVariant.OUTLINE,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Daily Goals
            Card(modifier = Modifier.fillMaxWidth()) {
                CardHeader(
                    title = "Daily Goals",
                    description = "Set your nutrition targets"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Input(
                    value = dailyTarget,
                    onValueChange = { dailyTarget = it.filter { char -> char.isDigit() } },
                    label = "Calorie Target",
                    placeholder = "2000",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Input(
                        value = proteinTarget,
                        onValueChange = { proteinTarget = it.filter { char -> char.isDigit() || char == '.' } },
                        label = "Protein (g)",
                        placeholder = "150",
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Input(
                        value = carbsTarget,
                        onValueChange = { carbsTarget = it.filter { char -> char.isDigit() || char == '.' } },
                        label = "Carbs (g)",
                        placeholder = "250",
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Input(
                        value = fatTarget,
                        onValueChange = { fatTarget = it.filter { char -> char.isDigit() || char == '.' } },
                        label = "Fat (g)",
                        placeholder = "70",
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Input(
                    value = waterTarget,
                    onValueChange = { waterTarget = it.filter { char -> char.isDigit() } },
                    label = "Water Target (ml)",
                    placeholder = "2000",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    val newSettings = UserSettings(
                        dailyCalorieTarget = dailyTarget.toIntOrNull() ?: 2000,
                        dailyProteinTarget = proteinTarget.toFloatOrNull() ?: 150f,
                        dailyCarbsTarget = carbsTarget.toFloatOrNull() ?: 250f,
                        dailyFatTarget = fatTarget.toFloatOrNull() ?: 70f,
                        dailyWaterTarget = waterTarget.toIntOrNull() ?: 2000,
                        userName = userName,
                        weight = weight.toFloatOrNull() ?: 70f,
                        height = height.toFloatOrNull() ?: 170f,
                        age = age.toIntOrNull() ?: 25,
                        gender = gender,
                        activityLevel = activityLevel
                    )
                    viewModel.updateSettings(newSettings)
                    onNavigateBack()
                },
                text = "Save Changes",
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun GenderSelector(
    selected: Gender,
    onSelect: (Gender) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Gender.values().forEach { genderOption ->
            val label = when (genderOption) {
                Gender.MALE -> "M"
                Gender.FEMALE -> "F"
                Gender.OTHER -> "O"
            }
            
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (selected == genderOption) 
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else 
                            MaterialTheme.colorScheme.surface
                    )
                    .clickable { onSelect(genderOption) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (selected == genderOption)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun ActivityLevelOption(
    level: ActivityLevel,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val (label, description) = when (level) {
        ActivityLevel.SEDENTARY -> "Sedentary" to "Little or no exercise"
        ActivityLevel.LIGHT -> "Lightly Active" to "Exercise 1-3 days/week"
        ActivityLevel.MODERATE -> "Moderately Active" to "Exercise 3-5 days/week"
        ActivityLevel.ACTIVE -> "Very Active" to "Exercise 6-7 days/week"
        ActivityLevel.VERY_ACTIVE -> "Extra Active" to "Physical job or 2x training"
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) 
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                else 
                    MaterialTheme.colorScheme.surface
            )
            .clickable { onSelect() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        
        if (isSelected) {
            Text(
                text = "✓",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun getBMICategory(bmi: Float): String {
    return when {
        bmi < 18.5f -> "Underweight"
        bmi < 25f -> "Normal"
        bmi < 30f -> "Overweight"
        else -> "Obese"
    }
}

fun getBMIVariant(bmi: Float): BadgeVariant {
    return when {
        bmi < 18.5f -> BadgeVariant.WARNING
        bmi < 25f -> BadgeVariant.SUCCESS
        bmi < 30f -> BadgeVariant.WARNING
        else -> BadgeVariant.ERROR
    }
}

