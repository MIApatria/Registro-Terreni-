package com.miapatria.registroterreni.data.repo

import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings
import com.google.firebase.firestore.Query
import com.miapatria.registroterreni.data.model.CategoriaExtra
import com.miapatria.registroterreni.data.model.Esecutori
import com.miapatria.registroterreni.data.model.FondoMovimento
import com.miapatria.registroterreni.data.model.Raccolto
import com.miapatria.registroterreni.data.model.Spesa
import com.miapatria.registroterreni.data.model.Terreno
import kotlinx.coroutines.flow.MutableStateFlow

enum class SyncStatus { CONNECTING, ONLINE, OFFLINE, ERROR }

/**
 * Repository dei dati su Cloud Firestore. Tutti gli utenti autenticati condividono
 * gli stessi dati (accesso paritario), memorizzati in collezioni radice. Firestore
 * mantiene una cache locale: l'app funziona anche offline e si sincronizza al
 * ripristino della connessione.
 */
class AppRepository(app: FirebaseApp) {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(app).apply {
        firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
            .build()
    }

    val terreni = MutableStateFlow<List<Terreno>>(emptyList())
    val spese = MutableStateFlow<List<Spesa>>(emptyList())
    val raccolti = MutableStateFlow<List<Raccolto>>(emptyList())
    val categorie = MutableStateFlow<List<CategoriaExtra>>(emptyList())
    val fondo = MutableStateFlow<List<FondoMovimento>>(emptyList())
    val status = MutableStateFlow(SyncStatus.CONNECTING)

    private fun terreniCol() = db.collection("terreni")
    private fun speseCol() = db.collection("spese")
    private fun raccoltiCol() = db.collection("raccolti")
    private fun categorieCol() = db.collection("categorie")
    private fun fondoCol() = db.collection("fondo")

    fun start() {
        terreniCol().orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snap, err ->
                if (err != null) { status.value = SyncStatus.ERROR; return@addSnapshotListener }
                if (snap != null) {
                    terreni.value = snap.toObjects(Terreno::class.java)
                    status.value = if (snap.metadata.isFromCache) SyncStatus.OFFLINE else SyncStatus.ONLINE
                }
            }
        speseCol().addSnapshotListener { snap, _ ->
            if (snap != null) spese.value = snap.toObjects(Spesa::class.java)
        }
        raccoltiCol().addSnapshotListener { snap, _ ->
            if (snap != null) raccolti.value = snap.toObjects(Raccolto::class.java)
        }
        categorieCol().orderBy("nome").addSnapshotListener { snap, _ ->
            if (snap != null) categorie.value = snap.toObjects(CategoriaExtra::class.java)
        }
        fondoCol().addSnapshotListener { snap, _ ->
            if (snap != null) fondo.value = snap.toObjects(FondoMovimento::class.java)
        }
    }

    // ---- Terreni ----
    fun addTerreno(nome: String) = terreniCol().add(Terreno(nome = nome.trim()))
    fun renameTerreno(t: Terreno, nome: String) = terreniCol().document(t.id).update("nome", nome.trim())
    fun deleteTerreno(t: Terreno) {
        terreniCol().document(t.id).delete()
        spese.value.filter { it.terrenoId == t.id }.forEach { speseCol().document(it.id).delete() }
        raccolti.value.filter { it.terrenoId == t.id }.forEach { raccoltiCol().document(it.id).delete() }
    }

    // ---- Spese ----
    fun saveSpesa(s: Spesa) {
        if (s.id.isBlank()) speseCol().add(s.copy(id = "")) else speseCol().document(s.id).set(s)
    }
    fun deleteSpesa(s: Spesa) { if (s.id.isNotBlank()) speseCol().document(s.id).delete() }

    // ---- Raccolti ----
    fun saveRaccolto(r: Raccolto) {
        if (r.id.isBlank()) raccoltiCol().add(r.copy(id = "")) else raccoltiCol().document(r.id).set(r)
    }
    fun deleteRaccolto(r: Raccolto) { if (r.id.isNotBlank()) raccoltiCol().document(r.id).delete() }
    fun prossimaStaccata(terrenoId: String): Int =
        (raccolti.value.filter { it.terrenoId == terrenoId }.maxOfOrNull { it.numero } ?: 0) + 1

    // ---- Categorie personalizzate ----
    fun addCategoria(nome: String) {
        val n = nome.trim().uppercase()
        if (n.isNotBlank() && categorie.value.none { it.nome == n }) {
            categorieCol().add(CategoriaExtra(nome = n))
        }
    }
    fun deleteCategoria(c: CategoriaExtra) { if (c.id.isNotBlank()) categorieCol().document(c.id).delete() }

    // ---- Fondo cassa ----
    fun saveFondo(m: FondoMovimento) {
        if (m.id.isBlank()) fondoCol().add(m.copy(id = "")) else fondoCol().document(m.id).set(m)
    }
    fun deleteFondo(m: FondoMovimento) { if (m.id.isNotBlank()) fondoCol().document(m.id).delete() }

    /** Saldo del fondo: versamenti − spese pagate con il fondo cassa. */
    fun saldoFondo(): Double {
        val versamenti = fondo.value.sumOf { it.importo }
        val addebiti = spese.value.filter { it.esecutore == Esecutori.FONDOCASSA }.sumOf { it.importo }
        return versamenti - addebiti
    }
}
