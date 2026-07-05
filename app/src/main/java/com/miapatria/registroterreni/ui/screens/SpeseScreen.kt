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
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.miapatria.registroterreni.data.model.Categorie
import com.miapatria.registroterreni.data.model.Esecutori
import com.miapatria.registroterreni.data.model.Spesa
import com.miapatria.registroterreni.ui.components.EmptyState
import com.miapatria.registroterreni.ui.components.StatTile
import com.miapatria.registroterreni.ui.theme.UscitaRed
import com.miapatria.registroterreni.util.Exporter
import com.miapatria.registroterreni.util.anniDisponibili
import com.miapatria.registroterreni.util.euro
import com.miapatria.registroterreni.util.yearOf

private enum class StatoFiltro { TUTTI, SALDATO, DA_SALDARE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeseScreen(vm: MainViewModel) {
    val context = LocalContext.current
    val terreni by vm.terreni.collectAsStateWithLifecycle()
    val speseAll by vm.spese.collectAsStateWithLifecycle()
    val raccolti by vm.raccolti.collectAsStateWithLifecycle()
    val categorieExtra by vm.categorie.collectAsStateWithLifecycle()

    var search by remember { mutableStateOf("") }
    var anno by remember { mutableStateOf<Int?>(null) }
    var stato by remember { mutableStateOf(StatoFiltro.TUTTI) }
    var selCategorie by remember { mutableStateOf(setOf<String>()) }
    var selEsecutori by remember { mutableStateOf(setOf<String>()) }
    var selTerreni by remember { mutableStateOf(setOf<String>()) }
    var editor by remember { mutableStateOf<Spesa?>(null) }

    val anni = anniDisponibili(speseAll, raccolti)
    val nomiCategorie = Categorie.PREDEFINITE + categorieExtra.map { it.nome }
    val nomiTerreni = terreni.associate { it.id to it.nome }

    val filtrate = speseAll.filter { s ->
        (anno == null || yearOf(s.data) == anno) &&
            (stato == StatoFiltro.TUTTI || (stato == StatoFiltro.SALDATO) == s.saldato) &&
            (selCategorie.isEmpty() || selCategorie.contains(s.categoria)) &&
            (selEsecutori.isEmpty() || selEsecutori.contains(s.esecutore)) &&
            (selTerreni.isEmpty() || selTerreni.contains(s.terrenoId)) &&
            (search.isBlank() || s.note.contains(search, true) || s.sottocategoria.contains(search, true))
    }.sortedByDescending { it.data }

    val totale = filtrate.sumOf { it.importo }
    val sottotitolo = (anno?.let { "Anno $it" } ?: "Tutti gli anni") +
        if (filtrate.size != speseAll.size) " · filtrato" else ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Spese") },
                actions = {
                    androidx.compose.material3.IconButton(onClick = {
                        Exporter.share(context, Exporter.speseReport(context, sottotitolo, terreni, filtrate))
                    }) { Icon(Icons.Filled.PictureAsPdf, "Esporta PDF") }
                    androidx.compose.material3.IconButton(onClick = {
                        Exporter.share(context, Exporter.speseCsv(context, terreni, filtrate))
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
                    onClick = { editor = Spesa(categoria = Categorie.ALTRE) },
                    icon = { Icon(Icons.Filled.Add, null) },
                    text = { Text("Spesa") }
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
                StatTile("Totale spese (filtrato)", euro(totale), UscitaRed, Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    label = { Text("Cerca nelle note") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                ChipRow("Anno") {
                    FilterChip(selected = anno == null, onClick = { anno = null }, label = { Text("Tutti") })
                    anni.forEach { y ->
                        FilterChip(selected = anno == y, onClick = { anno = y }, label = { Text("$y") })
                    }
                }
            }
            item {
                ChipRow("Stato") {
                    FilterChip(stato == StatoFiltro.TUTTI, { stato = StatoFiltro.TUTTI }, label = { Text("Tutti") })
                    FilterChip(stato == StatoFiltro.SALDATO, { stato = StatoFiltro.SALDATO }, label = { Text("Saldato") })
                    FilterChip(stato == StatoFiltro.DA_SALDARE, { stato = StatoFiltro.DA_SALDARE }, label = { Text("Da saldare") })
                }
            }
            item {
                ChipRow("Voce") {
                    nomiCategorie.forEach { c ->
                        FilterChip(
                            selected = selCategorie.contains(c),
                            onClick = { selCategorie = selCategorie.toggle(c) },
                            label = { Text(c) }
                        )
                    }
                }
            }
            item {
                ChipRow("Chi ha pagato") {
                    Esecutori.TUTTI.forEach { e ->
                        FilterChip(
                            selected = selEsecutori.contains(e),
                            onClick = { selEsecutori = selEsecutori.toggle(e) },
                            label = { Text(e) }
                        )
                    }
                }
            }
            if (terreni.size > 1) {
                item {
                    ChipRow("Terreno") {
                        terreni.forEach { t ->
                            FilterChip(
                                selected = selTerreni.contains(t.id),
                                onClick = { selTerreni = selTerreni.toggle(t.id) },
                                label = { Text(t.nome) }
                            )
                        }
                    }
                }
            }

            if (filtrate.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Filled.Payments,
                        title = "Nessuna spesa",
                        subtitle = if (terreni.isEmpty()) "Aggiungi prima un terreno."
                        else "Nessuna spesa per i filtri selezionati."
                    )
                }
            } else {
                items(filtrate, key = { it.id }) { s ->
                    SpesaRow(s, terrenoNome = nomiTerreni[s.terrenoId]) { editor = s }
                }
            }
        }
    }

    editor?.let { s ->
        SpesaEditor(
            initial = s,
            onSave = { vm.saveSpesa(it) },
            onDelete = if (s.id.isNotBlank()) ({ vm.deleteSpesa(s) }) else null,
            onDismiss = { editor = null },
            terreni = terreni,
            categorieDisponibili = if (s.id.isBlank()) nomiCategorie else emptyList()
        )
    }
}

@Composable
private fun ChipRow(title: String, content: @Composable () -> Unit) {
    Column {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) { content() }
    }
}

private fun Set<String>.toggle(v: String): Set<String> =
    if (contains(v)) this - v else this + v
