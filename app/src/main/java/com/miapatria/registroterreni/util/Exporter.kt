package com.miapatria.registroterreni.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.miapatria.registroterreni.data.model.FondoMovimento
import com.miapatria.registroterreni.data.model.Raccolto
import com.miapatria.registroterreni.data.model.Spesa
import com.miapatria.registroterreni.data.model.Terreno
import java.io.File

data class RiepilogoAnno(val anno: Int, val entrate: Double, val uscite: Double) {
    val saldo: Double get() = entrate - uscite
}

object Exporter {

    private fun outFile(context: Context, name: String): File {
        val dir = File(context.cacheDir, "exports").apply { mkdirs() }
        return File(dir, name)
    }

    private fun writePdf(context: Context, name: String, builder: PdfBuilder): File {
        val file = outFile(context, name)
        val doc = builder.finish()
        file.outputStream().use { doc.writeTo(it) }
        doc.close()
        return file
    }

    private fun csvCell(s: String): String =
        if (s.contains(';') || s.contains('"') || s.contains('\n'))
            "\"" + s.replace("\"", "\"\"") + "\"" else s

    private fun writeCsv(context: Context, name: String, header: List<String>, rows: List<List<String>>): File {
        val file = outFile(context, name)
        file.bufferedWriter().use { w ->
            w.write("\uFEFF") // BOM per Excel
            w.write(header.joinToString(";") { csvCell(it) }); w.newLine()
            rows.forEach { w.write(it.joinToString(";") { c -> csvCell(c) }); w.newLine() }
        }
        return file
    }

    // ---- SPESE ----
    fun speseReport(context: Context, sottotitolo: String, terreni: List<Terreno>, spese: List<Spesa>): File {
        val nomi = terreni.associate { it.id to it.nome }
        val ordinate = spese.sortedByDescending { it.data }
        val b = PdfBuilder()
        b.title("Estratto conto SPESE")
        b.subtitle("$sottotitolo · generato il ${formatDate(System.currentTimeMillis())}")
        b.spacer()
        if (ordinate.isEmpty()) {
            b.keyValue("Nessuna spesa", "per i filtri selezionati")
        } else {
            b.table(
                headers = listOf("Data", "Terreno", "Voce", "Chi", "Stato", "Importo"),
                rows = ordinate.map {
                    listOf(
                        formatDate(it.data), nomi[it.terrenoId] ?: "—", it.etichetta,
                        it.esecutore, if (it.saldato) "Saldato" else "Da saldare", euro(it.importo)
                    )
                },
                widths = listOf(1.0f, 1.4f, 1.5f, 1.1f, 1.0f, 1.0f),
                right = setOf(5)
            )
            b.totalsRow("TOTALE SPESE", euro(ordinate.sumOf { it.importo }))
            b.keyValue("di cui da saldare", euro(ordinate.filter { !it.saldato }.sumOf { it.importo }))
        }
        return writePdf(context, "spese.pdf", b)
    }

    fun speseCsv(context: Context, terreni: List<Terreno>, spese: List<Spesa>): File {
        val nomi = terreni.associate { it.id to it.nome }
        return writeCsv(
            context, "spese.csv",
            listOf("Data", "Terreno", "Voce", "Chi ha pagato", "Stato", "Importo", "Note"),
            spese.sortedByDescending { it.data }.map {
                listOf(
                    formatDate(it.data), nomi[it.terrenoId] ?: "", it.etichetta, it.esecutore,
                    if (it.saldato) "Saldato" else "Da saldare",
                    "%.2f".format(it.importo), it.note
                )
            }
        )
    }

    // ---- RACCOLTI ----
    fun raccoltiReport(context: Context, sottotitolo: String, terreni: List<Terreno>, raccolti: List<Raccolto>): File {
        val nomi = terreni.associate { it.id to it.nome }
        val ordinati = raccolti.sortedWith(compareBy({ it.numero }, { it.data }))
        val b = PdfBuilder()
        b.title("Estratto conto RACCOLTI")
        b.subtitle("$sottotitolo · generato il ${formatDate(System.currentTimeMillis())}")
        b.spacer()
        if (ordinati.isEmpty()) {
            b.keyValue("Nessun raccolto", "per i filtri selezionati")
        } else {
            b.table(
                headers = listOf("Data", "Terreno", "Staccata", "Kg", "€/kg", "Totale"),
                rows = ordinati.map {
                    listOf(
                        formatDate(it.data), nomi[it.terrenoId] ?: "—", "${it.numero}°",
                        kg(it.kg), euro(it.prezzoKg), euro(it.totale)
                    )
                },
                widths = listOf(1.0f, 1.5f, 1.0f, 1.0f, 0.9f, 1.1f),
                right = setOf(3, 4, 5)
            )
            b.totalsRow("TOTALE KG", kg(ordinati.sumOf { it.kg }))
            b.totalsRow("TOTALE ENTRATE", euro(ordinati.sumOf { it.totale }))
        }
        return writePdf(context, "raccolti.pdf", b)
    }

    fun raccoltiCsv(context: Context, terreni: List<Terreno>, raccolti: List<Raccolto>): File {
        val nomi = terreni.associate { it.id to it.nome }
        return writeCsv(
            context, "raccolti.csv",
            listOf("Data", "Terreno", "Staccata", "Kg", "Prezzo/kg", "Totale", "Note"),
            raccolti.sortedWith(compareBy({ it.numero }, { it.data })).map {
                listOf(
                    formatDate(it.data), nomi[it.terrenoId] ?: "", "${it.numero}",
                    "%.2f".format(it.kg), "%.2f".format(it.prezzoKg), "%.2f".format(it.totale), it.note
                )
            }
        )
    }

    // ---- FONDO CASSA ----
    fun fondoReport(context: Context, anno: Int?, saldo: Double, versamenti: List<FondoMovimento>, addebiti: List<Spesa>, terreni: List<Terreno>): File {
        val nomi = terreni.associate { it.id to it.nome }
        data class Mov(val data: Long, val descr: String, val importo: Double)
        val movimenti = (versamenti.map { Mov(it.data, "Versamento" + if (it.note.isBlank()) "" else " · ${it.note}", it.importo) } +
                addebiti.map { Mov(it.data, "Spesa · ${it.etichetta} (${nomi[it.terrenoId] ?: "—"})", -it.importo) })
            .filter { anno == null || yearOf(it.data) == anno }
            .sortedBy { it.data }
        val b = PdfBuilder()
        b.title("Fondo cassa")
        b.subtitle((anno?.let { "Anno $it" } ?: "Storico completo") + " · generato il ${formatDate(System.currentTimeMillis())}")
        b.spacer()
        b.keyValue("SALDO ATTUALE", euro(saldo))
        b.spacer()
        if (movimenti.isEmpty()) {
            b.keyValue("Nessun movimento", "")
        } else {
            b.table(
                headers = listOf("Data", "Descrizione", "Importo"),
                rows = movimenti.map { listOf(formatDate(it.data), it.descr, euro(it.importo)) },
                widths = listOf(1.0f, 3.0f, 1.1f),
                right = setOf(2)
            )
        }
        return writePdf(context, "fondo_cassa.pdf", b)
    }

    // ---- FLUSSI ----
    fun flussiReport(context: Context, riepiloghi: List<RiepilogoAnno>): File {
        val ordinati = riepiloghi.sortedBy { it.anno }
        val b = PdfBuilder()
        b.title("Flussi di cassa")
        val periodo = if (ordinati.isEmpty()) "—" else "${ordinati.first().anno} – ${ordinati.last().anno}"
        b.subtitle("Periodo $periodo · generato il ${formatDate(System.currentTimeMillis())}")
        b.section("Andamento per anno")
        b.barChart(
            labels = ordinati.map { it.anno.toString() },
            entrate = ordinati.map { it.entrate },
            uscite = ordinati.map { it.uscite }
        )
        b.section("Riepilogo")
        b.table(
            headers = listOf("Anno", "Entrate", "Uscite", "Utile/Perdita"),
            rows = ordinati.map { listOf(it.anno.toString(), euro(it.entrate), euro(it.uscite), euro(it.saldo)) },
            widths = listOf(1f, 1.4f, 1.4f, 1.5f),
            right = setOf(1, 2, 3)
        )
        b.totalsRow("ENTRATE TOTALI", euro(ordinati.sumOf { it.entrate }))
        b.totalsRow("USCITE TOTALI", euro(ordinati.sumOf { it.uscite }))
        b.totalsRow("SALDO COMPLESSIVO", euro(ordinati.sumOf { it.saldo }))
        return writePdf(context, "flussi_di_cassa.pdf", b)
    }

    /** Apre il foglio di condivisione (WhatsApp, Drive, Email, salva su File…). */
    fun share(context: Context, file: File) {
        val mime = if (file.extension == "csv") "text/csv" else "application/pdf"
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mime
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Condividi / salva").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}
