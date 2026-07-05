package com.miapatria.registroterreni

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miapatria.registroterreni.ui.components.Loading
import com.miapatria.registroterreni.ui.nav.AppNav
import com.miapatria.registroterreni.ui.screens.LoginScreen
import com.miapatria.registroterreni.ui.screens.SetupScreen
import com.miapatria.registroterreni.ui.theme.RegistroTerreniTheme

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroTerreniTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    RootScreen(vm)
                }
            }
        }
    }
}

@Composable
private fun RootScreen(vm: MainViewModel) {
    val config by vm.config.collectAsStateWithLifecycle()
    val user by vm.user.collectAsStateWithLifecycle()
    val initError by vm.initError.collectAsStateWithLifecycle()
    val authBusy by vm.authBusy.collectAsStateWithLifecycle()
    val authError by vm.authError.collectAsStateWithLifecycle()

    val cfg = config
    when {
        cfg == null -> Loading("Avvio…")
        !cfg.isReady || initError != null -> SetupScreen(
            initial = cfg,
            errorMessage = initError,
            onSave = { vm.saveConfig(it) }
        )
        user == null -> LoginScreen(
            busy = authBusy,
            error = authError,
            onLogin = { e, p -> vm.login(e, p) },
            onRegister = { e, p -> vm.register(e, p) },
            onReconfigure = { vm.clearConfig() }
        )
        else -> AppNav(vm)
    }
}
