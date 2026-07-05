package com.miapatria.registroterreni.ui.nav

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import com.miapatria.registroterreni.MainViewModel
import com.miapatria.registroterreni.ui.screens.SettingsScreen
import com.miapatria.registroterreni.ui.screens.TerreniScreen
import com.miapatria.registroterreni.ui.screens.TerrenoDetailScreen

/**
 * La prima schermata mostra solo l'elenco dei terreni. Selezionando un terreno
 * si apre la sua pagina, che contiene al suo interno il menu con Spese,
 * Raccolti, Fondo cassa e Flussi di cassa — tutti i dati di quel terreno.
 */
@Composable
fun AppNav(vm: MainViewModel) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "terreni") {
        composable("terreni") {
            TerreniScreen(
                vm = vm,
                onOpen = { nav.navigate("terreno/${it.id}") },
                onSettings = { nav.navigate("settings") }
            )
        }
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
