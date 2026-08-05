package com.francesco.crono.data

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap

/**
 * Persiste lo stato delle spunte in SharedPreferences, così le caselle
 * spuntate restano tali anche dopo aver chiuso l'app.
 *
 * Usato sia per la checklist di partenza sia per lo spuntare le singole
 * tappe di ogni giornata (chiave = "day{n}:{indice}").
 */
class CheckStore(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("crono_checks", Context.MODE_PRIVATE)

    private val state: SnapshotStateMap<String, Boolean> = mutableStateMapOf<String, Boolean>().apply {
        prefs.all.forEach { (k, v) -> if (v is Boolean) put(k, v) }
    }

    fun isChecked(key: String): Boolean = state[key] ?: false

    fun toggle(key: String) {
        val newValue = !(state[key] ?: false)
        state[key] = newValue
        prefs.edit().putBoolean(key, newValue).apply()
    }

    /** Numero di elementi spuntati tra le chiavi indicate. */
    fun countChecked(keys: List<String>): Int = keys.count { state[it] == true }
}
