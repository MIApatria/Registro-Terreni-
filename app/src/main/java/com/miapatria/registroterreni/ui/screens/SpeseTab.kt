package com.miapatria.registroterreni.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miapatria.registroterreni.MainViewModel
import com.miapatria.registroterreni.data.model.Categorie
import com.miapatria.registroterreni.data.model.CategoriaExtra
import com.miapatria.registroterreni.data.model.Spesa
import com.miapatria.registroterreni.data.model.Terreno
import com.miapatria.registroterreni.ui.components.ConfirmDialog
import com.miapatria.registroterreni.ui.components.EmptyState
import com.miapatria.registroterreni.ui.components.StatTile
import com.miapatria.registroterreni.ui.components.TextPromptDialog
import com.miapatria.registroterreni.ui.theme.UscitaRed
import com.miapatria.registroterreni.util.Exporter
import com.miapatria.registroterreni.util.euro

private val SidebarWidth = 132.dp

@Composable
fun SpeseTab(vm: MainViewModel, terreno: Terreno) {
    val context = LocalContext.current
    val speseAll by vm.spese.collectAsStateWithLifecycle()
    val categorie by vm.categorie.collectAsStateWithLifecycle()
    val spese = speseAll.filter { it.terrenoId == terreno.id }.sortedByDescending { it.data }

    var editor by remember { mutableStateOf<Spesa?>(null) }
    var addCategoria by remember { mutableStateOf(false) }
    var deleteCategoria by remember { mutableStateOf<CategoriaExtra?>(null) }

    val totale = spese.sumOf { it.importo }
    val daSaldare = spese.filter { !it.saldato }.sumOf { it.importo }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatTile("Totale spese", euro(totale), UscitaRed, Modifier.weight(1f))
            StatTile("Da saldare", euro(daSaldare), MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
        }

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = {
                Exporter.share(context, Exporter.speseReport(context, terreno.nome, listOf(terreno), spese))
            }) { Icon(Icons.Filled.PictureAsPdf, "Esporta PDF") }
            IconButton(onClick = {
                Exporter.share(context, Exporter.speseCsv(context, listOf(terreno), spese))
            }) { Icon(Icons.Filled.TableChart, "Esporta CSV") }
        }

        Row(Modifier.fillMaxWidth().weight(1f)) {
            // Colonna sinistra: voci di spesa, una sotto l'altra
            Column(
                Modifier
                    .width(SidebarWidth)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 8.dp, bottom = 16.dp)
            ) {
                Text(
                    "Aggiungi spesa",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Categorie.PREDEFINITE.forEach { cat ->
                    CategoriaSideButton(
                        nome = cat,
                        onClick = { editor = Spesa(terrenoId = terreno.id, categoria = cat) }
                    )
                    Spacer(Modifier.height(8.dp))
                }
                categorie.forEach { c ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CategoriaSideButton(
                            nome = c.nome,
                            onClick = { editor = Spesa(terrenoId = terreno.id, categoria = c.nome) },
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { deleteCategoria = c },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                "Elimina voce",
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
                FilledTonalButton(
                    onClick = { addCategoria = true },
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Add, null, Modifier.height(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Aggiungi", style = MaterialTheme.typography.labelMedium)
                }
            }

            // Divisore verticale
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )

            // Colonna destra: elenco spese
            Box(Modifier.weight(1f).fillMaxHeight()) {
                if (spese.isEmpty()) {
                    EmptyState(
                        icon = Icons.Filled.Payments,
                        title = "Nessuna spesa",
                        subtitle = "Scegli una voce a sinistra per registrare la prima spesa."
                    )
                } else {
                    LazyColumn(
                        Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(spese, key = { it.id }) { s -> SpesaRow(s) { editor = s } }
                    }
                }
            }
        }
    }

    editor?.let { s ->
        SpesaEditor(
            initial = s,
            onSave = { vm.saveSpesa(it) },
            onDelete = if (s.id.isNotBlank()) ({ vm.deleteSpesa(s) }) else null,
            onDismiss = { editor = null }
        )
    }
    if (addCategoria) {
        TextPromptDialog(
            title = "Nuova voce di spesa",
            label = "Nome della voce (es. ASSICURAZIONE)",
            confirmLabel = "Aggiungi",
            onConfirm = { vm.addCategoria(it) },
            onDismiss = { addCategoria = false }
        )
    }
    deleteCategoria?.let { c ->
        ConfirmDialog(
            title = "Eliminare \"${c.nome}\"?",
            message = "La voce verrà rimossa dall'elenco. Le spese già registrate con questa voce non vengono toccate.",
            onConfirm = { vm.deleteCategoria(c) },
            onDismiss = { deleteCategoria = null }
        )
    }
}

@Composable
private fun CategoriaSideButton(nome: String, onClick: () -> Unit, modifier: Modifier = Modifier.fillMaxWidth()) {
    OutlinedButton(
        onClick = onClick,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 8.dp),
        modifier = modifier
    ) {
        Icon(iconForCategoria(nome), null, Modifier.height(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(
            nome,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}
