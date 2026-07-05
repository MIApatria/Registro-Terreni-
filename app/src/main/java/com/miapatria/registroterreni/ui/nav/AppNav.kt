package com.miapatria.registroterreni.ui.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.miapatria.registroterreni.MainViewModel
import com.miapatria.registroterreni.ui.screens.FlussiScreen
import com.miapatria.registroterreni.ui.screens.FondoCassaScreen
import com.miapatria.registroterreni.ui.screens.RaccoltiScreen
import com.miapatria.registroterreni.ui.screens.SettingsScreen
import com.miapatria.registroterreni.ui.screens.SpeseScreen
import com.miapatria.registroterreni.ui.screens.TerreniScreen
import com.miapatria.registroterreni.ui.screens.TerrenoDetailScreen

private data class TopDest(val route: String, val label: String, val icon: ImageVector)

private val topDestinations = listOf(
    TopDest("terreni", "Terreni", Icons.Filled.Grass),
    TopDest("spese", "Spese", Icons.Filled.Payments),
    TopDest("raccolti", "Raccolti", Icons.Filled.Agriculture),
    TopDest("fondo", "Fondo", Icons.Filled.Savings),
    TopDest("flussi", "Flussi", Icons.Filled.ShowChart)
)

@Composable
fun AppNav(vm: MainViewModel) {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route
    val showBottomBar = topDestinations.any { it.route == route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    topDestinations.forEach { dest ->
                        NavigationBarItem(
                            selected = route == dest.route,
                            onClick = {
                                nav.navigate(dest.route) {
                                    popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(dest.icon, null) },
                            label = { Text(dest.label) }
                        )
                    }
                }
            }
        }
    ) { pad ->
        NavHost(
            navController = nav,
            startDestination = "terreni",
            modifier = Modifier.padding(pad)
        ) {
            composable("terreni") {
                TerreniScreen(
                    vm = vm,
                    onOpen = { nav.navigate("terreno/${it.id}") },
                    onSettings = { nav.navigate("settings") }
                )
            }
            composable("spese") { SpeseScreen(vm) }
            composable("raccolti") { RaccoltiScreen(vm) }
            composable("fondo") { FondoCassaScreen(vm) }
            composable("flussi") { FlussiScreen(vm) }
            composable("settings") { SettingsScreen(vm, onBack = { nav.popBackStack() }) }
            composable("terreno/{id}") { entry ->
                val id = entry.arguments?.getString("id").orEmpty()
                val terreno = vm.terrenoById(id)
                if (terreno == null) {
                    nav.popBackStack()
                } else {
                    TerrenoDetailScreen(vm = vm, terreno = terreno, onBack = { nav.popBackStack() })
                }
            }
        }
    }
}
