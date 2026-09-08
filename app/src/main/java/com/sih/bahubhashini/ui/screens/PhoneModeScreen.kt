package com.sih.bahubhashini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih.bahubhashini.model.AudioMessage
import com.sih.bahubhashini.model.IndianLanguage
import com.sih.bahubhashini.ui.theme.AlertAmber
import com.sih.bahubhashini.ui.theme.CardBorder
import com.sih.bahubhashini.ui.theme.DarkSurface
import com.sih.bahubhashini.ui.theme.DarkSurfaceElevated
import com.sih.bahubhashini.ui.theme.DarkSurfaceVariant
import com.sih.bahubhashini.ui.theme.EmergencyRed
import com.sih.bahubhashini.ui.theme.OnEmergencyRed
import com.sih.bahubhashini.ui.theme.SignalGreen
import com.sih.bahubhashini.ui.theme.TechCyan
import com.sih.bahubhashini.ui.theme.TextPrimary
import com.sih.bahubhashini.ui.theme.TextSecondary
import com.sih.bahubhashini.ui.components.WaveformVisualizer

@Composable
fun PhoneModeScreen(
    isCallActive: Boolean,
    callDurationSec: Int,
    isSpeakerOn: Boolean,
    isMuted: Boolean,
    sourceLang: IndianLanguage,
    targetLang: IndianLanguage,
    messages: List<AudioMessage>,
    onToggleCall: () -> Unit,
    onToggleSpeaker: () -> Unit,
    onToggleMute: () -> Unit,
    modifier: Modifier = Modifier
) {
    val minutes = callDurationSec / 60
    val seconds = callDurationSec % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Call Details
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceElevated)
                    .border(2.dp, if (isCallActive) SignalGreen else CardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PhoneInTalk,
                    contentDescription = null,
                    tint = if (isCallActive) SignalGreen else TextSecondary,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Rescue Boat Alpha (NDRF)",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isCallActive) "CALL ACTIVE • $timeFormatted" else "OFFLINE READY • TAP TO CALL",
                color = if (isCallActive) SignalGreen else AlertAmber,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )

            Text(
                text = "Direct P2P Link (No Internet) • Low-Bandwidth Text Sync",
                color = TextSecondary,
                fontSize = 10.5.sp
            )
        }

        // Live Two-Way Translated Subtitles Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 14.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurfaceElevated)
                .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIVE TRANSLATED CAPTIONS",
                        color = TechCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(DarkSurfaceVariant)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${sourceLang.nativeName} ⇄ ${targetLang.nativeName}",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Last peer incoming message
                val lastIncoming = messages.lastOrNull { it.isIncoming }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceVariant)
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "PEER SAYS (${targetLang.nativeName}):",
                            color = SignalGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = lastIncoming?.translatedText ?: "বোঁট প্রস্তুত আছে, আমরা ঘাটে পৌঁছাচ্ছি।",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Original: ${lastIncoming?.originalText ?: "বোট প্রস্তুত আছে"}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Last your outgoing message
                val lastOutgoing = messages.lastOrNull { !it.isIncoming }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceVariant.copy(alpha = 0.6f))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "YOU SAID (${sourceLang.nativeName}):",
                            color = TechCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = lastOutgoing?.originalText ?: "हम मंदिर के पास की छत पर प्रतीक्षा कर रहे हैं।",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Peer received as: ${lastOutgoing?.translatedText ?: "আমরা মন্দিরের কাছের ছাদে অপেক্ষা করছি।"}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Waveform showing call audio activity
                WaveformVisualizer(
                    isActive = isCallActive,
                    barColor = SignalGreen,
                    maxBarHeight = 26.dp,
                    barCount = 20
                )
            }
        }

        // Bottom Call Controls Bar
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mute Button
                IconButton(
                    onClick = onToggleMute,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(if (isMuted) AlertAmber else DarkSurfaceElevated)
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = if (isMuted) OnEmergencyRed else TextPrimary
                    )
                }

                // Call / Hangup Button
                IconButton(
                    onClick = onToggleCall,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(if (isCallActive) EmergencyRed else SignalGreen)
                ) {
                    Icon(
                        imageVector = if (isCallActive) Icons.Default.CallEnd else Icons.Default.Call,
                        contentDescription = "Call",
                        tint = OnEmergencyRed,
                        modifier = Modifier.size(34.dp)
                    )
                }

                // Speaker Button
                IconButton(
                    onClick = onToggleSpeaker,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(if (isSpeakerOn) SignalGreen else DarkSurfaceElevated)
                ) {
                    Icon(
                        imageVector = if (isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Speaker",
                        tint = if (isSpeakerOn) OnEmergencyRed else TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isCallActive) "Loudspeaker: ${if (isSpeakerOn) "ON (Max Emergency)" else "OFF"} • Audio synthesized via on-device TTS" else "Tap Green Call to establish low-bandwidth P2P call",
                color = TextSecondary,
                fontSize = 10.sp
            )
        }
    }
}

