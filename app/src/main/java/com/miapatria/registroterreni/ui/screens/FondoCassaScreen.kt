package com.miapatria.registroterreni.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miapatria.registroterreni.MainViewModel
import com.miapatria.registroterreni.data.model.Esecutori
import com.miapatria.registroterreni.data.model.FondoMovimento
import com.miapatria.registroterreni.ui.components.DateField
import com.miapatria.registroterreni.ui.components.StatTile
import com.miapatria.registroterreni.ui.theme.EntrataGreen
import com.miapatria.registroterreni.ui.theme.UscitaRed
import com.miapatria.registroterreni.util.Exporter
import com.miapatria.registroterreni.util.euro
import com.miapatria.registroterreni.util.formatDate
import com.miapatria.registroterreni.util.parseDecimal

private sealed class Movimento(val data: Long) {
    class Versamento(val m: FondoMovimento) : Movimento(m.data)
    class Addebito(val descr: String, val importo: Double, data: Long) : Movimento(data)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FondoCassaScreen(vm: MainViewModel) {
    val context = LocalContext.current
    val fondo by vm.fondo.collectAsStateWithLifecycle()
    val spese by vm.spese.collectAsStateWithLifecycle()
    val terreni by vm.terreni.collectAsStateWithLifecycle()

    var editor by remember { mutableStateOf<FondoMovimento?>(null) }

    val nomi = terreni.associate { it.id to it.nome }
    val addebiti = spese.filter { it.esecutore == Esecutori.FONDOCASSA }
    val saldo = fondo.sumOf { it.importo } - addebiti.sumOf { it.importo }

    val movimenti = (fondo.map { Movimento.Versamento(it) } +
        addebiti.map { Movimento.Addebito("${it.etichetta} (${nomi[it.terrenoId] ?: "—"})", it.importo, it.data) })
        .sortedByDescending { it.data }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fondo cassa") },
                actions = {
                    IconButton(onClick = {
                        Exporter.share(context, Exporter.fondoReport(context, null, saldo, fondo, addebiti, terreni))
                    }) { Icon(Icons.Filled.PictureAsPdf, "Esporta PDF") }
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
                onClick = { editor = FondoMovimento() },
                icon = { Icon(Icons.Filled.Add, null) },
                text = { Text("Aggiungi soldi") }
            )
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).padding(16.dp)) {
            StatTile(
                "Saldo attuale",
                euro(saldo),
                if (saldo >= 0) EntrataGreen else UscitaRed,
                Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Text("Storico movimenti", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (movimenti.isEmpty()) {
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Savings, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(12.dp))
                        Text("Aggiungi soldi al fondo con il pulsante in basso. Le spese pagate " +
                            "con \"FONDOCASSA\" verranno scalate automaticamente.")
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(movimenti) { mov ->
                        when (mov) {
                            is Movimento.Versamento -> MovimentoCard(
                                titolo = "Versamento",
                                sottotitolo = formatDate(mov.m.data) + if (mov.m.note.isBlank()) "" else " · ${mov.m.note}",
                                importo = mov.m.importo,
                                positivo = true,
                                onClick = { editor = mov.m }
                            )
                            is Movimento.Addebito -> MovimentoCard(
                                titolo = "Spesa · ${mov.descr}",
                                sottotitolo = formatDate(mov.data) + " · pagata dal fondo",
                                importo = -mov.importo,
                                positivo = false,
                                onClick = null
                            )
                        }
                    }
                }
            }
        }
    }

    editor?.let { m ->
        FondoEditor(
            initial = m,
            onSave = { vm.saveFondo(it) },
            onDelete = if (m.id.isNotBlank()) ({ vm.deleteFondo(m) }) else null,
            onDismiss = { editor = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovimentoCard(titolo: String, sottotitolo: String, importo: Double, positivo: Boolean, onClick: (() -> Unit)?) {
    val card: @Composable (@Composable () -> Unit) -> Unit = { inner ->
        if (onClick != null) Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) { inner() }
        else Card(Modifier.fillMaxWidth()) { inner() }
    }
    card {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(titolo, style = MaterialTheme.typography.titleMedium)
                Text(sottotitolo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                (if (positivo) "+ " else "− ") + euro(kotlin.math.abs(importo)),
                style = MaterialTheme.typography.titleMedium,
                color = if (positivo) EntrataGreen else UscitaRed
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FondoEditor(
    initial: FondoMovimento,
    onSave: (FondoMovimento) -> Unit,
    onDelete: (() -> Unit)?,
    onDismiss: () -> Unit
) {
    val sheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var importo by remember { mutableStateOf(if (initial.importo == 0.0) "" else initial.importo.toString().replace('.', ',')) }
    var note by remember { mutableStateOf(initial.note) }
    var data by remember { mutableLongStateOf(initial.data) }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheet) {
        Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).imePadding().padding(20.dp)) {
            Text(
                if (initial.id.isBlank()) "Aggiungi soldi al fondo" else "Modifica versamento",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = importo,
                onValueChange = { importo = it },
                label = { Text("Importo in €") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            DateField(value = data, onChange = { data = it })
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (facoltative)") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    onSave(initial.copy(importo = parseDecimal(importo), note = note.trim(), data = data))
                    onDismiss()
                },
                enabled = parseDecimal(importo) > 0,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Salva") }
            if (onDelete != null) {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { onDelete(); onDismiss() }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Filled.Delete, null); Spacer(Modifier.width(8.dp)); Text("Elimina")
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
