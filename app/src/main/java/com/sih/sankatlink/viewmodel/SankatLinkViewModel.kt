package com.sih.sankatlink.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sih.sankatlink.audio.EmergencyAudioController
import com.sih.sankatlink.model.AppMode
import com.sih.sankatlink.model.AudioMessage
import com.sih.sankatlink.model.EmergencyPhrase
import com.sih.sankatlink.model.IndianLanguage
import com.sih.sankatlink.model.PeerDevice
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class SankatLinkUiState(
    val activeMode: AppMode = AppMode.EMERGENCY_VOICE_NOTE,
    val sourceLang: IndianLanguage = IndianLanguage.HINDI,
    val targetLang: IndianLanguage = IndianLanguage.BENGALI,
    val connectedPeers: List<PeerDevice> = emptyList(),
    val messages: List<AudioMessage> = emptyList(),
    val isPttPressed: Boolean = false,
    val isRecording: Boolean = false,
    val vadSpeechActive: Boolean = false,
    val isTranscribing: Boolean = false,
    val liveTranscriptPreview: String = "",
    val currentlyPlayingMessageId: String? = null,
    val activeCallDurationSec: Int = 0,
    val isCallActive: Boolean = false,
    val isSpeakerOn: Boolean = true,
    val isMuted: Boolean = false,
    val isEmergencyVolumeActive: Boolean = true
)

class SankatLinkViewModel(application: Application) : AndroidViewModel(application) {

    private val audioController = EmergencyAudioController(application)

    private val _uiState = MutableStateFlow(SankatLinkUiState())
    val uiState: StateFlow<SankatLinkUiState> = _uiState.asStateFlow()

    private var callTimerJob: Job? = null
    private var pttJob: Job? = null

    init {
        loadInitialMockPeers()
        loadInitialEmergencyFeed()
    }

    private fun loadInitialMockPeers() {
        _uiState.update { current ->
            current.copy(
                connectedPeers = listOf(
                    PeerDevice("peer_1", "Rescue Boat Alpha (NDRF)", "Wi-Fi Direct", -48, 25f, true, true),
                    PeerDevice("peer_2", "Camp Medical Officer", "BLE Mesh", -62, 55f, true, true),
                    PeerDevice("peer_3", "Civilian Hub Sector 4", "BLE Mesh", -74, 110f, true, false)
                )
            )
        }
    }

    private fun loadInitialEmergencyFeed() {
        val sampleMsgs = listOf(
            AudioMessage(
                id = "msg_1",
                senderName = "Rescue Boat Alpha (NDRF)",
                senderId = "peer_1",
                sourceLang = IndianLanguage.BENGALI,
                targetLang = IndianLanguage.HINDI,
                originalText = "উদ্ধারকারী দল সেক্টর ৩-এ পৌঁছেছে। সবাই ছাদে জমা হন।",
                translatedText = "बचाव दल सेक्टर 3 में पहुँच गया है। सभी लोग छत पर इकट्ठा हों।",
                audioDurationSec = 3.8f,
                payloadSizeBytes = 72,
                isEmergency = true,
                isIncoming = true,
                mode = AppMode.EMERGENCY_VOICE_NOTE,
                timestamp = System.currentTimeMillis() - 120_000
            ),
            AudioMessage(
                id = "msg_2",
                senderName = "You",
                senderId = "self",
                sourceLang = IndianLanguage.HINDI,
                targetLang = IndianLanguage.BENGALI,
                originalText = "यहाँ 5 लोग हैं, दो बच्चे भी शामिल हैं। पानी दूसरी मंजिल तक पहुँच गया है।",
                translatedText = "এখানে ৫ জন মানুষ আছেন, দুজন শিশুও রয়েছে। জল দ্বিতীয় তলা পর্যন্ত পৌঁছে গেছে।",
                audioDurationSec = 4.5f,
                payloadSizeBytes = 89,
                isEmergency = true,
                isIncoming = false,
                mode = AppMode.EMERGENCY_VOICE_NOTE,
                timestamp = System.currentTimeMillis() - 45_000
            )
        )
        _uiState.update { it.copy(messages = sampleMsgs) }
    }

    fun setMode(mode: AppMode) {
        _uiState.update { it.copy(activeMode = mode) }
    }

    fun setSourceLanguage(lang: IndianLanguage) {
        _uiState.update { it.copy(sourceLang = lang) }
    }

    fun setTargetLanguage(lang: IndianLanguage) {
        _uiState.update { it.copy(targetLang = lang) }
    }

    fun swapLanguages() {
        _uiState.update {
            it.copy(
                sourceLang = it.targetLang,
                targetLang = it.sourceLang
            )
        }
    }

    /**
     * Emergency test siren: Forces max volume, plays tone, and triggers vibration
     */
    fun testEmergencySiren() {
        audioController.forceMaximumEmergencyVolume()
        audioController.playEmergencyAlertTone()
        audioController.triggerEmergencyVibration()
    }

    /**
     * Sends an instant one-touch SOS phrase without needing to speak or type.
     * Uses offline local translation dictionary.
     */
    fun sendInstantSosPhrase(phrase: EmergencyPhrase) {
        val srcText = phrase.getText(_uiState.value.sourceLang)
        val tgtText = phrase.getText(_uiState.value.targetLang)
        val payloadBytes = srcText.toByteArray(Charsets.UTF_8).size + tgtText.toByteArray(Charsets.UTF_8).size + 30

        val newMsg = AudioMessage(
            id = UUID.randomUUID().toString(),
            senderName = "You",
            senderId = "self",
            sourceLang = _uiState.value.sourceLang,
            targetLang = _uiState.value.targetLang,
            originalText = srcText,
            translatedText = tgtText,
            audioDurationSec = 2.5f,
            payloadSizeBytes = payloadBytes,
            isEmergency = true,
            isIncoming = false,
            mode = _uiState.value.activeMode
        )

        _uiState.update { it.copy(messages = it.messages + newMsg) }

        // Simulate peer acknowledgment after 2 seconds
        viewModelScope.launch {
            delay(2500)
            simulateIncomingEmergencyResponse(newMsg)
        }
    }

    /**
     * Push-To-Talk Button Pressed
     */
    fun onPttDown() {
        _uiState.update {
            it.copy(
                isPttPressed = true,
                isRecording = true,
                vadSpeechActive = true,
                liveTranscriptPreview = "Listening... (Silero VAD active)"
            )
        }

        pttJob = viewModelScope.launch {
            delay(800)
            _uiState.update { it.copy(liveTranscriptPreview = "Capturing voice stream...") }
        }
    }

    /**
     * Push-To-Talk Button Released: Triggers on-device STT -> Translation -> Local P2P dispatch
     */
    fun onPttUp() {
        pttJob?.cancel()
        _uiState.update {
            it.copy(
                isPttPressed = false,
                isRecording = false,
                vadSpeechActive = false,
                isTranscribing = true,
                liveTranscriptPreview = "Running IndicConformer STT & IndicTrans2..."
            )
        }

        viewModelScope.launch {
            delay(600) // Simulating fast on-device inference
            val spokenText = when (_uiState.value.sourceLang) {
                IndianLanguage.HINDI -> "राहत शिविर में दवाइयाँ भेजें।"
                IndianLanguage.BENGALI -> "ত্রাণ শিবিরে ওষুধ পাঠান।"
                IndianLanguage.TAMIL -> "நிவாரண முகாமிற்கு மருந்துகளை அனுப்பவும்."
                else -> "Urgent medicines needed at relief center."
            }

            val translatedText = when (_uiState.value.targetLang) {
                IndianLanguage.BENGALI -> "ত্রাণ শিবিরে দ্রুত ওষুধ পাঠান।"
                IndianLanguage.HINDI -> "राहत शिविर में तुरंत दवाइयाँ भेजें।"
                IndianLanguage.TAMIL -> "நிவாரண முகாமிற்கு உடனடியாக மருந்துகளை அனுப்பவும்."
                else -> "Send emergency medical supplies to the camp."
            }

            val payloadSize = spokenText.toByteArray(Charsets.UTF_8).size + translatedText.toByteArray(Charsets.UTF_8).size + 28

            val message = AudioMessage(
                id = UUID.randomUUID().toString(),
                senderName = "You",
                senderId = "self",
                sourceLang = _uiState.value.sourceLang,
                targetLang = _uiState.value.targetLang,
                originalText = spokenText,
                translatedText = translatedText,
                audioDurationSec = 3.0f,
                payloadSizeBytes = payloadSize,
                isEmergency = _uiState.value.activeMode == AppMode.EMERGENCY_VOICE_NOTE,
                isIncoming = false,
                mode = _uiState.value.activeMode
            )

            _uiState.update {
                it.copy(
                    isTranscribing = false,
                    liveTranscriptPreview = "",
                    messages = it.messages + message
                )
            }
        }
    }

    /**
     * Plays the audio message.
     * In EMERGENCY_VOICE_NOTE mode, automatically forces volume to 100% MAX!
     */
    fun playMessage(message: AudioMessage) {
        if (_uiState.value.activeMode == AppMode.EMERGENCY_VOICE_NOTE || message.isEmergency) {
            // Force 100% maximum volume override for emergency survival!
            audioController.forceMaximumEmergencyVolume()
            audioController.requestEmergencyAudioFocus()
            audioController.triggerEmergencyVibration()
        }

        _uiState.update { it.copy(currentlyPlayingMessageId = message.id) }

        viewModelScope.launch {
            delay((message.audioDurationSec * 1000).toLong())
            _uiState.update { it.copy(currentlyPlayingMessageId = null) }
        }
    }

    /**
     * Phone Mode Controls
     */
    fun startPhoneCall() {
        _uiState.update { it.copy(isCallActive = true, activeCallDurationSec = 0) }
        callTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { it.copy(activeCallDurationSec = it.activeCallDurationSec + 1) }
            }
        }
    }

    fun endPhoneCall() {
        callTimerJob?.cancel()
        _uiState.update { it.copy(isCallActive = false, activeCallDurationSec = 0) }
    }

    fun toggleSpeaker() {
        _uiState.update { it.copy(isSpeakerOn = !it.isSpeakerOn) }
    }

    fun toggleMute() {
        _uiState.update { it.copy(isMuted = !it.isMuted) }
    }

    private fun simulateIncomingEmergencyResponse(outbound: AudioMessage) {
        val incoming = AudioMessage(
            id = UUID.randomUUID().toString(),
            senderName = "Rescue Team 04",
            senderId = "peer_ndrf",
            sourceLang = _uiState.value.targetLang,
            targetLang = _uiState.value.sourceLang,
            originalText = "বার্তা পেয়েছি। মোটর বোট রওনা হয়ে গেছে, ৫ মিনিটের মধ্যে পৌঁছাবে।",
            translatedText = "संदेश प्राप्त हुआ। मोटर बोट रवाना हो चुकी है, 5 मिनट में पहुँचेगी।",
            audioDurationSec = 3.6f,
            payloadSizeBytes = 84,
            isEmergency = true,
            isIncoming = true,
            mode = _uiState.value.activeMode
        )

        _uiState.update { it.copy(messages = it.messages + incoming) }

        // In Emergency mode, auto-trigger loudest alert
        if (_uiState.value.activeMode == AppMode.EMERGENCY_VOICE_NOTE) {
            playMessage(incoming)
        }
    }
}

