package com.miapatria.registroterreni.util

import com.miapatria.registroterreni.data.model.Raccolto
import com.miapatria.registroterreni.data.model.Spesa

/** Anni per cui esistono spese o raccolti, in ordine crescente. */
fun anniDisponibili(spese: List<Spesa>, raccolti: List<Raccolto>): List<Int> {
    val anni = (spese.map { yearOf(it.data) } + raccolti.map { yearOf(it.data) }).toSortedSet()
    return anni.toList()
}

/** Riepilogo entrate/uscite per ciascun anno indicato. */
fun riepiloghiPerAnno(anni: List<Int>, spese: List<Spesa>, raccolti: List<Raccolto>): List<RiepilogoAnno> =
    anni.sorted().map { anno ->
        RiepilogoAnno(
            anno = anno,
            entrate = raccolti.filter { yearOf(it.data) == anno }.sumOf { it.totale },
            uscite = spese.filter { yearOf(it.data) == anno }.sumOf { it.importo }
        )
    }
