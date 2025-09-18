# [ITA] ⚽ Scoreboard

Scoreboard è un'app Android costruita in **Kotlin** con architettura **Single Activity**, progettata per gestire un tabellone segnapunti interattivo per partite di calcio con esperienza realistica di match.

---

## 🚀 Funzionalità

- ⏱️ **Cronometro personalizzabile** con durate configurabili (15, 10, 5 min)
- 🔊 **Audio immersivo** con suoni della folla e fischietti dell'arbitro
- ⚽ **Gestione goal** con feedback visivo e sonoro
- ⏰ **Tempi supplementari** e calci di rigore automatici
- 🏠 **Fattore casa** per simulare il vantaggio del pubblico
- 📱 **UI landscape** ottimizzata per tablet e dispositivi orizzontali
- 🎮 **Controlli touch** intuitivi con feedback aptico

---

## 🧠 Architettura

L'app segue il pattern **Single Activity** con navigazione via Intent e gestione stato tramite timer personalizzati.

```
MainActivity ──▶ ScoreboardActivity
     │                    │
     ▼                    ▼
Configuration       Game Logic
     │                    │
     ▼                    ▼
UI Controls         SoundManager
```

### 📂 Moduli principali

- `MainActivity.kt` – Schermata di configurazione iniziale
- `ScoreboardActivity.kt` – Tabellone principale con cronometro
- `SoundManager.kt` – Gestione audio e suoni ambientali
- `ScoreboardConfiguration.kt` – Classe dati per configurazioni match

---

## 🎵 Sistema Audio

🔊 **Gestione suoni multichannel**:
- `refereeMediaPlayer` – Fischietti arbitro
- `crowdBackgroundMediaPlayer` – Sottofondo folla
- `crowdOnActionMediaPlayer` – Reazioni ai goal

🎯 **Effetti sonori**:
- Goal squadra di casa/ospite
- Fischio inizio/fine tempo
- Calci di rigore segnati/sbagliati
- Pausa/ripresa gioco

---

## ⚙️ Configurazione

✅ **Opzioni personalizzabili**:
- 🕐 Durata partita (15/10/5 minuti)
- 🏠 Nomi squadre personalizzati
- 🔊 Attivazione/disattivazione audio
- ⏰ Tempi supplementari opzionali
- 📍 Fattore casa per audio differenziato

---

## 🎮 Come funziona

1. **⚙️ Configurazione**: Imposta durata, nomi squadre e opzioni audio
2. **▶️ Avvio partita**: Tocca il cronometro per countdown di 3 secondi
3. **⚽ Segna goal**: Tocca il punteggio durante il gioco per segnare
4. **⏸️ Pausa/Play**: Tocca il timer per fermare/riprendere
5. **🏁 Fine automatica**: Gestione automatica di tempi, supplementari e rigori

---

## 🧪 Build & Deploy

✅ **Configurazioni**:
- 🐛 **Debug**: Timer 10 secondi per test rapidi
- 🚀 **Release**: Timer realistici 15/10/5 minuti
- 🔐 **Firma**: Keystore personalizzato per debug/release

```bash
./gradlew assembleDebug    # 🐛 Build debug
./gradlew assembleRelease  # 🚀 Build release
```

---

## 📲 Requisiti tecnici

- **Android SDK**: Compile 36, Target 32, Min 21
- **Kotlin**: 2.2.20
- **Gradle**: 8.13
- **UI**: Material Design Components
- **Audio**: MediaPlayer nativo Android

---

## 📂 Struttura progetto

```
app/
├── 📱 MainActivity.kt          # Configurazione iniziale
├── ⚽ ScoreboardActivity.kt    # Tabellone principale  
├── 🔊 SoundManager.kt          # Gestione audio
└── ⚙️ Configuration.kt         # Dati configurazione
```

---

## 📌 Note aggiuntive

- 🌐 **Orientamento**: Forzato landscape per esperienza ottimale
- 📳 **Feedback**: Vibrazione aptica sui controlli
- 🎯 **Performance**: Timer ottimizzati per precisione millisecondi
- 🔄 **Stato**: Gestione automatica fasi partita

---

## 👨‍💻 Autore

Realizzato da **Elia Crocetta**  
🗓️ 2024

---

# [ENG] ⚽ Scoreboard

Scoreboard is an Android app built in **Kotlin** with **Single Activity** architecture, designed to manage an interactive football match scoreboard with realistic match experience.

---

## 🚀 Features

- ⏱️ **Customizable timer** with configurable durations (15, 10, 5 min)
- 🔊 **Immersive audio** with crowd sounds and referee whistles
- ⚽ **Goal management** with visual and sound feedback
- ⏰ **Automatic extra time** and penalty shootouts
- 🏠 **Home factor** to simulate crowd advantage
- 📱 **Landscape UI** optimized for tablets and horizontal devices
- 🎮 **Intuitive touch controls** with haptic feedback

---

## 🧠 Architecture

The app follows the **Single Activity** pattern with Intent navigation and state management through custom timers.

```
MainActivity ──▶ ScoreboardActivity
     │                    │
     ▼                    ▼
Configuration       Game Logic
     │                    │
     ▼                    ▼
UI Controls         SoundManager
```

### 📂 Main Modules

- `MainActivity.kt` – Initial configuration screen
- `ScoreboardActivity.kt` – Main scoreboard with timer
- `SoundManager.kt` – Audio management and ambient sounds
- `ScoreboardConfiguration.kt` – Data class for match configurations

---

## 🎵 Audio System

🔊 **Multichannel sound management**:
- `refereeMediaPlayer` – Referee whistles
- `crowdBackgroundMediaPlayer` – Crowd background
- `crowdOnActionMediaPlayer` – Goal reactions

🎯 **Sound effects**:
- Home/away team goals
- Start/end whistle
- Penalty scored/missed
- Game pause/resume

---

## ⚙️ Configuration

✅ **Customizable options**:
- 🕐 Match duration (15/10/5 minutes)
- 🏠 Custom team names
- 🔊 Audio enable/disable
- ⏰ Optional extra time
- 📍 Home factor for differentiated audio

---

## 🎮 How it works

1. **⚙️ Setup**: Set duration, team names and audio options
2. **▶️ Match start**: Tap timer for 3-second countdown
3. **⚽ Score goals**: Tap score during play to score
4. **⏸️ Pause/Play**: Tap timer to stop/resume
5. **🏁 Auto finish**: Automatic management of halves, extra time and penalties

---

## 🧪 Build & Deploy

✅ **Configurations**:
- 🐛 **Debug**: 10-second timer for quick testing
- 🚀 **Release**: Realistic 15/10/5 minute timers
- 🔐 **Signing**: Custom keystore for debug/release

```bash
./gradlew assembleDebug    # 🐛 Debug build
./gradlew assembleRelease  # 🚀 Release build
```

---

## 📲 Technical Requirements

- **Android SDK**: Compile 36, Target 32, Min 21
- **Kotlin**: 2.2.20
- **Gradle**: 8.13
- **UI**: Material Design Components
- **Audio**: Native Android MediaPlayer

---

## 📂 Project Structure

```
app/
├── 📱 MainActivity.kt          # Initial configuration
├── ⚽ ScoreboardActivity.kt    # Main scoreboard  
├── 🔊 SoundManager.kt          # Audio management
└── ⚙️ Configuration.kt         # Configuration data
```

---

## 📌 Additional Notes

- 🌐 **Orientation**: Forced landscape for optimal experience
- 📳 **Feedback**: Haptic vibration on controls
- 🎯 **Performance**: Optimized timers for millisecond precision
- 🔄 **State**: Automatic match phase management

---

## 👨‍💻 Author

Created by **Elia Crocetta**  
🗓️ 2024