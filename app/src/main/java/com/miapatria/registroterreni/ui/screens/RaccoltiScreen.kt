package com.miapatria.registroterreni.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miapatria.registroterreni.MainViewModel
import com.miapatria.registroterreni.data.model.Raccolto
import com.miapatria.registroterreni.ui.components.BarChartView
import com.miapatria.registroterreni.ui.components.EmptyState
import com.miapatria.registroterreni.ui.components.Serie
import com.miapatria.registroterreni.ui.components.StatTile
import com.miapatria.registroterreni.ui.theme.EntrataGreen
import com.miapatria.registroterreni.ui.theme.LeafLight
import com.miapatria.registroterreni.util.Exporter
import com.miapatria.registroterreni.util.anniDisponibili
import com.miapatria.registroterreni.util.euro
import com.miapatria.registroterreni.util.kg
import com.miapatria.registroterreni.util.yearOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RaccoltiScreen(vm: MainViewModel) {
    val context = LocalContext.current
    val terreni by vm.terreni.collectAsStateWithLifecycle()
    val speseAll by vm.spese.collectAsStateWithLifecycle()
    val raccoltiAll by vm.raccolti.collectAsStateWithLifecycle()

    var anno by remember { mutableStateOf<Int?>(null) }
    var selTerreni by remember { mutableStateOf(setOf<String>()) }
    var editor by remember { mutableStateOf<Raccolto?>(null) }

    val anni = anniDisponibili(speseAll, raccoltiAll)
    val nomiTerreni = terreni.associate { it.id to it.nome }

    val filtrati = raccoltiAll.filter { r ->
        (anno == null || yearOf(r.data) == anno) &&
            (selTerreni.isEmpty() || selTerreni.contains(r.terrenoId))
    }

    val totKg = filtrati.sumOf { it.kg }
    val totEuro = filtrati.sumOf { it.totale }
    val sottotitolo = (anno?.let { "Anno $it" } ?: "Tutti gli anni")

    // Numero di staccate (distinte per terreno+numero) per anno
    val staccatePerAnno = anni.map { y ->
        raccoltiAll.filter { yearOf(it.data) == y }
            .map { it.terrenoId to it.numero }.distinct().size.toDouble()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Raccolti") },
                actions = {
                    IconButton(onClick = {
                        Exporter.share(context, Exporter.raccoltiReport(context, sottotitolo, terreni, filtrati))
                    }) { Icon(Icons.Filled.PictureAsPdf, "Esporta PDF") }
                    IconButton(onClick = {
                        Exporter.share(context, Exporter.raccoltiCsv(context, terreni, filtrati))
                    }) { Icon(Icons.Filled.TableChart, "Esporta CSV") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            if (terreni.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { editor = Raccolto(numero = 1) },
                    icon = { Icon(Icons.Filled.Add, null) },
                    text = { Text("Raccolto") }
                )
            }
        }
    ) { pad ->
        LazyColumn(
            Modifier.fillMaxSize().padding(pad),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatTile("Totale kg", kg(totKg), LeafLight, Modifier.weight(1f))
                    StatTile("Totale €", euro(totEuro), EntrataGreen, Modifier.weight(1f))
                }
            }
            item {
                Column {
                    Text("Anno", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(4.dp))
                    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(anno == null, { anno = null }, label = { Text("Tutti") })
                        anni.forEach { y -> FilterChip(anno == y, { anno = y }, label = { Text("$y") }) }
                    }
                }
            }
            if (terreni.size > 1) {
                item {
                    Column {
                        Text("Terreno", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            terreni.forEach { t ->
                                FilterChip(
                                    selected = selTerreni.contains(t.id),
                                    onClick = { selTerreni = if (selTerreni.contains(t.id)) selTerreni - t.id else selTerreni + t.id },
                                    label = { Text(t.nome) }
                                )
                            }
                        }
                    }
                }
            }
            if (anni.isNotEmpty()) {
                item {
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Numero di staccate per anno", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            BarChartView(
                                labels = anni.map { it.toString() },
                                serie = listOf(Serie("Staccate", staccatePerAnno, LeafLight)),
                                height = 160.dp
                            )
                        }
                    }
                }
            }

            if (filtrati.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Filled.Agriculture,
                        title = "Nessun raccolto",
                        subtitle = if (terreni.isEmpty()) "Aggiungi prima un terreno."
                        else "Nessun raccolto per i filtri selezionati."
                    )
                }
            } else {
                items(filtrati.sortedWith(compareByDescending<Raccolto> { it.data }), key = { it.id }) { r ->
                    RaccoltoRow(r, terrenoNome = nomiTerreni[r.terrenoId]) { editor = r }
                }
            }
        }
    }

    editor?.let { r ->
        RaccoltoEditor(
            initial = r,
            onSave = { vm.saveRaccolto(it) },
            onDelete = if (r.id.isNotBlank()) ({ vm.deleteRaccolto(r) }) else null,
            onDismiss = { editor = null },
            terreni = terreni
        )
    }
}
