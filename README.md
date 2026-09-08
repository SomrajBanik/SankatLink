# SankatLink (संकटलिंक)
# SankatLink (संकटलिंक)
> **Offline, Low-Bandwidth, Multilingual Emergency Voice Communication System**  
> *Developed for Smart India Hackathon (SIH) & Disaster Response Scenarios*

---

## Overview

In the aftermath of floods, earthquakes, cyclones, or collapsed infrastructure, cellular towers and internet service are often destroyed or severed. First responders and stranded civilians speak different regional languages, creating dangerous communication barriers.

**SankatLink** solves this through a zero-internet, on-device pipeline that combines:
1. **On-Device Voice Activity Detection (VAD)** (Silero VAD ONNX)
2. **On-Device Speech-to-Text (STT)** (AI4Bharat IndicConformer CTC via Sherpa-ONNX)
3. **On-Device Machine Translation (MT)** (AI4Bharat IndicTrans2 distilled ONNX)
4. **Ultra-Low Bandwidth P2P Transport** (Wi-Fi Direct & BLE Mesh)
5. **On-Device Text-to-Speech (TTS)** (Sherpa-ONNX VITS / Piper / Native TTS)
6. **Emergency Loudspeaker Override** (Forces 100% MAX Volume in Emergency Voice Note mode)

---

## Bandwidth Saving Architecture: Audio vs. Text Payload

Sending raw or compressed audio over ad-hoc local mesh connections (BLE/Wi-Fi Direct) is unreliable in disaster zones:
* **Raw PCM audio (10 sec)**: ~320 KB
* **Compressed Opus audio (10 sec)**: ~20 KB – 50 KB *(High packet drop rate on BLE/low-signal mesh)*
* **SankatLink Transcribed + Translated Text**: **~60 to 120 BYTES** *(A **>99.5% bandwidth reduction**)*

```
[ Sender Device A ]                                           [ Receiver Device B ]
 Microphone ──▶ Silero VAD (ONNX)                              Local P2P Receiver
                      │                                                │
                 IndicConformer                                 Extract Translated Text
                   ASR / STT                                           │
                      │                                         Render Dual Captions
                 IndicTrans2                                           │
                  Translation                                   Emergency Mode Check
                      │                                                │
            Build Text Payload (<100 B) ───[ Wi-Fi Direct / BLE ]──▶ AudioManager.STREAM_MUSIC
                                                                     FORCED TO 100% MAX VOLUME
                                                                               │
                                                                        On-Device TTS
                                                                        (Speech Out)
```

By transferring only the text payload over the air and allowing the receiving device to locally synthesize the speech via TTS, SankatLink achieves instant, resilient communication even over noisy, degraded radio links.

---

## 3 Operational Modes

### 1. 🚨 Emergency Voice Note Mode
* **Life-Safety Max Volume Override**: When an emergency distress message is received, the app automatically requests transient exclusive audio focus, triggers an emergency siren haptic vibration, and forces system media/alarm volume to **100% MAXIMUM**.
* **One-Touch SOS Broadcast**: Pre-translated life-saving phrases ("Immediate medical assistance required!", "People trapped under rubble!", "Water level rising!") for rapid one-tap broadcast when victims are injured or cannot speak.
* **Dual-Language Message Feed**: Displays the translated text prominently, with original native text and transmission footprint metadata (e.g., `📦 72 Bytes via BLE`).

### 2. 📻 Walkie-Talkie (PTT) Mode
* **Tactile Push-To-Talk**: Large central PTT button with haptic feedback and dynamic audio waveform visualizer.
* **Live Silero VAD**: Automatically detects human speech vs. silence/background noise, minimizing latency.
* **Auto-Play on Loudspeaker**: Receiver immediately synthesizes and announces incoming transmissions through the loudspeaker without requiring screen taps.
* **Multi-Channel Mesh**: Dedicated channels for Emergency SOS, Medical Dispatch, and Evacuation Logistics.

### 3. 📞 Phone Call Mode
* **Near-Synchronous Voice Over Text-Sync**: Simulates a direct walkie-talkie style phone call over local Wi-Fi Direct/BLE.
* **Live Bidirectional Subtitles**: Shows real-time translated subtitles for both speakers side-by-side.
* **Hands-Free In-Call Controls**: Speakerphone toggle, microphone mute, and call duration timer.

---

## 10 Supported Indian Languages

SankatLink natively supports the 10 most widely spoken Indian languages (+ English):

| # | Language | Native Script | ISO Code | Script |
|---|---|---|---|---|
| 1 | **Hindi** | हिन्दी | `hi` | Devanagari |
| 2 | **Bengali** | বাংলা | `bn` | Bengali |
| 3 | **Tamil** | தமிழ் | `ta` | Tamil |
| 4 | **Telugu** | తెలుగు | `te` | Telugu |
| 5 | **Marathi** | मराठी | `mr` | Devanagari |
| 6 | **Gujarati** | ગુજરાતી | `gu` | Gujarati |
| 7 | **Kannada** | ಕನ್ನಡ | `kn` | Kannada |
| 8 | **Malayalam** | മലയാളം | `ml` | Malayalam |
| 9 | **Odia** | ଓଡ଼ିଆ | `or` | Odia |
| 10 | **Punjabi** | ਪੰਜਾਬੀ | `pa` | Gurmukhi |
| + | **English** | English | `en` | Latin |

---

## 100% Open-Source On-Device Model Recommendations

| Pipeline Stage | Recommended Model | Quantized Size | Inference Runtime | License |
|---|---|---|---|---|
| **VAD** | **Silero VAD v5** | ~2.2 MB | ONNX Runtime Android / Sherpa-ONNX | MIT |
| **STT / ASR** | **AI4Bharat IndicConformer CTC** | ~45 MB – 80 MB | `com.k2fsa.sherpa.onnx` Android AAR | MIT / CC-BY |
| **Translation** | **AI4Bharat IndicTrans2 (Distilled)** | ~180 MB – 260 MB | ONNX Runtime (int8) | MIT / CC-BY-4.0 |
| **TTS** | **Sherpa-ONNX VITS / Piper** | ~25 MB / lang | Sherpa-ONNX `OfflineTts` / Android TTS | MIT / Apache 2.0 |

---

## Implementation Status

> ⚠️ **This is currently a UI prototype / demo.** The AI pipeline stages below are **planned** and have interface stubs in place, but the actual ONNX model inference is not yet implemented.

| Pipeline Stage | Status | Notes |
|---|---|---|
| **VAD** (Silero VAD ONNX) | 🔲 Stub only | `ai/vad/VoiceActivityDetector.kt` interface defined |
| **STT** (IndicConformer CTC) | 🔲 Stub only | `ai/stt/SpeechToTextEngine.kt` interface defined |
| **Translation** (IndicTrans2) | 🔲 Stub only | `ai/translation/TranslationEngine.kt` interface defined |
| **TTS** (Sherpa-ONNX VITS) | 🔲 Stub only | `ai/tts/TextToSpeechEngine.kt` interface defined |
| **Wi-Fi Direct / BLE Mesh** | 🔲 Stub only | `network/MeshNetworkManager.kt` interface defined |
| **Emergency Volume Override** | ✅ Implemented | `audio/EmergencyAudioController.kt` — real Android AudioManager code |
| **SOS Phrase Translations** | ✅ Implemented | `model/IndianLanguage.kt` — hardcoded offline phrases for all 10 languages |
| **UI Screens** | ✅ Implemented | All 3 modes: Emergency Voice Note, Walkie-Talkie, Phone Call |

---

## Project Structure

```
sankat_link/
├── .gitignore                     # Standard Android Git exclusion rules
├── build.gradle.kts               # Root build script
├── settings.gradle.kts            # Project repositories & modules
├── gradle.properties              # JVM & AndroidX optimization settings
├── local.properties               # Android SDK path configuration
├── gradlew / gradlew.bat          # Gradle 9.1 wrapper binaries
├── gradle/
│   └── libs.versions.toml         # Centralized dependency catalog
└── app/
    ├── build.gradle.kts           # Module dependencies (Compose, Material 3, Coroutines)
    └── src/main/
        ├── AndroidManifest.xml    # Permissions (Audio, Volume Override, BLE, Wi-Fi Direct)
        ├── res/                   # Drawables, themes, and string resources
        └── java/com/sih/sankatlink/
            ├── SankatLinkApp.kt
            ├── MainActivity.kt
            ├── audio/
            │   └── EmergencyAudioController.kt   # Forces 100% max volume & sirens
            ├── model/
            │
            ├── ai/                              # AI pipeline layer (stubs — ready for ONNX integration)
            │   ├── vad/
            │   │   └── VoiceActivityDetector.kt  # Silero VAD v5 ONNX interface
            │   ├── stt/
            │   │   └── SpeechToTextEngine.kt     # IndicConformer CTC (Sherpa-ONNX) interface
            │   ├── tts/
            │   │   └── TextToSpeechEngine.kt     # VITS / Piper (Sherpa-ONNX) interface
            │   └── translation/
            │       └── TranslationEngine.kt      # IndicTrans2 ONNX interface
            │
            ├── network/                          # P2P mesh transport layer (stub)
            │   └── MeshNetworkManager.kt         # Wi-Fi Direct & BLE Mesh interface
            │
            ├── audio/                            # ✅ Real, working audio utilities
            │   └── EmergencyAudioController.kt  # Forces 100% max volume & sirens
            │
            ├── model/                            # Data models & offline SOS phrase data
            │   ├── AppMode.kt                   # Voice Note / Walkie-Talkie / Phone Call
            │   ├── AudioMessage.kt              # Text packet payload model
            │   ├── IndianLanguage.kt            # 10 Indian languages + SOS phrasebook
            │   ├── IndianLanguage.kt            # ✅ 10 Indian languages + SOS phrasebook
            │   └── PeerDevice.kt                # Discovered Wi-Fi Direct & BLE peers
            ├── theme/
            │   ├── Color.kt                     # Tactical emergency dark theme palette
            │   ├── Theme.kt                     # Material 3 dark color scheme
            │   └── Type.kt                      # Typography scales
            ├── ui/
            │   ├── components/
            │
            ├── ui/                              # UI layer (Jetpack Compose)
            │   ├── theme/                       # Material 3 dark tactical theme
            │   │   ├── Color.kt                 # Emergency dark palette
            │   │   ├── Theme.kt                 # Material 3 dark color scheme
            │   │   └── Type.kt                  # Typography scales
            │   ├── components/                  # Reusable composables
            │   │   ├── ConnectionStatusBar.kt   # P2P mesh status & bandwidth stats
            │   │   ├── EmergencyVolumeBanner.kt # 100% volume warning & siren tester
            │   │   ├── LanguageSelectorDialog.kt# 10-language selection modal sheet
            │   │   └── WaveformVisualizer.kt    # Live audio pulse animation
            │   └── screens/
            │   └── screens/                     # Full-screen composables
            │       ├── MainEmergencyScreen.kt   # Coordinator layout & mode tabs
            │       ├── VoiceNoteScreen.kt       # Voice note feed, SOS chips, max volume playback
            │       ├── WalkieTalkieScreen.kt    # Tactile PTT button & Silero VAD monitor
            │       ├── WalkieTalkieScreen.kt    # Tactile PTT button & VAD status monitor
            │       └── PhoneModeScreen.kt       # Turn-based call with live translated subtitles
            │
            └── viewmodel/
                └── SankatLinkViewModel.kt     # State machine, pipeline simulation & audio control
                └── SankatLinkViewModel.kt     # State machine & audio control orchestration
```

---

## How to Build and Run

1. **Prerequisites**:
   * Android Studio Ladybug / Meerkat or Android CLI
   * JDK 17 or JDK 21
   * Android SDK (API 26 to 36)

2. **Build via Command Line**:
   ```bash
   ./gradlew assembleDebug
   ```

3. **Install on Device via ADB**:
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

---

## License

This project is open-source and licensed under the **Apache License 2.0 / MIT License**.
All underlying recommended AI models (AI4Bharat IndicConformer, IndicTrans2, Silero VAD, Piper TTS) are 100% open source.
