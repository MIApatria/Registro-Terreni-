package com.francesco.crono.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francesco.crono.data.CheckStore
import com.francesco.crono.model.Day
import com.francesco.crono.model.Event
import com.francesco.crono.model.Segment

@Composable
fun DayScreen(day: Day, store: CheckStore) {
    val accent = Color(day.colorHex)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Header colorato
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(accent)
                    .padding(start = 16.dp, end = 16.dp, top = 44.dp, bottom = 18.dp)
            ) {
                Text(
                    text = "GIORNO ${day.number} · ${day.date}",
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = day.title,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 30.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = day.timezone,
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Per tornare indietro usa il tasto ◁ del telefono",
                    color = Color.White.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        // Tratte in auto
        if (day.segments.isNotEmpty()) {
            item {
                SectionLabel(
                    "In auto",
                    modifier = Modifier.padding(start = 16.dp, top = 18.dp, bottom = 8.dp)
                )
            }
            items(day.segments) { seg ->
                SegmentCard(
                    seg = seg,
                    accent = accent,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

        // Programma della giornata (spuntabile)
        item {
            SectionLabel(
                "Programma della giornata",
                modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
            )
        }
        itemsIndexed(day.events) { index, event ->
            EventRow(
                event = event,
                accent = accent,
                checkKey = "day${day.number}:$index",
                store = store,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        // Note / biglietti
        if (day.boxes.isNotEmpty()) {
            item {
                SectionLabel(
                    "Note & biglietti",
                    modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
                )
            }
            items(day.boxes) { box ->
                InfoBoxCard(box = box, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            }
        }

        // Footer
        item {
            Text(
                text = day.footer,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp)
            )
        }
    }
}

@Composable
private fun SegmentCard(seg: Segment, accent: Color, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = seg.route,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = seg.info,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
            }
            if (seg.mapsQuery != null) {
                Spacer(Modifier.width(8.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        val uri = Uri.parse(
                            "https://www.google.com/maps/search/?api=1&query=" +
                                Uri.encode(seg.mapsQuery)
                        )
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                ) {
                    Icon(Icons.Filled.Map, contentDescription = "Apri in Google Maps", tint = accent)
                    Text("Mappa", style = MaterialTheme.typography.labelSmall, color = accent)
                }
            }
        }
    }
}

@Composable
private fun EventRow(
    event: Event,
    accent: Color,
    checkKey: String,
    store: CheckStore,
    modifier: Modifier = Modifier
) {
    var checked by remember(checkKey) { mutableStateOf(store.isChecked(checkKey)) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                store.toggle(checkKey)
                checked = store.isChecked(checkKey)
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (event.highlight)
                accent.copy(alpha = 0.12f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(modifier = Modifier.padding(14.dp)) {
            // Colonna orario
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(52.dp)
            ) {
                Text(
                    text = event.time,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = accent
                )
                if (event.highlight) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            // Testo
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (checked) TextDecoration.LineThrough else null,
                    color = if (checked)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                    else
                        MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(2.dp))
                if (checked) {
                    Text(
                        text = event.detail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                } else {
                    LinkableText(text = event.detail, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(Modifier.width(8.dp))
            // Casella di spunta
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(if (checked) accent else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (checked) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = "Fatto",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
