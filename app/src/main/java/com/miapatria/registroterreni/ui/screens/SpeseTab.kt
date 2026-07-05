package com.miapatria.registroterreni.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miapatria.registroterreni.MainViewModel
import com.miapatria.registroterreni.data.model.Categorie
import com.miapatria.registroterreni.data.model.Spesa
import com.miapatria.registroterreni.data.model.Terreno
import com.miapatria.registroterreni.ui.components.EmptyState
import com.miapatria.registroterreni.ui.components.StatTile
import com.miapatria.registroterreni.ui.components.TextPromptDialog
import com.miapatria.registroterreni.ui.theme.UscitaRed
import com.miapatria.registroterreni.util.euro

@Composable
fun SpeseTab(vm: MainViewModel, terreno: Terreno) {
    val speseAll by vm.spese.collectAsStateWithLifecycle()
    val categorie by vm.categorie.collectAsStateWithLifecycle()
    val spese = speseAll.filter { it.terrenoId == terreno.id }.sortedByDescending { it.data }

    var editor by remember { mutableStateOf<Spesa?>(null) }
    var addCategoria by remember { mutableStateOf(false) }

    val totale = spese.sumOf { it.importo }
    val daSaldare = spese.filter { !it.saldato }.sumOf { it.importo }
    val nomiCategorie = Categorie.PREDEFINITE + categorie.map { it.nome }

    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatTile("Totale spese", euro(totale), UscitaRed, Modifier.weight(1f))
            StatTile("Da saldare", euro(daSaldare), MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
        }

        Text(
            "Aggiungi una spesa:",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 16.dp, bottom = 6.dp)
        )
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            nomiCategorie.forEach { cat ->
                AssistChip(
                    onClick = { editor = Spesa(terrenoId = terreno.id, categoria = cat) },
                    label = { Text(cat) },
                    leadingIcon = { Icon(iconForCategoria(cat), null, Modifier.height(18.dp)) }
                )
            }
            SuggestionChip(
                onClick = { addCategoria = true },
                label = { Text("AGGIUNGI") },
                icon = { Icon(Icons.Filled.Add, null, Modifier.height(18.dp)) }
            )
        }

        Spacer(Modifier.height(12.dp))

        if (spese.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.Payments,
                title = "Nessuna spesa",
                subtitle = "Scegli una voce qui sopra per registrare la prima spesa."
            )
        } else {
            LazyColumn(
                Modifier.fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(spese, key = { it.id }) { s -> SpesaRow(s) { editor = s } }
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
}
