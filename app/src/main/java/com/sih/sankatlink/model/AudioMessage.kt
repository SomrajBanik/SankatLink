package com.sih.sankatlink.model

/**
 * Represents a voice/text packet transmitted or received over low-bandwidth P2P mesh.
 * Note: Only text metadata is transmitted over the wire (payloadSizeBytes < 150 bytes),
 * and audio is synthesized locally using on-device TTS.
 */
data class AudioMessage(
    val id: String,
    val senderName: String,
    val senderId: String,
    val sourceLang: IndianLanguage,
    val targetLang: IndianLanguage,
    val originalText: String,
    val translatedText: String,
    val audioDurationSec: Float,
    val payloadSizeBytes: Int,
    val isEmergency: Boolean = false,
    val isIncoming: Boolean = true,
    val mode: AppMode = AppMode.EMERGENCY_VOICE_NOTE,
    val timestamp: Long = System.currentTimeMillis()
)

