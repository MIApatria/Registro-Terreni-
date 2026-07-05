package com.miapatria.registroterreni.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.miapatria.registroterreni.util.formatDate

/** Campo data che apre un calendario. La data pre-compilata è quella odierna. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(
    value: Long,
    onChange: (Long) -> Unit,
    label: String = "Data",
    modifier: Modifier = Modifier
) {
    var show by remember { mutableStateOf(false) }

    Box(modifier) {
        OutlinedTextField(
            value = formatDate(value),
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text(label) },
            trailingIcon = { Icon(Icons.Filled.CalendarMonth, contentDescription = "Scegli data") },
            modifier = Modifier.fillMaxWidth()
        )
        // Overlay trasparente per intercettare il tap (il campo è disabilitato).
        Box(Modifier.matchParentSize().clickable { show = true })
    }

    if (show) {
        val state = rememberDatePickerState(initialSelectedDateMillis = value)
        DatePickerDialog(
            onDismissRequest = { show = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let(onChange)
                    show = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { show = false }) { Text("Annulla") } }
        ) { DatePicker(state = state) }
    }
}

/** Dialog con un singolo campo di testo. */
@Composable
fun TextPromptDialog(
    title: String,
    label: String,
    initial: String = "",
    confirmLabel: String = "Salva",
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(initial) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(label) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                enabled = text.isNotBlank(),
                onClick = { onConfirm(text.trim()); onDismiss() }
            ) { Text(confirmLabel) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } }
    )
}
