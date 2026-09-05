package com.sih.bahubhashini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
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
import com.sih.bahubhashini.model.EmergencyPhrase
import com.sih.bahubhashini.model.IndianLanguage
import com.sih.bahubhashini.theme.AlertAmber
import com.sih.bahubhashini.theme.CardBorder
import com.sih.bahubhashini.theme.DarkSurface
import com.sih.bahubhashini.theme.DarkSurfaceElevated
import com.sih.bahubhashini.theme.DarkSurfaceVariant
import com.sih.bahubhashini.theme.EmergencyRed
import com.sih.bahubhashini.theme.EmergencyRedContainer
import com.sih.bahubhashini.theme.OnEmergencyRed
import com.sih.bahubhashini.theme.SignalGreen
import com.sih.bahubhashini.theme.TechCyan
import com.sih.bahubhashini.theme.TextPrimary
import com.sih.bahubhashini.theme.TextSecondary
import com.sih.bahubhashini.ui.components.EmergencyVolumeBanner
import com.sih.bahubhashini.ui.components.WaveformVisualizer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VoiceNoteScreen(
    messages: List<AudioMessage>,
    sourceLang: IndianLanguage,
    currentlyPlayingId: String?,
    onPlayMessage: (AudioMessage) -> Unit,
    onSendEmergencyPhrase: (EmergencyPhrase) -> Unit,
    onTestSiren: () -> Unit,
    onRecordVoiceNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        // High Volume Override Warning Banner
        EmergencyVolumeBanner(onTestAlert = onTestSiren)

        Spacer(modifier = Modifier.height(10.dp))

        // Quick 1-Tap SOS Phrases Bar
        Text(
            text = "ONE-TOUCH SOS BROADCAST (OFFLINE)",
            color = AlertAmber,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(IndianLanguage.EMERGENCY_PHRASES) { phrase ->
                QuickSosChip(
                    phrase = phrase,
                    language = sourceLang,
                    onClick = { onSendEmergencyPhrase(phrase) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Incoming / Outgoing Emergency Messages
        Text(
            text = "EMERGENCY AUDIO & TEXT FEED",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(messages.reversed(), key = { it.id }) { msg ->
                EmergencyMessageCard(
                    message = msg,
                    isPlaying = msg.id == currentlyPlayingId,
                    onPlay = { onPlayMessage(msg) }
                )
            }
        }

        // Bottom Record Emergency Voice Note Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurfaceElevated)
                .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "RECORD EMERGENCY VOICE NOTE",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "VAD -> STT -> Translated text payload (<100 B)",
                        color = TechCyan,
                        fontSize = 10.5.sp
                    )
                }

                Button(
                    onClick = onRecordVoiceNote,
                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Record",
                        tint = OnEmergencyRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Record & Send",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickSosChip(
    phrase: EmergencyPhrase,
    language: IndianLanguage,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurfaceVariant)
            .border(1.dp, AlertAmber.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = AlertAmber,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = phrase.getText(language),
                color = TextPrimary,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun EmergencyMessageCard(
    message: AudioMessage,
    isPlaying: Boolean,
    onPlay: () -> Unit
) {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val formattedTime = timeFormat.format(Date(message.timestamp))

    val borderColor = if (message.isEmergency) EmergencyRed.copy(alpha = 0.6f) else CardBorder
    val bgColor = if (message.isEmergency) EmergencyRedContainer.copy(alpha = 0.35f) else DarkSurfaceElevated

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column {
            // Header: Sender, Time, Emergency Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (message.isIncoming) SignalGreen else TechCyan)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (message.isIncoming) message.senderName else "You (Broadcast)",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (message.isEmergency) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(EmergencyRed)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "SOS MAX VOL",
                                color = OnEmergencyRed,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Text(
                        text = formattedTime,
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Translated Text (Large & Prominent)
            Text(
                text = message.translatedText,
                color = TextPrimary,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Original Spoken Text (Subtext with source language)
            Text(
                text = "Original (${message.sourceLang.nativeName}): ${message.originalText}",
                color = TextSecondary,
                fontSize = 11.5.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Audio Player Bar & Low Bandwidth Metadata
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurfaceVariant)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onPlay,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(EmergencyRed)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = "Play at Max Volume",
                            tint = OnEmergencyRed,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    if (isPlaying) {
                        WaveformVisualizer(
                            isActive = true,
                            barColor = EmergencyRed,
                            maxBarHeight = 22.dp,
                            barCount = 14,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Column {
                            Text(
                                text = "Play at 100% Volume (${message.audioDurationSec}s)",
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Local TTS synthesis • Zero audio bytes transferred",
                                color = SignalGreen,
                                fontSize = 9.5.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Low-bandwidth footprint pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(TechCyan.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "📦 ${message.payloadSizeBytes} B (P2P)",
                        color = TechCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

