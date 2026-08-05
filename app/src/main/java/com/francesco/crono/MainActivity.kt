package com.francesco.crono

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.francesco.crono.data.CheckStore
import com.francesco.crono.data.TripData
import com.francesco.crono.ui.ChecklistScreen
import com.francesco.crono.ui.CronoTheme
import com.francesco.crono.ui.DashboardScreen
import com.francesco.crono.ui.DayScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CronoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val store = remember { CheckStore(context) }
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "dashboard") {
                        composable("dashboard") {
                            DashboardScreen(
                                store = store,
                                onOpenDay = { number -> navController.navigate("day/$number") },
                                onOpenChecklist = { navController.navigate("checklist") }
                            )
                        }
                        composable(
                            route = "day/{number}",
                            arguments = listOf(navArgument("number") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val number = backStackEntry.arguments?.getInt("number") ?: 1
                            val day = TripData.days.firstOrNull { it.number == number }
                                ?: TripData.days.first()
                            // Il tasto indietro di sistema torna alla dashboard automaticamente.
                            DayScreen(day = day, store = store)
                        }
                        composable("checklist") {
                            ChecklistScreen(store = store)
                        }
                    }
                }
            }
        }
    }
}
