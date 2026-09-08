package com.sih.sankatlink.ai.stt

/**
 * Speech-to-Text (STT / ASR) engine interface.
 *
 * Planned implementation: AI4Bharat IndicConformer CTC via Sherpa-ONNX.
 * Model size: ~45–80 MB | License: MIT / CC-BY
 *
 * TODO: Add dependency com.k2fsa.sherpa.onnx Android AAR to build.gradle.kts.
 *       Load the IndicConformer CTC ONNX model from assets/, configure
 *       OnlineRecognizer / OfflineRecognizer with the appropriate tokens file,
 *       and stream 16kHz PCM audio for low-latency transcription.
 */
interface SpeechToTextEngine {
    /**
     * Transcribes a complete audio buffer (offline / batch mode).
     * @param audioData 16kHz mono PCM audio samples.
     * @param languageCode ISO 639-1 code (e.g. "hi", "bn", "ta").
     * @return Transcribed text string.
     */
    suspend fun transcribe(audioData: ShortArray, languageCode: String): String

    /** Releases native Sherpa-ONNX recognizer resources. */
    fun release()
}
