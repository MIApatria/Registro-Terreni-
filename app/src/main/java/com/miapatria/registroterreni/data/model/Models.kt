package com.miapatria.registroterreni.data.model

import com.google.firebase.firestore.DocumentId

/** Un terreno / appezzamento con un nome scelto dall'utente. */
data class Terreno(
    @DocumentId var id: String = "",
    var nome: String = "",
    var createdAt: Long = System.currentTimeMillis()
)

/** Una voce di spesa collegata a un terreno. */
data class Spesa(
    @DocumentId var id: String = "",
    var terrenoId: String = "",
    var categoria: String = "",
    /** Tipologia specificata dall'utente quando la categoria è "ALTRE". */
    var sottocategoria: String = "",
    /** Chi ha effettuato la spesa (vedi [Esecutori]). */
    var esecutore: String = "",
    var note: String = "",
    var importo: Double = 0.0,
    var saldato: Boolean = false,
    var data: Long = System.currentTimeMillis()
) {
    val etichetta: String
        get() = if (categoria == Categorie.ALTRE && sottocategoria.isNotBlank())
            "ALTRE · $sottocategoria" else categoria
}

/**
 * Una linea di raccolta appartenente a una "staccata". Ogni staccata (numerata)
 * può contenere più linee (prodotti/date diverse). L'importo è kg × prezzo/kg.
 */
data class Raccolto(
    @DocumentId var id: String = "",
    var terrenoId: String = "",
    /** Numero progressivo della staccata (1° staccata, 2° staccata, …). */
    var numero: Int = 1,
    var kg: Double = 0.0,
    var prezzoKg: Double = 0.0,
    var note: String = "",
    var data: Long = System.currentTimeMillis()
) {
    val totale: Double get() = kg * prezzoKg
}

/** Categoria di spesa personalizzata aggiunta dall'utente tramite "AGGIUNGI". */
data class CategoriaExtra(
    @DocumentId var id: String = "",
    var nome: String = ""
)

/** Un movimento del fondo cassa: versamento (importo positivo) di denaro. */
data class FondoMovimento(
    @DocumentId var id: String = "",
    var importo: Double = 0.0,
    var note: String = "",
    var data: Long = System.currentTimeMillis()
)

/** Categorie di spesa predefinite. */
object Categorie {
    const val POTATURA = "POTATURA"
    const val SCOCCHIATURA = "SCOCCHIATURA"
    const val ACQUEDOTTO = "ACQUEDOTTO"
    const val MOTORISTA = "MOTORISTA"
    const val CONCIMI = "CONCIMI"
    const val VELENI = "VELENI"
    const val TASSE = "TASSE"
    const val CARBURANTE = "CARBURANTE"
    const val GARAGE = "GARAGE"
    const val ALTRE = "ALTRE"

    val PREDEFINITE = listOf(
        POTATURA, SCOCCHIATURA, ACQUEDOTTO, MOTORISTA,
        CONCIMI, VELENI, TASSE, CARBURANTE, GARAGE, ALTRE
    )
}

/** Chi ha effettuato una spesa. FONDOCASSA scala l'importo dal fondo cassa. */
object Esecutori {
    const val SALVATORE = "SALVATORE"
    const val ANTONIO = "ANTONIO"
    const val FRANCESCO = "FRANCESCO"
    const val FONDOCASSA = "FONDOCASSA"

    val TUTTI = listOf(SALVATORE, ANTONIO, FRANCESCO, FONDOCASSA)
}
