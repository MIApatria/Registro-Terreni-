package com.miapatria.registroterreni.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.miapatria.registroterreni.data.model.Categorie
import com.miapatria.registroterreni.data.model.Esecutori
import com.miapatria.registroterreni.data.model.Raccolto
import com.miapatria.registroterreni.data.model.Spesa
import com.miapatria.registroterreni.data.model.Terreno
import com.miapatria.registroterreni.ui.components.DateField
import com.miapatria.registroterreni.util.euro
import com.miapatria.registroterreni.util.parseDecimal

private fun decimalToText(v: Double) = if (v == 0.0) "" else v.toString().replace('.', ',')

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Dropdown(
    label: String,
    value: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt) },
                    onClick = { onSelect(opt); expanded = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpesaEditor(
    initial: Spesa,
    onSave: (Spesa) -> Unit,
    onDelete: (() -> Unit)?,
    onDismiss: () -> Unit,
    terreni: List<Terreno> = emptyList(),
    categorieDisponibili: List<String> = emptyList()
) {
    val sheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var terrenoId by remember { mutableStateOf(initial.terrenoId) }
    var categoria by remember { mutableStateOf(initial.categoria) }
    var sottocategoria by remember { mutableStateOf(initial.sottocategoria) }
    var esecutore by remember { mutableStateOf(initial.esecutore) }
    var importo by remember { mutableStateOf(decimalToText(initial.importo)) }
    var note by remember { mutableStateOf(initial.note) }
    var saldato by remember { mutableStateOf(initial.saldato) }
    var data by remember { mutableLongStateOf(initial.data) }

    val needTerreno = initial.terrenoId.isBlank() && terreni.isNotEmpty()

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheet) {
        Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).imePadding().padding(20.dp)
        ) {
            Text(
                if (initial.id.isBlank()) "Nuova spesa" else "Modifica spesa",
                style = MaterialTheme.typography.titleLarge
            )
            if (categorieDisponibili.isEmpty()) {
                Text(categoria, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.height(16.dp))

            if (needTerreno) {
                Dropdown(
                    label = "Terreno",
                    value = terreni.firstOrNull { it.id == terrenoId }?.nome ?: "",
                    options = terreni.map { it.nome },
                    onSelect = { nome -> terrenoId = terreni.first { it.nome == nome }.id }
                )
                Spacer(Modifier.height(12.dp))
            }

            if (categorieDisponibili.isNotEmpty()) {
                Dropdown(
                    label = "Voce di spesa",
                    value = categoria,
                    options = categorieDisponibili,
                    onSelect = { categoria = it }
                )
                Spacer(Modifier.height(12.dp))
            }

            if (categoria == Categorie.ALTRE) {
                OutlinedTextField(
                    value = sottocategoria,
                    onValueChange = { sottocategoria = it },
                    label = { Text("Tipologia di spesa") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
            }

            OutlinedTextField(
                value = importo,
                onValueChange = { importo = it },
                label = { Text("Spesa in €") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            Dropdown(
                label = "Chi ha pagato",
                value = esecutore,
                options = Esecutori.TUTTI,
                onSelect = { esecutore = it }
            )
            Spacer(Modifier.height(12.dp))

            DateField(value = data, onChange = { data = it })
            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = saldato, onCheckedChange = { saldato = it })
                Spacer(Modifier.width(12.dp))
                Text(if (saldato) "Saldato" else "Non saldato", style = MaterialTheme.typography.titleMedium)
            }
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
                    onSave(
                        initial.copy(
                            terrenoId = terrenoId,
                            categoria = categoria,
                            sottocategoria = sottocategoria.trim(),
                            esecutore = esecutore,
                            importo = parseDecimal(importo),
                            note = note.trim(),
                            saldato = saldato,
                            data = data
                        )
                    )
                    onDismiss()
                },
                enabled = terrenoId.isNotBlank() && categoria.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Salva spesa") }

            DeleteButton(onDelete, onDismiss)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RaccoltoEditor(
    initial: Raccolto,
    onSave: (Raccolto) -> Unit,
    onDelete: (() -> Unit)?,
    onDismiss: () -> Unit,
    terreni: List<Terreno> = emptyList()
) {
    val sheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var terrenoId by remember { mutableStateOf(initial.terrenoId) }
    var numero by remember { mutableStateOf(initial.numero.toString()) }
    var kg by remember { mutableStateOf(decimalToText(initial.kg)) }
    var prezzo by remember { mutableStateOf(decimalToText(initial.prezzoKg)) }
    var note by remember { mutableStateOf(initial.note) }
    var data by remember { mutableLongStateOf(initial.data) }

    val needTerreno = initial.terrenoId.isBlank() && terreni.isNotEmpty()
    val totale = parseDecimal(kg) * parseDecimal(prezzo)

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheet) {
        Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).imePadding().padding(20.dp)
        ) {
            Text(
                if (initial.id.isBlank()) "Nuova linea di raccolta" else "Modifica linea",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(16.dp))

            if (needTerreno) {
                Dropdown(
                    label = "Terreno",
                    value = terreni.firstOrNull { it.id == terrenoId }?.nome ?: "",
                    options = terreni.map { it.nome },
                    onSelect = { nome -> terrenoId = terreni.first { it.nome == nome }.id }
                )
                Spacer(Modifier.height(12.dp))
            }

            OutlinedTextField(
                value = numero,
                onValueChange = { numero = it.filter { c -> c.isDigit() } },
                label = { Text("Numero staccata") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = kg,
                onValueChange = { kg = it },
                label = { Text("Quantità raccolta (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = prezzo,
                onValueChange = { prezzo = it },
                label = { Text("Prezzo unitario (€/kg)") },
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
                label = { Text("Note / prodotto (facoltative)") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            Text(
                "Importo totale: ${euro(totale)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    onSave(
                        initial.copy(
                            terrenoId = terrenoId,
                            numero = numero.toIntOrNull() ?: initial.numero,
                            kg = parseDecimal(kg),
                            prezzoKg = parseDecimal(prezzo),
                            note = note.trim(),
                            data = data
                        )
                    )
                    onDismiss()
                },
                enabled = terrenoId.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Salva linea") }

            DeleteButton(onDelete, onDismiss)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DeleteButton(onDelete: (() -> Unit)?, onDismiss: () -> Unit) {
    if (onDelete != null) {
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = { onDelete(); onDismiss() }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Filled.Delete, null)
            Spacer(Modifier.width(8.dp))
            Text("Elimina")
        }
    }
}
