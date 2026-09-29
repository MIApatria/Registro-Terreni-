package com.miapatria.registroterreni.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val ITALY = Locale("it", "IT")
private val currencyFmt: NumberFormat = NumberFormat.getCurrencyInstance(ITALY)
private val numberFmt: NumberFormat = NumberFormat.getNumberInstance(ITALY).apply {
    minimumFractionDigits = 0
    maximumFractionDigits = 2
}
private val dateFmt = SimpleDateFormat("dd/MM/yyyy", ITALY)

fun euro(value: Double): String = currencyFmt.format(value)

fun kg(value: Double): String = numberFmt.format(value) + " kg"

fun formatDate(millis: Long): String = dateFmt.format(Date(millis))

fun yearOf(millis: Long): Int {
    val c = Calendar.getInstance()
    c.timeInMillis = millis
    return c.get(Calendar.YEAR)
}

fun startOfYear(year: Int): Long = Calendar.getInstance().apply {
    clear(); set(Calendar.YEAR, year)
}.timeInMillis

fun endOfYear(year: Int): Long = Calendar.getInstance().apply {
    clear(); set(Calendar.YEAR, year + 1)
}.timeInMillis - 1

/** Interpreta un numero digitato dall'utente accettando sia la virgola sia il punto. */
fun parseDecimal(text: String): Double {
    val t = text.trim()
    // Con la virgola: stile italiano (punto = migliaia, virgola = decimali).
    val normalized = if (t.contains(',')) t.replace(".", "").replace(',', '.') else t
    return normalized.toDoubleOrNull() ?: 0.0
}
