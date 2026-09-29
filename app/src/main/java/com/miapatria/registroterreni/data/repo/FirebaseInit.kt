package com.miapatria.registroterreni.data.repo

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

/**
 * Inizializza (una sola volta) un'istanza di FirebaseApp con le credenziali
 * inserite dall'utente, così da non dover includere alcun segreto (google-services.json)
 * nel codice sorgente. Ritorna null se le credenziali non sono valide.
 */
object FirebaseInit {
    const val APP_NAME = "registro-terreni"

    fun app(context: Context, config: Config): FirebaseApp? {
        if (!config.isReady) return null
        FirebaseApp.getApps(context).firstOrNull { it.name == APP_NAME }?.let { return it }
        val options = FirebaseOptions.Builder()
            .setProjectId(config.projectId.trim())
            .setApplicationId(config.appId.trim())
            .setApiKey(config.apiKey.trim())
            .build()
        return FirebaseApp.initializeApp(context, options, APP_NAME)
    }
}
