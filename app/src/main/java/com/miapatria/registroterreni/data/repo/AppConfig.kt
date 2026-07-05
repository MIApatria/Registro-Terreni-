package com.miapatria.registroterreni.data.repo

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "registro_config")

/** Credenziali del progetto Firebase salvate sul dispositivo. */
data class Config(
    val projectId: String = "",
    val appId: String = "",
    val apiKey: String = ""
) {
    val isReady: Boolean
        get() = projectId.isNotBlank() && appId.isNotBlank() && apiKey.isNotBlank()
}

class AppConfigStore(private val context: Context) {

    private object Keys {
        val PROJECT_ID = stringPreferencesKey("project_id")
        val APP_ID = stringPreferencesKey("app_id")
        val API_KEY = stringPreferencesKey("api_key")
    }

    val config: Flow<Config> = context.dataStore.data.map { p ->
        Config(
            projectId = p[Keys.PROJECT_ID] ?: "",
            appId = p[Keys.APP_ID] ?: "",
            apiKey = p[Keys.API_KEY] ?: ""
        )
    }

    suspend fun save(config: Config) {
        context.dataStore.edit { p ->
            p[Keys.PROJECT_ID] = config.projectId.trim()
            p[Keys.APP_ID] = config.appId.trim()
            p[Keys.API_KEY] = config.apiKey.trim()
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
