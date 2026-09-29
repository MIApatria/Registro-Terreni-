package com.miapatria.registroterreni.data.repo

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/** Gestisce l'accesso email/password con Firebase Authentication. */
class AuthManager(app: FirebaseApp) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance(app)

    val currentUser: FirebaseUser? get() = auth.currentUser

    /** Emette l'utente corrente ad ogni cambio di stato (login/logout). */
    val authState: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email.trim(), password).await()
    }

    suspend fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email.trim(), password).await()
    }

    fun logout() = auth.signOut()
}
