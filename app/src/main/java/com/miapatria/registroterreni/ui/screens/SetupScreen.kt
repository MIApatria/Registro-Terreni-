package com.miapatria.registroterreni.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.miapatria.registroterreni.data.repo.Config

@Composable
fun SetupScreen(
    initial: Config,
    errorMessage: String?,
    onSave: (Config) -> Unit
) {
    var projectId by remember { mutableStateOf(initial.projectId) }
    var appId by remember { mutableStateOf(initial.appId) }
    var apiKey by remember { mutableStateOf(initial.apiKey) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
    ) {
        Icon(Icons.Filled.Cloud, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 8.dp))
        Text("Collega Firebase", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(4.dp))
        Text(
            "Per sincronizzare i dati fra i tre telefoni in tempo reale, l'app usa un " +
                "database gratuito Google Firebase. Inserisci una sola volta i 3 dati del tuo " +
                "progetto (li trovi in Impostazioni progetto → Le tue app).",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            Modifier.fillMaxWidth().padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Info, null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        "  Guida rapida",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                Text(
                    "1. Vai su console.firebase.google.com e crea un progetto (gratis).\n" +
                        "2. Aggiungi un'app Android.\n" +
                        "3. In \"Firestore Database\" premi Crea database.\n" +
                        "4. In Authentication → abilita Email/Password.\n" +
                        "5. Copia qui sotto i 3 valori e usa gli STESSI su tutti i telefoni.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Field("ID progetto (project id)", projectId) { projectId = it }
        Field("ID app (App ID / mobilesdk_app_id)", appId) { appId = it }
        Field("Chiave API Web (api key)", apiKey) { apiKey = it }

        if (errorMessage != null) {
            Spacer(Modifier.height(8.dp))
            Text("Errore: $errorMessage", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onSave(Config(projectId, appId, apiKey)) },
            enabled = projectId.isNotBlank() && appId.isNotBlank() && apiKey.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Salva e continua") }
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun Field(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    )
}
