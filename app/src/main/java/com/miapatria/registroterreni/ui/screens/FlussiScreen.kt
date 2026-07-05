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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miapatria.registroterreni.MainViewModel
import com.miapatria.registroterreni.ui.components.BarChartView
import com.miapatria.registroterreni.ui.components.Serie
import com.miapatria.registroterreni.ui.components.StatTile
import com.miapatria.registroterreni.ui.theme.EntrataGreen
import com.miapatria.registroterreni.ui.theme.UscitaRed
import com.miapatria.registroterreni.util.Exporter
import com.miapatria.registroterreni.util.anniDisponibili
import com.miapatria.registroterreni.util.euro
import com.miapatria.registroterreni.util.riepiloghiPerAnno

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlussiScreen(vm: MainViewModel) {
    val context = LocalContext.current
    val spese by vm.spese.collectAsStateWithLifecycle()
    val raccolti by vm.raccolti.collectAsStateWithLifecycle()

    val anni = anniDisponibili(spese, raccolti)
    var selAnni by remember { mutableStateOf(setOf<Int>()) }

    val riepiloghi = riepiloghiPerAnno(anni, spese, raccolti)
    val entrateTot = riepiloghi.sumOf { it.entrate }
    val usciteTot = riepiloghi.sumOf { it.uscite }
    val utile = entrateTot - usciteTot

    val perExport = if (selAnni.isEmpty()) riepiloghi else riepiloghi.filter { selAnni.contains(it.anno) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flussi di cassa") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { pad ->
        LazyColumn(
            Modifier.fillMaxSize().padding(pad),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatTile("Entrate totali", euro(entrateTot), EntrataGreen, Modifier.weight(1f))
                    StatTile("Uscite totali", euro(usciteTot), UscitaRed, Modifier.weight(1f))
                }
            }
            item {
                StatTile(
                    if (utile >= 0) "Utile complessivo" else "Perdita complessiva",
                    euro(utile),
                    if (utile >= 0) EntrataGreen else UscitaRed,
                    Modifier.fillMaxWidth()
                )
            }

            if (anni.isEmpty()) {
                item {
                    Text(
                        "Non ci sono ancora dati. Registra spese e raccolti per vedere i flussi di cassa.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                item {
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Entrate vs Uscite per anno", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(12.dp))
                            BarChartView(
                                labels = riepiloghi.map { it.anno.toString() },
                                serie = listOf(
                                    Serie("Entrate", riepiloghi.map { it.entrate }, EntrataGreen),
                                    Serie("Uscite", riepiloghi.map { it.uscite }, UscitaRed)
                                ),
                                height = 200.dp
                            )
                        }
                    }
                }
                item {
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Dettaglio per anno", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            TableHeader()
                            riepiloghi.forEach { r ->
                                Row(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                                    Cell(r.anno.toString(), 1f)
                                    Cell(euro(r.entrate), 1.6f, EntrataGreen)
                                    Cell(euro(r.uscite), 1.6f, UscitaRed)
                                    Cell(euro(r.saldo), 1.6f, if (r.saldo >= 0) EntrataGreen else UscitaRed)
                                }
                            }
                        }
                    }
                }
                item {
                    Column {
                        Text("Anni da esportare nel PDF (nessuno = tutti)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(6.dp))
                        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            anni.forEach { y ->
                                FilterChip(
                                    selected = selAnni.contains(y),
                                    onClick = { selAnni = if (selAnni.contains(y)) selAnni - y else selAnni + y },
                                    label = { Text("$y") }
                                )
                            }
                        }
                    }
                }
                item {
                    Button(
                        onClick = { Exporter.share(context, Exporter.flussiReport(context, perExport)) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.PictureAsPdf, null)
                        Spacer(Modifier.height(0.dp))
                        Text("  Scarica PDF flussi di cassa")
                    }
                }
            }
        }
    }
}

@Composable
private fun TableHeader() {
    Row(Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
        Cell("Anno", 1f, weightBold = true)
        Cell("Entrate", 1.6f, weightBold = true)
        Cell("Uscite", 1.6f, weightBold = true)
        Cell("Saldo", 1.6f, weightBold = true)
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.Cell(
    text: String,
    weight: Float,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    weightBold: Boolean = false
) {
    Text(
        text,
        modifier = Modifier.weight(weight),
        color = color,
        style = if (weightBold) MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        else MaterialTheme.typography.bodyMedium,
        maxLines = 1
    )
}
