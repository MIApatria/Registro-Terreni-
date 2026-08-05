# Cronoprogramma USA 2026 — App Android

App Android nativa (Kotlin + Jetpack Compose) con il **Road Trip USA 2026**
(West Coast: California, Nevada, Arizona, Utah — 13 giorni).

## Cosa fa

- **Dashboard**: una griglia di caselle, **3 per riga** e poi in verticale.
  Ogni casella mostra il **giorno**, la **data** e la **città/località**.
- **Tocca una casella** → si apre l'intero programma di quella giornata
  (tratte in auto, timeline oraria dettagliata, note e biglietti fissi).
- **Indietro**: si torna alla dashboard con il **tasto ◁ del telefono**.
- **Checklist "prima di partire"** spuntabile: le spunte restano salvate
  anche chiudendo l'app.
- In più: ogni tappa della giornata si può **spuntare** durante il viaggio,
  i **numeri di telefono/email** sono cliccabili e le tratte hanno un
  pulsante **Mappa** che apre Google Maps.

## Come ottenere l'APK da installare sul telefono

Il codice si compila automaticamente su GitHub Actions (il workflow
`.github/workflows/build-apk.yml`):

1. Ad ogni push sul branch, GitHub costruisce l'APK.
2. Vai nella sezione **Releases** del repository → release **`latest`**.
3. Scarica dal telefono `USA-2026-cronoprogramma.apk`, aprilo e consenti
   l'installazione da questa fonte.

In alternativa l'APK è disponibile come **artifact** della run del workflow
(scheda *Actions*).

## Compilare in locale (opzionale)

Serve Android SDK + JDK 17:

```bash
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```
