# 🌿 Registro Terreni

App Android nativa per gestire i **terreni agricoli**: spese, raccolti, fondo cassa e
flussi di cassa, con **sincronizzazione in tempo reale** tra i telefoni dei tre
proprietari (Salvatore, Antonio, Francesco).

Costruita in **Kotlin + Jetpack Compose (Material 3)**, dati su **Cloud Firestore**
(funziona anche offline), esportazioni in **PDF e CSV** condivisibili via WhatsApp.

---

## 📲 Come ottenere l'APK

Ogni push su GitHub genera automaticamente l'APK:

1. Apri la scheda **Actions** del repository.
2. Apri l'ultima esecuzione **“Build APK”** (verde).
3. In fondo, scarica l'artefatto **`registro-terreni-debug-apk`**.
4. Estrai lo `.apk` e invialo via WhatsApp ai tre telefoni. Su ogni telefono:
   attiva *«Installa app sconosciute»* per WhatsApp e apri il file.

---

## ☁️ Configurazione Firebase (una volta sola, ~10 minuti)

I dati sono condivisi tramite un progetto **Firebase gratuito**. Va creato **una
volta** e i suoi 3 valori vanno inseriti su **ogni** telefono.

1. Vai su **console.firebase.google.com** → **Crea un progetto** (piano Spark, gratis).
2. **Aggiungi un'app Android** — come *nome pacchetto* usa
   `com.miapatria.registroterreni`.
3. Menu **Build → Firestore Database → Crea database** (modalità produzione va bene).
4. Menu **Build → Authentication → Inizia → Email/Password → Abilita**.
5. **Impostazioni progetto** (⚙️) → sezione **Le tue app**: copia questi 3 valori
   e inseriscili nella schermata iniziale dell'app:

   | Nell'app | In Firebase |
   |---|---|
   | **ID progetto** | *ID progetto* / `project_id` |
   | **ID app** | *ID app* / `App ID` (es. `1:1234567890:android:abcdef`) |
   | **Chiave API Web** | *Chiave API web* / `apiKey` |

6. Incolla nel Firestore le regole di sicurezza del file [`firestore.rules`](firestore.rules)
   (**Firestore → Regole → Pubblica**). Consentono ai soli utenti autenticati di
   leggere/scrivere.
7. Ogni proprietario, al primo avvio, tocca **«Registrati»** e crea il proprio
   accesso email/password. Da quel momento vedono e modificano gli **stessi dati**.

> I 3 valori Firebase **non sono segreti critici**: sono identificatori pubblici del
> progetto. La protezione dei dati è garantita dal login e dalle regole Firestore.
> Per questo l'APK può essere compilato pubblicamente senza inserire alcun file
> `google-services.json`.

---

## 📖 Guida utente

- **Terreni** — aggiungi un terreno con un nome libero; tocca un terreno per aprirne
  il registro (schede **Spese** e **Raccolti**). Menu ⋮ per rinominare o eliminare.
- **Spese** — voci predefinite (Potatura, Scocchiatura, Acquedotto, Motorista,
  Concimi, Veleni, Tasse, Carburante, Garage, Altre) + **Aggiungi** per crearne di
  nuove. Ogni spesa: importo €, **saldato/non saldato**, data, **chi ha pagato**
  (Salvatore/Antonio/Francesco/Fondocassa) e note. Filtri per anno, voce, esecutore,
  stato e ricerca nelle note. Esporta PDF/CSV.
- **Raccolti** — organizzati in **staccate** numerate; ogni staccata può avere più
  **linee** (kg × €/kg = totale). Totali kg/€ e grafico *staccate per anno*. Esporta
  PDF/CSV.
- **Fondo cassa** — aggiungi denaro al fondo; le spese pagate con **Fondocassa**
  vengono **scalate automaticamente** dal saldo. Storico e PDF.
- **Flussi di cassa** — entrate totali, uscite totali, utile/perdita, **grafico
  Entrate vs Uscite per anno** e PDF degli anni selezionati.

Tutti i dati si sincronizzano appena il telefono è online; offline si usano gli
ultimi dati salvati.

---

## 🛠️ Sviluppo

```bash
./gradlew assembleDebug   # APK in app/build/outputs/apk/debug/
```

Stack: Kotlin 1.9 · AGP 8.5 · Compose BOM 2024.09 · Firebase BOM 33 (Firestore + Auth,
inizializzati a runtime). Nessuna libreria esterna per grafici o PDF: sono disegnati
con `Canvas` e `android.graphics.pdf`.
