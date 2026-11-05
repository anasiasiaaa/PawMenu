package com.example.pawmenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.pawmenu.ui.*

object Route {
    const val WELCOME = "welcome"; const val ANIMAL = "animal"; const val PET_SETUP = "pet_setup"
    const val FEED_TYPE = "feed_type"; const val ALLERGIES = "allergies"; const val RATION_SETUP = "ration_setup"
    const val HOME = "home"; const val DIET = "diet"; const val PETS = "pets"; const val PROFILE = "profile"
    const val COMPOSITION = "composition"; const val FOOD = "food"; const val NOTES = "notes"
    const val DIARY = "diary"; const val STUB = "stub"

    val onboarding = setOf(WELCOME, ANIMAL, PET_SETUP, FEED_TYPE, ALLERGIES, RATION_SETUP)
    val homeChildren = setOf(COMPOSITION, FOOD, NOTES, DIARY, STUB)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { AppRoot() }
    }
}

private fun NavHostController.goTab(route: String) = navigate(route) {
    popUpTo(Route.HOME) { saveState = true }
    launchSingleTop = true
    restoreState = true
}

@Composable
fun AppRoot() {
    var mode by rememberSaveable { mutableStateOf(ThemeMode.SYSTEM) }
    PawMenuTheme(mode) {
        val nav = rememberNavController()
        val route = nav.currentBackStackEntryAsState().value?.destination?.route
        val tabs = listOf(
            Triple(Route.HOME, "🏠", "Главная"), Triple(Route.DIET, "📅", "Рацион"),
            Triple(Route.PETS, "🐾", "Питомцы"), Triple(Route.PROFILE, "👤", "Профиль")
        )
        val back: () -> Unit = { nav.popBackStack() }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (route != null && route !in Route.onboarding) {
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                        tabs.forEach { (r, icon, label) ->
                            NavigationBarItem(
                                selected = r == route || (r == Route.HOME && route in Route.homeChildren),
                                onClick = { nav.goTab(r) },
                                icon = { Text(icon) }, label = { Text(label) }
                            )
                        }
                    }
                }
            }
        ) { pad ->
            NavHost(nav, startDestination = Route.WELCOME, modifier = Modifier.padding(pad)) {
                // --- онбординг ---
                composable(Route.WELCOME) { WelcomeScreen { nav.navigate(Route.ANIMAL) } }
                composable(Route.ANIMAL) { AnimalScreen(back) { nav.navigate(Route.PET_SETUP) } }
                composable(Route.PET_SETUP) { PetProfileScreen(back) { nav.navigate(Route.FEED_TYPE) } }
                composable(Route.FEED_TYPE) { FeedTypeScreen(back) { nav.navigate(Route.ALLERGIES) } }
                composable(Route.ALLERGIES) { AllergiesScreen(back) { nav.navigate(Route.RATION_SETUP) } }
                composable(Route.RATION_SETUP) {
                    RationScreen("Сохранить рацион", back, { nav.navigate(Route.COMPOSITION) }) {
                        nav.navigate(Route.HOME) { popUpTo(Route.WELCOME) { inclusive = true } }
                    }
                }
                // --- вкладки ---
                composable(Route.HOME) {
                    HomeScreen(
                        onPet = { nav.goTab(Route.PETS) }, onDiary = { nav.navigate(Route.DIARY) },
                        onFood = { nav.navigate(Route.FOOD) }, onRation = { nav.goTab(Route.DIET) },
                        onNotes = { nav.navigate(Route.NOTES) }, onStub = { nav.navigate(Route.STUB) }
                    )
                }
                composable(Route.DIET) { RationScreen("Подобрать корм", null, { nav.navigate(Route.COMPOSITION) }) { nav.navigate(Route.FOOD) } }
                composable(Route.PETS) { PetProfileScreen() }
                composable(Route.PROFILE) { ProfileScreen(mode) { mode = it } }
                // --- вложенные экраны ---
                composable(Route.COMPOSITION) { CompositionScreen(back) }
                composable(Route.FOOD) { FoodPickerScreen(back) }
                composable(Route.NOTES) { NotesScreen(back) }
                composable(Route.DIARY) { DiaryScreen(back) }
                composable(Route.STUB) { StubScreen("Анализы", back) }
            }
        }
    }
}