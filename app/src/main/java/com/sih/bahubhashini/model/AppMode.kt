package com.sih.bahubhashini.model

/**
 * Three distinct operational modes for BahuBhashini:
 * 1. EMERGENCY_VOICE_NOTE: High volume priority alert notes (max volume override)
 * 2. WALKIE_TALKIE: Push-to-Talk (PTT) tactical broadcast
 * 3. PHONE_CALL: Near-synchronous two-way voice call with translated live captions
 */
enum class AppMode(
    val title: String,
    val subtitle: String
) {
    EMERGENCY_VOICE_NOTE(
        title = "Voice Note",
        subtitle = "Emergency Max Vol"
    ),
    WALKIE_TALKIE(
        title = "Walkie-Talkie",
        subtitle = "Push-To-Talk (PTT)"
    ),
    PHONE_CALL(
        title = "Phone Call",
        subtitle = "Turn-Based Voice"
    )
}

