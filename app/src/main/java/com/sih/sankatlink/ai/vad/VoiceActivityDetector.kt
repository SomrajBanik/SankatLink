package com.sih.sankatlink.ai.vad

/**
 * Voice Activity Detection (VAD) engine interface.
 *
 * Planned implementation: Silero VAD v5 (ONNX Runtime Android / Sherpa-ONNX).
 * Model size: ~2.2 MB | License: MIT
 *
 * TODO: Integrate com.microsoft.onnxruntime or com.k2fsa.sherpa.onnx AAR.
 *       Load the silero_vad.onnx model from assets/ and run inference
 *       on 16kHz PCM audio frames to detect speech segments.
 */
interface VoiceActivityDetector {
    /** Returns true if the given raw PCM audio frame contains active speech. */
    fun isSpeech(audioFrame: ShortArray): Boolean

    /** Resets internal state (e.g. between utterances). */
    fun reset()

    /** Releases native ONNX session resources. */
    fun release()
}
