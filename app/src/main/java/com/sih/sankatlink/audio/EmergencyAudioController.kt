package com.sih.sankatlink.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

/**
 * Controller responsible for emergency audio behaviors:
 * 1. Overrides media & alarm streams to 100% MAXIMUM VOLUME for critical distress messages.
 * 2. Requests transient exclusive audio focus.
 * 3. Triggers loud alert tone & haptic vibration sirens.
 */
class EmergencyAudioController(private val context: Context) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var previousVolume: Int? = null

    /**
     * Forces system media and alarm stream volume to 100% MAXIMUM for emergency audibility.
     */
    fun forceMaximumEmergencyVolume() {
        try {
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            if (previousVolume == null) {
                previousVolume = currentVolume
            }

            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                maxVolume,
                AudioManager.FLAG_SHOW_UI
            )

            // Also set alarm stream to max for life safety
            val maxAlarm = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
            audioManager.setStreamVolume(
                AudioManager.STREAM_ALARM,
                maxAlarm,
                0
            )

            Log.i("EmergencyAudio", "Volume overridden to 100% MAX ($maxVolume) for emergency voice note.")
        } catch (e: Exception) {
            Log.e("EmergencyAudio", "Failed to force max volume: ${e.message}")
        }
    }

    /**
     * Requests high-priority exclusive audio focus for emergency voice notes
     */
    fun requestEmergencyAudioFocus(): Boolean {
        return try {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()

            val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(false)
                .setOnAudioFocusChangeListener { /* Emergency focus locked */ }
                .build()

            val res = audioManager.requestAudioFocus(focusRequest)
            res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } catch (e: Exception) {
            Log.e("EmergencyAudio", "Failed to request audio focus: ${e.message}")
            false
        }
    }

    /**
     * Triggers a loud attention-getting emergency beep tone before TTS voice playback begins
     */
    fun playEmergencyAlertTone() {
        try {
            val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 100)
            toneGen.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 400)
        } catch (e: Exception) {
            Log.e("EmergencyAudio", "Failed to play alert tone: ${e.message}")
        }
    }

    /**
     * Triggers a distinct emergency haptic siren pattern on the phone
     */
    fun triggerEmergencyVibration() {
        try {
            val timings = longArrayOf(0, 250, 100, 250, 100, 400)
            val amplitudes = intArrayOf(0, 255, 0, 255, 0, 255)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                val combined = CombinedVibration.createParallel(effect)
                vibratorManager?.vibrate(combined)
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                vibrator?.vibrate(effect)
            }
        } catch (e: Exception) {
            Log.e("EmergencyAudio", "Vibration failed: ${e.message}")
        }
    }

    /**
     * Restores previous volume after emergency playback if desired
     */
    fun restorePreviousVolume() {
        previousVolume?.let { prev ->
            try {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, prev, 0)
                previousVolume = null
            } catch (e: Exception) {
                Log.e("EmergencyAudio", "Failed to restore volume: ${e.message}")
            }
        }
    }
}

