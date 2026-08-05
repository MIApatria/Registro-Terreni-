package com.francesco.crono.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francesco.crono.model.BoxKind
import com.francesco.crono.model.InfoBox

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 1.sp,
        modifier = modifier
    )
}

@Composable
fun InfoBoxCard(box: InfoBox, modifier: Modifier = Modifier) {
    val (accent, icon: ImageVector) = when (box.kind) {
        BoxKind.TICKET -> Color(0xFF2E7D32) to Icons.Filled.ConfirmationNumber
        BoxKind.WARNING -> Color(0xFFD84315) to Icons.Filled.WarningAmber
        BoxKind.TIP -> Color(0xFF1F6FB2) to Icons.Filled.Lightbulb
    }
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = accent.copy(alpha = 0.10f)
        )
    ) {
        Row(modifier = Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null, tint = accent)
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = box.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
                Spacer(Modifier.width(2.dp))
                LinkableText(
                    text = box.body,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/** Padding orizzontale standard per i contenuti a tutta larghezza. */
val ContentPadding = PaddingValues(horizontal = 16.dp)
