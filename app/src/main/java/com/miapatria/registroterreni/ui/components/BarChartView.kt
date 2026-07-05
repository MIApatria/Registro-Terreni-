package com.miapatria.registroterreni.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Serie(val nome: String, val valori: List<Double>, val colore: Color)

/**
 * Grafico a barre raggruppate disegnato con Canvas (nessuna libreria esterna).
 * Ogni voce di [labels] è un gruppo (es. un anno) con una barra per ogni [serie].
 */
@Composable
fun BarChartView(
    labels: List<String>,
    serie: List<Serie>,
    modifier: Modifier = Modifier,
    height: Dp = 200.dp
) {
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val density = LocalDensity.current
    val maxVal = (serie.flatMap { it.valori }.maxOrNull() ?: 0.0).coerceAtLeast(1.0)

    Column(modifier.fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().height(height)) {
            val axis = size.height - 28f
            val groupW = size.width / labels.size.coerceAtLeast(1)
            val barCount = serie.size.coerceAtLeast(1)
            val barW = (groupW * 0.6f) / barCount
            val labelPx = with(density) { 10.sp.toPx() }

            drawLine(labelColor, Offset(0f, axis), Offset(size.width, axis), strokeWidth = 1.5f)

            val paint = android.graphics.Paint().apply {
                color = labelColor.toArgb()
                textSize = labelPx
                textAlign = android.graphics.Paint.Align.CENTER
                isAntiAlias = true
            }
            labels.forEachIndexed { i, lab ->
                val groupStart = groupW * i + groupW * 0.2f
                serie.forEachIndexed { s, serieItem ->
                    val v = serieItem.valori.getOrElse(i) { 0.0 }
                    val h = (v / maxVal * (axis - 6f)).toFloat()
                    val x = groupStart + barW * s
                    drawRoundRect(
                        color = serieItem.colore,
                        topLeft = Offset(x, axis - h),
                        size = Size(barW * 0.9f, h),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }
                drawIntoCanvas { c ->
                    c.nativeCanvas.drawText(lab, groupW * i + groupW / 2f, size.height - 6f, paint)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            serie.forEach { s ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(12.dp).clip(RoundedCornerShape(3.dp)).background(s.colore))
                    Spacer(Modifier.width(6.dp))
                    Text(s.nome, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
