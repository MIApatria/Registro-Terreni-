package com.miapatria.registroterreni.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.miapatria.registroterreni.MainViewModel
import com.miapatria.registroterreni.data.model.Terreno
import com.miapatria.registroterreni.data.repo.SyncStatus
import com.miapatria.registroterreni.ui.components.ConfirmDialog
import com.miapatria.registroterreni.ui.components.EmptyState
import com.miapatria.registroterreni.ui.components.Pill
import com.miapatria.registroterreni.ui.components.TextPromptDialog
import com.miapatria.registroterreni.ui.theme.EntrataGreen
import com.miapatria.registroterreni.ui.theme.UscitaRed
import com.miapatria.registroterreni.util.euro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerreniScreen(vm: MainViewModel, onOpen: (Terreno) -> Unit, onSettings: () -> Unit) {
    val terreni by vm.terreni.collectAsStateWithLifecycle()
    val spese by vm.spese.collectAsStateWithLifecycle()
    val raccolti by vm.raccolti.collectAsStateWithLifecycle()
    val status by vm.status.collectAsStateWithLifecycle()

    var showAdd by remember { mutableStateOf(false) }
    var rename by remember { mutableStateOf<Terreno?>(null) }
    var delete by remember { mutableStateOf<Terreno?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("I miei terreni") },
                actions = {
                    Icon(
                        if (status == SyncStatus.ONLINE) Icons.Filled.CloudDone else Icons.Filled.CloudOff,
                        contentDescription = "Stato sincronizzazione"
                    )
                    IconButton(onClick = onSettings) { Icon(Icons.Filled.Settings, "Impostazioni") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAdd = true },
                icon = { Icon(Icons.Filled.Add, null) },
                text = { Text("Terreno") }
            )
        }
    ) { pad ->
        if (terreni.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.Agriculture,
                title = "Nessun terreno",
                subtitle = "Aggiungi il primo terreno con il pulsante in basso a destra.",
                modifier = Modifier.padding(pad)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(pad),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(terreni, key = { it.id }) { t ->
                    val uscite = spese.filter { it.terrenoId == t.id }.sumOf { it.importo }
                    val entrate = raccolti.filter { it.terrenoId == t.id }.sumOf { it.totale }
                    TerrenoCard(
                        terreno = t,
                        entrate = entrate,
                        uscite = uscite,
                        onOpen = { onOpen(t) },
                        onRename = { rename = t },
                        onDelete = { delete = t }
                    )
                }
            }
        }
    }

    if (showAdd) {
        TextPromptDialog(
            title = "Nuovo terreno",
            label = "Nome del terreno",
            confirmLabel = "Aggiungi",
            onConfirm = { vm.addTerreno(it) },
            onDismiss = { showAdd = false }
        )
    }
    rename?.let { t ->
        TextPromptDialog(
            title = "Rinomina terreno",
            label = "Nome del terreno",
            initial = t.nome,
            onConfirm = { vm.renameTerreno(t, it) },
            onDismiss = { rename = null }
        )
    }
    delete?.let { t ->
        ConfirmDialog(
            title = "Eliminare \"${t.nome}\"?",
            message = "Verranno eliminate anche tutte le spese e i raccolti di questo terreno.",
            onConfirm = { vm.deleteTerreno(t) },
            onDismiss = { delete = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TerrenoCard(
    terreno: Terreno,
    entrate: Double,
    uscite: Double,
    onOpen: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit
) {
    var menu by remember { mutableStateOf(false) }
    Card(onClick = onOpen, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Agriculture, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(10.dp))
                Text(
                    terreno.nome,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Box {
                    IconButton(onClick = { menu = true }) {
                        Icon(Icons.Filled.MoreVert, "Opzioni")
                    }
                    DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                        DropdownMenuItem(
                            text = { Text("Rinomina") },
                            leadingIcon = { Icon(Icons.Filled.Edit, null) },
                            onClick = { menu = false; onRename() }
                        )
                        DropdownMenuItem(
                            text = { Text("Elimina") },
                            leadingIcon = { Icon(Icons.Filled.Delete, null) },
                            onClick = { menu = false; onDelete() }
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Pill("Entrate ${euro(entrate)}", EntrataGreen.copy(alpha = 0.15f), EntrataGreen)
                Pill("Uscite ${euro(uscite)}", UscitaRed.copy(alpha = 0.15f), UscitaRed)
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Apri registro",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(Icons.Filled.ChevronRight, null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
