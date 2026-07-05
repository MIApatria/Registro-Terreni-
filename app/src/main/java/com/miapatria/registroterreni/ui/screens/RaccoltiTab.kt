package com.miapatria.registroterreni.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.miapatria.registroterreni.data.model.Terreno
import com.miapatria.registroterreni.ui.components.EmptyState
import com.miapatria.registroterreni.ui.components.StatTile
import com.miapatria.registroterreni.ui.theme.EntrataGreen
import com.miapatria.registroterreni.ui.theme.LeafLight
import com.miapatria.registroterreni.util.Exporter
import com.miapatria.registroterreni.util.euro
import com.miapatria.registroterreni.util.kg

@Composable
fun RaccoltiTab(vm: MainViewModel, terreno: Terreno) {
    val context = LocalContext.current
    val raccoltiAll by vm.raccolti.collectAsStateWithLifecycle()
    val raccolti = raccoltiAll.filter { it.terrenoId == terreno.id }
    val staccate = raccolti.groupBy { it.numero }.toSortedMap()

    var editor by remember { mutableStateOf<Raccolto?>(null) }

    val totKg = raccolti.sumOf { it.kg }
    val totEuro = raccolti.sumOf { it.totale }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatTile("Totale raccolto", kg(totKg), LeafLight, Modifier.weight(1f))
            StatTile("Totale entrate", euro(totEuro), EntrataGreen, Modifier.weight(1f))
        }

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            FilledTonalButton(
                onClick = {
                    editor = Raccolto(terrenoId = terreno.id, numero = vm.prossimaStaccata(terreno.id))
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Add, null); Spacer(Modifier.width(8.dp)); Text("Nuova staccata")
            }
            IconButton(onClick = {
                Exporter.share(context, Exporter.raccoltiReport(context, terreno.nome, listOf(terreno), raccolti))
            }) { Icon(Icons.Filled.PictureAsPdf, "Esporta PDF") }
            IconButton(onClick = {
                Exporter.share(context, Exporter.raccoltiCsv(context, listOf(terreno), raccolti))
            }) { Icon(Icons.Filled.TableChart, "Esporta CSV") }
        }

        Spacer(Modifier.height(12.dp))

        if (raccolti.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.Agriculture,
                title = "Nessun raccolto",
                subtitle = "Registra la prima \"staccata\" con il pulsante qui sopra. " +
                    "Ogni staccata può avere più linee di raccolta.",
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                Modifier.fillMaxWidth().weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                staccate.forEach { (numero, linee) ->
                    item(key = "head_$numero") {
                        StaccataHeader(
                            numero = numero,
                            kgTot = linee.sumOf { it.kg },
                            euroTot = linee.sumOf { it.totale },
                            onAddLinea = {
                                editor = Raccolto(terrenoId = terreno.id, numero = numero)
                            }
                        )
                    }
                    items(linee.sortedBy { it.data }, key = { it.id }) { r ->
                        RaccoltoRow(r) { editor = r }
                    }
                }
            }
        }
    }

    editor?.let { r ->
        RaccoltoEditor(
            initial = r,
            onSave = { vm.saveRaccolto(it) },
            onDelete = if (r.id.isNotBlank()) ({ vm.deleteRaccolto(r) }) else null,
            onDismiss = { editor = null }
        )
    }
}

@Composable
private fun StaccataHeader(numero: Int, kgTot: Double, euroTot: Double, onAddLinea: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text("$numero° staccata", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Text(
                "${kg(kgTot)} · ${euro(euroTot)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        FilledTonalButton(onClick = onAddLinea) {
            Icon(Icons.Filled.Add, null); Spacer(Modifier.width(6.dp)); Text("Linea")
        }
    }
}
