package com.sih.sankatlink.ai.translation

/**
 * Machine Translation (MT) engine interface.
 *
 * Planned implementation: AI4Bharat IndicTrans2 Distilled (ONNX, int8 quantized).
 * Model size: ~180–260 MB | License: MIT / CC-BY-4.0
 *
 * TODO: Add dependency ai.onnxruntime:onnxruntime-android to build.gradle.kts.
 *       Load the IndicTrans2 encoder + decoder ONNX models from assets/.
 *       Run tokenization (IndicNLP / SentencePiece), encoder-decoder inference,
 *       and detokenization for each supported language pair.
 */
interface TranslationEngine {
    /**
     * Translates the given text from one Indian language to another, fully offline.
     * @param text Source text to translate.
     * @param sourceLangCode ISO 639-1 source language code (e.g. "hi").
     * @param targetLangCode ISO 639-1 target language code (e.g. "bn").
     * @return Translated text string.
     */
    suspend fun translate(
        text: String,
        sourceLangCode: String,
        targetLangCode: String
    ): String

    /** Releases native ONNX session resources. */
    fun release()
}
