package app.calio.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.calio.android.data.repository.CalorieRepositoryImpl
import app.calio.android.domain.repository.CalorieRepository
import app.calio.android.domain.usecase.*
import app.calio.android.ui.components.BottomNavBar
import app.calio.android.ui.components.NavDestination
import app.calio.android.ui.screens.*
import app.calio.android.ui.theme.CaLioTheme
import app.calio.android.viewmodel.CalorieViewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: CalorieViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize repository
        val repository: CalorieRepository = CalorieRepositoryImpl(applicationContext)
        
        // Initialize use cases
        val addFoodEntryUseCase = AddFoodEntryUseCase(repository)
        val deleteFoodEntryUseCase = DeleteFoodEntryUseCase(repository)
        val addWaterEntryUseCase = AddWaterEntryUseCase(repository)
        val getDailyStatsUseCase = GetDailyStatsUseCase(repository)
        val getWeeklyStatsUseCase = GetWeeklyStatsUseCase(repository)
        val getWeeklyProgressUseCase = GetWeeklyProgressUseCase(repository)
        val updateSettingsUseCase = UpdateSettingsUseCase(repository)
        val calculateBMIUseCase = CalculateBMIUseCase()
        val calculateBMRUseCase = CalculateBMRUseCase()
        val calculateTDEEUseCase = CalculateTDEEUseCase(calculateBMRUseCase)
        
        // Initialize ViewModel with use cases
        viewModel = CalorieViewModel(
            repository = repository,
            addFoodEntryUseCase = addFoodEntryUseCase,
            deleteFoodEntryUseCase = deleteFoodEntryUseCase,
            addWaterEntryUseCase = addWaterEntryUseCase,
            getDailyStatsUseCase = getDailyStatsUseCase,
            getWeeklyStatsUseCase = getWeeklyStatsUseCase,
            getWeeklyProgressUseCase = getWeeklyProgressUseCase,
            updateSettingsUseCase = updateSettingsUseCase,
            calculateBMIUseCase = calculateBMIUseCase,
            calculateBMRUseCase = calculateBMRUseCase,
            calculateTDEEUseCase = calculateTDEEUseCase
        )
        
        enableEdgeToEdge()
        setContent {
            CaLioTheme {
                CalorieTrackerApp(viewModel)
            }
        }
    }
}

enum class Screen {
    HOME,
    ADD_ENTRY,
    STATS,
    PROFILE,
    HISTORY
}

@Composable
fun CalorieTrackerApp(viewModel: CalorieViewModel) {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var currentNavDestination by remember { mutableStateOf(NavDestination.HOME) }
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Show bottom nav only on main screens, not on add entry screen
            if (currentScreen != Screen.ADD_ENTRY) {
                BottomNavBar(
                    currentDestination = currentNavDestination,
                    onNavigate = { destination ->
                        currentNavDestination = destination
                        currentScreen = when (destination) {
                            NavDestination.HOME -> Screen.HOME
                            NavDestination.STATS -> Screen.STATS
                            NavDestination.HISTORY -> Screen.HISTORY
                            NavDestination.PROFILE -> Screen.PROFILE
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                Screen.HOME -> {
                    EnhancedHomeScreen(
                        viewModel = viewModel,
                        onNavigateToAdd = { 
                            currentScreen = Screen.ADD_ENTRY 
                        },
                        onNavigateToStats = { 
                            currentScreen = Screen.STATS
                            currentNavDestination = NavDestination.STATS
                        },
                        onNavigateToProfile = { 
                            currentScreen = Screen.PROFILE
                            currentNavDestination = NavDestination.PROFILE
                        }
                    )
                }
            Screen.ADD_ENTRY -> {
                EnhancedAddEntryScreen(
                    viewModel = viewModel,
                    onNavigateBack = { 
                        currentScreen = Screen.HOME
                        currentNavDestination = NavDestination.HOME
                    }
                )
            }
            Screen.STATS -> {
                EnhancedStatsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { 
                        currentScreen = Screen.HOME
                        currentNavDestination = NavDestination.HOME
                    }
                )
            }
            Screen.PROFILE -> {
                ProfileScreen(
                    viewModel = viewModel,
                    onNavigateBack = { 
                        currentScreen = Screen.HOME
                        currentNavDestination = NavDestination.HOME
                    }
                )
            }
                Screen.HISTORY -> {
                    HistoryScreen(
                        viewModel = viewModel,
                        onNavigateBack = { 
                            currentScreen = Screen.HOME
                            currentNavDestination = NavDestination.HOME
                        }
                    )
                }
            }
        }
    }
}
