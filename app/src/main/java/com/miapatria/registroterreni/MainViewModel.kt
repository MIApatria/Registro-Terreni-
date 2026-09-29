package com.miapatria.registroterreni

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.miapatria.registroterreni.data.model.CategoriaExtra
import com.miapatria.registroterreni.data.model.FondoMovimento
import com.miapatria.registroterreni.data.model.Raccolto
import com.miapatria.registroterreni.data.model.Spesa
import com.miapatria.registroterreni.data.model.Terreno
import com.miapatria.registroterreni.data.repo.AppConfigStore
import com.miapatria.registroterreni.data.repo.AppRepository
import com.miapatria.registroterreni.data.repo.AuthManager
import com.miapatria.registroterreni.data.repo.Config
import com.miapatria.registroterreni.data.repo.FirebaseInit
import com.miapatria.registroterreni.data.repo.SyncStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val store = AppConfigStore(app)

    /** null = configurazione ancora in caricamento. */
    val config: StateFlow<Config?> =
        store.config.map { it }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val user = MutableStateFlow<FirebaseUser?>(null)
    val firebaseReady = MutableStateFlow(false)
    val initError = MutableStateFlow<String?>(null)
    val authBusy = MutableStateFlow(false)
    val authError = MutableStateFlow<String?>(null)

    val terreni = MutableStateFlow<List<Terreno>>(emptyList())
    val spese = MutableStateFlow<List<Spesa>>(emptyList())
    val raccolti = MutableStateFlow<List<Raccolto>>(emptyList())
    val categorie = MutableStateFlow<List<CategoriaExtra>>(emptyList())
    val fondo = MutableStateFlow<List<FondoMovimento>>(emptyList())
    val status = MutableStateFlow(SyncStatus.CONNECTING)

    private var auth: AuthManager? = null
    private var repo: AppRepository? = null
    private val mirrorJobs = mutableListOf<Job>()
    private var lastConfigApplied: Config? = null

    init {
        viewModelScope.launch {
            store.config.collect { cfg ->
                if (cfg.isReady && cfg != lastConfigApplied) initFirebase(cfg)
            }
        }
    }

    private fun initFirebase(cfg: Config) {
        try {
            val app = FirebaseInit.app(getApplication(), cfg) ?: return
            lastConfigApplied = cfg
            firebaseReady.value = true
            val a = AuthManager(app)
            auth = a
            viewModelScope.launch {
                a.authState.collect { u ->
                    user.value = u
                    if (u != null) startRepo(app) else stopRepo()
                }
            }
        } catch (e: Exception) {
            initError.value = e.message ?: "Errore di inizializzazione Firebase"
        }
    }

    private fun startRepo(app: com.google.firebase.FirebaseApp) {
        if (repo != null) return
        val r = AppRepository(app)
        repo = r
        r.start()
        mirrorJobs += viewModelScope.launch { r.terreni.collect { terreni.value = it } }
        mirrorJobs += viewModelScope.launch { r.spese.collect { spese.value = it } }
        mirrorJobs += viewModelScope.launch { r.raccolti.collect { raccolti.value = it } }
        mirrorJobs += viewModelScope.launch { r.categorie.collect { categorie.value = it } }
        mirrorJobs += viewModelScope.launch { r.fondo.collect { fondo.value = it } }
        mirrorJobs += viewModelScope.launch { r.status.collect { status.value = it } }
    }

    private fun stopRepo() {
        mirrorJobs.forEach { it.cancel() }
        mirrorJobs.clear()
        repo = null
        terreni.value = emptyList(); spese.value = emptyList(); raccolti.value = emptyList()
        categorie.value = emptyList(); fondo.value = emptyList()
    }

    // ---- Configurazione & autenticazione ----
    fun saveConfig(cfg: Config) = viewModelScope.launch { store.save(cfg) }

    fun login(email: String, password: String) = viewModelScope.launch {
        authBusy.value = true; authError.value = null
        try { auth?.login(email, password) }
        catch (e: Exception) { authError.value = mapAuthError(e) }
        finally { authBusy.value = false }
    }

    fun register(email: String, password: String) = viewModelScope.launch {
        authBusy.value = true; authError.value = null
        try { auth?.register(email, password) }
        catch (e: Exception) { authError.value = mapAuthError(e) }
        finally { authBusy.value = false }
    }

    fun logout() {
        stopRepo()
        auth?.logout()
    }

    fun clearConfig() = viewModelScope.launch {
        stopRepo()
        auth?.logout()
        store.clear()
    }

    private fun mapAuthError(e: Exception): String {
        val m = e.message ?: return "Errore di accesso"
        return when {
            m.contains("password is invalid", true) || m.contains("credential", true) ->
                "Email o password non corretti"
            m.contains("no user record", true) -> "Utente non trovato: registrati prima"
            m.contains("email address is already", true) -> "Questa email è già registrata"
            m.contains("badly formatted", true) -> "Email non valida"
            m.contains("at least 6", true) -> "La password deve avere almeno 6 caratteri"
            m.contains("network", true) -> "Nessuna connessione a internet"
            else -> m
        }
    }

    // ---- Deleghe dati ----
    fun addTerreno(nome: String) { repo?.addTerreno(nome) }
    fun renameTerreno(t: Terreno, nome: String) { repo?.renameTerreno(t, nome) }
    fun deleteTerreno(t: Terreno) { repo?.deleteTerreno(t) }

    fun saveSpesa(s: Spesa) { repo?.saveSpesa(s) }
    fun deleteSpesa(s: Spesa) { repo?.deleteSpesa(s) }

    fun saveRaccolto(r: Raccolto) { repo?.saveRaccolto(r) }
    fun deleteRaccolto(r: Raccolto) { repo?.deleteRaccolto(r) }
    fun prossimaStaccata(terrenoId: String): Int = repo?.prossimaStaccata(terrenoId) ?: 1

    fun addCategoria(nome: String) { repo?.addCategoria(nome) }
    fun deleteCategoria(c: CategoriaExtra) { repo?.deleteCategoria(c) }

    fun saveFondo(m: FondoMovimento) { repo?.saveFondo(m) }
    fun deleteFondo(m: FondoMovimento) { repo?.deleteFondo(m) }

    fun terrenoById(id: String): Terreno? = terreni.value.firstOrNull { it.id == id }
}
