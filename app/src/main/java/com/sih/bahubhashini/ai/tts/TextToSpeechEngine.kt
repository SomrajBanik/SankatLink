package com.sih.bahubhashini.ai.tts

/**
 * Text-to-Speech (TTS) engine interface.
 *
 * Planned implementation: Sherpa-ONNX VITS / Piper (on-device, offline).
 * Model size: ~25 MB per language | License: MIT / Apache 2.0
 *
 * TODO: Add dependency com.k2fsa.sherpa.onnx Android AAR to build.gradle.kts.
 *       Load VITS ONNX model + lexicon + tokens from assets/ for each supported
 *       language. Use OfflineTts to synthesize and play via AudioTrack.
 */
interface TextToSpeechEngine {
    /**
     * Synthesizes the given text to audio and plays it immediately.
     * @param text The text to speak.
     * @param languageCode ISO 639-1 code (e.g. "hi", "bn", "ta").
     * @param speedFactor Playback speed multiplier (default 1.0).
     */
    suspend fun speak(text: String, languageCode: String, speedFactor: Float = 1.0f)

    /**
     * Synthesizes text and returns raw 16kHz mono PCM samples (for custom playback).
     */
    suspend fun synthesize(text: String, languageCode: String): ShortArray

    /** Stops any ongoing synthesis/playback immediately. */
    fun stop()

    /** Releases native Sherpa-ONNX TTS resources. */
    fun release()
}
