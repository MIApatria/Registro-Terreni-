package com.miapatria.registroterreni.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.miapatria.registroterreni.MainViewModel
import com.miapatria.registroterreni.data.model.Terreno

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerrenoDetailScreen(vm: MainViewModel, terreno: Terreno, onBack: () -> Unit) {
    var tab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(terreno.nome, maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Indietro")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad)) {
            ScrollableTabRow(selectedTabIndex = tab, edgePadding = 8.dp) {
                Tab(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    text = { Text("Spese") },
                    icon = { Icon(Icons.Filled.Payments, null) }
                )
                Tab(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    text = { Text("Raccolti") },
                    icon = { Icon(Icons.Filled.Agriculture, null) }
                )
                Tab(
                    selected = tab == 2,
                    onClick = { tab = 2 },
                    text = { Text("Fondo") },
                    icon = { Icon(Icons.Filled.Savings, null) }
                )
                Tab(
                    selected = tab == 3,
                    onClick = { tab = 3 },
                    text = { Text("Flussi") },
                    icon = { Icon(Icons.Filled.ShowChart, null) }
                )
            }
            Box(Modifier.weight(1f)) {
                when (tab) {
                    0 -> SpeseTab(vm, terreno)
                    1 -> RaccoltiTab(vm, terreno)
                    2 -> FondoCassaScreen(vm, terreno)
                    else -> FlussiScreen(vm, terreno)
                }
            }
        }
    }
}
