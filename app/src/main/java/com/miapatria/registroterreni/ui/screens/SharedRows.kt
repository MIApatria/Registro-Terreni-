package com.miapatria.registroterreni.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.miapatria.registroterreni.data.model.Raccolto
import com.miapatria.registroterreni.data.model.Spesa
import com.miapatria.registroterreni.ui.components.Pill
import com.miapatria.registroterreni.ui.theme.EntrataGreen
import com.miapatria.registroterreni.ui.theme.UscitaRed
import com.miapatria.registroterreni.util.euro
import com.miapatria.registroterreni.util.formatDate
import com.miapatria.registroterreni.util.kg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpesaRow(s: Spesa, terrenoNome: String? = null, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(iconForCategoria(s.categoria), null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(s.etichetta, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                val meta = buildString {
                    append(formatDate(s.data))
                    if (terrenoNome != null) append(" · $terrenoNome")
                    if (s.esecutore.isNotBlank()) append(" · ${s.esecutore}")
                    if (s.note.isNotBlank()) append(" · ${s.note}")
                }
                Text(
                    meta,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(euro(s.importo), style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                if (s.saldato) Pill("Saldato", EntrataGreen.copy(alpha = 0.15f), EntrataGreen)
                else Pill("Da saldare", UscitaRed.copy(alpha = 0.15f), UscitaRed)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RaccoltoRow(r: Raccolto, terrenoNome: String? = null, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("${r.numero}° staccata", style = MaterialTheme.typography.titleMedium)
                val meta = buildString {
                    append("${kg(r.kg)} · ${euro(r.prezzoKg)}/kg · ${formatDate(r.data)}")
                    if (terrenoNome != null) append(" · $terrenoNome")
                    if (r.note.isNotBlank()) append(" · ${r.note}")
                }
                Text(
                    meta,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(euro(r.totale), style = MaterialTheme.typography.titleMedium, color = EntrataGreen)
        }
    }
}
