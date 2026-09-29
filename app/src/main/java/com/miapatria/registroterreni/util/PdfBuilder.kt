package com.miapatria.registroterreni.util

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument

/**
 * Piccolo motore di impaginazione per PDF in formato A4 con gestione automatica
 * dei salti pagina. Coordinate in punti (72 dpi).
 */
class PdfBuilder {
    private val pageWidth = 595
    private val pageHeight = 842
    private val margin = 40f
    private val bottom = pageHeight - margin

    private val doc = PdfDocument()
    private var page: PdfDocument.Page? = null
    private var canvas = beginNewPage()
    private var y = margin
    private var pageNumber = 1

    private val title = Paint().apply { color = Color.parseColor("#2E5E1E"); textSize = 20f; typeface = Typeface.DEFAULT_BOLD }
    private val h2 = Paint().apply { color = Color.parseColor("#2E5E1E"); textSize = 14f; typeface = Typeface.DEFAULT_BOLD }
    private val normal = Paint().apply { color = Color.DKGRAY; textSize = 10f }
    private val bold = Paint().apply { color = Color.BLACK; textSize = 10f; typeface = Typeface.DEFAULT_BOLD }
    private val light = Paint().apply { color = Color.GRAY; textSize = 9f }
    private val line = Paint().apply { color = Color.parseColor("#DDDDDD"); strokeWidth = 0.8f }
    private val headerBg = Paint().apply { color = Color.parseColor("#EAF2E3") }

    private fun beginNewPage(): android.graphics.Canvas {
        val info = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        page = doc.startPage(info)
        return page!!.canvas
    }

    private fun ensure(space: Float) {
        if (y + space > bottom) {
            doc.finishPage(page)
            pageNumber++
            canvas = beginNewPage()
            y = margin
        }
    }

    fun title(text: String) { ensure(28f); canvas.drawText(text, margin, y + 16f, title); y += 30f }
    fun subtitle(text: String) { ensure(16f); canvas.drawText(text, margin, y + 11f, light); y += 20f }

    fun section(text: String) {
        y += 8f
        ensure(20f)
        canvas.drawText(text, margin, y + 12f, h2); y += 22f
    }

    fun spacer(h: Float = 8f) { y += h }

    fun keyValue(key: String, value: String) {
        ensure(16f)
        canvas.drawText(key, margin, y + 11f, normal)
        canvas.drawText(value, pageWidth - margin - bold.measureText(value), y + 11f, bold)
        y += 16f
    }

    /**
     * Disegna una tabella. [widths] sono pesi relativi delle colonne.
     * [right] indica quali colonne allineare a destra (numeri).
     */
    fun table(headers: List<String>, rows: List<List<String>>, widths: List<Float>, right: Set<Int> = emptySet()) {
        val usable = pageWidth - 2 * margin
        val totalW = widths.sum()
        val cols = widths.map { it / totalW * usable }
        fun drawRow(cells: List<String>, paint: Paint, bg: Boolean) {
            ensure(18f)
            if (bg) canvas.drawRect(margin, y, pageWidth - margin, y + 18f, headerBg)
            var x = margin
            cells.forEachIndexed { i, c ->
                val w = cols[i]
                val text = ellipsize(c, paint, w - 8f)
                val tx = if (right.contains(i)) x + w - 4f - paint.measureText(text) else x + 4f
                canvas.drawText(text, tx, y + 13f, paint)
                x += w
            }
            canvas.drawLine(margin, y + 18f, pageWidth - margin, y + 18f, line)
            y += 18f
        }
        drawRow(headers, bold, true)
        rows.forEach { drawRow(it, normal, false) }
    }

    fun totalsRow(label: String, value: String) {
        ensure(20f)
        canvas.drawLine(margin, y, pageWidth - margin, y, line)
        y += 4f
        canvas.drawText(label, margin, y + 12f, bold)
        canvas.drawText(value, pageWidth - margin - h2.measureText(value), y + 12f, h2)
        y += 20f
    }

    /** Grafico a barre semplice entrate/uscite per anno. */
    fun barChart(labels: List<String>, entrate: List<Double>, uscite: List<Double>) {
        val h = 180f
        ensure(h + 30f)
        val usable = pageWidth - 2 * margin
        val maxVal = (entrate + uscite).maxOrNull()?.takeIf { it > 0 } ?: 1.0
        val baseline = y + h
        val slot = usable / labels.size.coerceAtLeast(1)
        val barW = (slot / 3f)
        val green = Paint().apply { color = Color.parseColor("#2E7D32") }
        val red = Paint().apply { color = Color.parseColor("#C62828") }
        canvas.drawLine(margin, baseline, pageWidth - margin, baseline, line)
        labels.forEachIndexed { i, lab ->
            val cx = margin + slot * i + slot / 2f
            val eH = (entrate[i] / maxVal * (h - 10)).toFloat()
            val uH = (uscite[i] / maxVal * (h - 10)).toFloat()
            canvas.drawRect(cx - barW - 2f, baseline - eH, cx - 2f, baseline, green)
            canvas.drawRect(cx + 2f, baseline - uH, cx + barW + 2f, baseline, red)
            canvas.drawText(lab, cx - light.measureText(lab) / 2f, baseline + 12f, light)
        }
        y = baseline + 20f
        // Legenda
        canvas.drawRect(margin, y, margin + 10f, y + 10f, green)
        canvas.drawText("Entrate", margin + 14f, y + 9f, normal)
        val ux = margin + 90f
        canvas.drawRect(ux, y, ux + 10f, y + 10f, red)
        canvas.drawText("Uscite", ux + 14f, y + 9f, normal)
        y += 22f
    }

    private fun ellipsize(text: String, paint: Paint, maxWidth: Float): String {
        if (paint.measureText(text) <= maxWidth) return text
        var t = text
        while (t.isNotEmpty() && paint.measureText("$t…") > maxWidth) t = t.dropLast(1)
        return "$t…"
    }

    fun finish(): PdfDocument {
        doc.finishPage(page)
        return doc
    }
}
