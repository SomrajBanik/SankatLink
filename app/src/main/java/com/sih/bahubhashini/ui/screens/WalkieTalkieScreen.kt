package com.sih.bahubhashini.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih.bahubhashini.model.AudioMessage
import com.sih.bahubhashini.model.IndianLanguage
import com.sih.bahubhashini.theme.AlertAmber
import com.sih.bahubhashini.theme.CardBorder
import com.sih.bahubhashini.theme.DarkSurface
import com.sih.bahubhashini.theme.DarkSurfaceElevated
import com.sih.bahubhashini.theme.DarkSurfaceVariant
import com.sih.bahubhashini.theme.EmergencyRed
import com.sih.bahubhashini.theme.EmergencyRedDark
import com.sih.bahubhashini.theme.OnEmergencyRed
import com.sih.bahubhashini.theme.SignalGreen
import com.sih.bahubhashini.theme.TechCyan
import com.sih.bahubhashini.theme.TextPrimary
import com.sih.bahubhashini.theme.TextSecondary
import com.sih.bahubhashini.ui.components.WaveformVisualizer

@Composable
fun WalkieTalkieScreen(
    isPttPressed: Boolean,
    vadSpeechActive: Boolean,
    isTranscribing: Boolean,
    liveTranscriptPreview: String,
    messages: List<AudioMessage>,
    sourceLang: IndianLanguage,
    targetLang: IndianLanguage,
    onPttDown: () -> Unit,
    onPttUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedChannel by remember { mutableStateOf("CH 01 - Emergency SOS") }
    val channels = listOf("CH 01 - Emergency SOS", "CH 02 - Medical", "CH 03 - Evacuation Logistics")

    val buttonScale by animateFloatAsState(
        targetValue = if (isPttPressed) 0.93f else 1.0f,
        label = "pttScale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Channel Selector Bar
        Text(
            text = "SELECT MESH FREQUENCY / CHANNEL",
            color = TextSecondary,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(channels) { ch ->
                val isSelected = ch == selectedChannel
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) AlertAmber.copy(alpha = 0.2f) else DarkSurfaceVariant)
                        .border(1.dp, if (isSelected) AlertAmber else CardBorder, RoundedCornerShape(10.dp))
                        .clickable { selectedChannel = ch }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Radio,
                            contentDescription = null,
                            tint = if (isSelected) AlertAmber else TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = ch,
                            color = if (isSelected) AlertAmber else TextPrimary,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // VAD (Voice Activity Detection) Status Monitor
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DarkSurfaceElevated)
                .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(if (vadSpeechActive) SignalGreen else Color.DarkGray)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (vadSpeechActive) "VAD: SPEECH DETECTED" else "VAD: MONITORING (SILERO ONNX)",
                            color = if (vadSpeechActive) SignalGreen else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Auto-clips silence • Zero audio bytes stored",
                            color = TextSecondary,
                            fontSize = 9.5.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(TechCyan.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${sourceLang.nativeName} ➔ ${targetLang.nativeName}",
                        color = TechCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Waveform Visualizer for live voice
        WaveformVisualizer(
            isActive = isPttPressed || isTranscribing,
            barColor = if (isPttPressed) EmergencyRed else TechCyan,
            maxBarHeight = 36.dp,
            barCount = 24
        )

        // Live transcription / status pill
        Box(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(DarkSurfaceVariant)
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(
                text = if (isTranscribing)
                    "⚡ $liveTranscriptPreview"
                else if (isPttPressed)
                    "🔴 TRANSMITTING LIVE (PTT ACTIVE)"
                else
                    "HOLD TO TALK • RELEASE TO BROADCAST",
                color = if (isPttPressed) EmergencyRed else TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Large Tactile Push-To-Talk (PTT) Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(160.dp)
                .scale(buttonScale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = if (isPttPressed)
                            listOf(EmergencyRed, EmergencyRedDark)
                        else
                            listOf(DarkSurfaceElevated, DarkSurfaceVariant)
                    )
                )
                .border(
                    width = if (isPttPressed) 4.dp else 2.dp,
                    color = if (isPttPressed) AlertAmber else CardBorder,
                    shape = CircleShape
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            onPttDown()
                            tryAwaitRelease()
                            onPttUp()
                        }
                    )
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Push To Talk",
                    tint = if (isPttPressed) OnEmergencyRed else TechCyan,
                    modifier = Modifier.size(54.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isPttPressed) "TRANSMITTING" else "PUSH TO TALK",
                    color = if (isPttPressed) OnEmergencyRed else TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Loudspeaker Auto-Play",
                    color = if (isPttPressed) OnEmergencyRed.copy(alpha = 0.8f) else TextSecondary,
                    fontSize = 9.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Recent Tactical Transmissions Log
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RECENT CHANNEL BROADCASTS",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Auto-Played via TTS",
                color = SignalGreen,
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages.takeLast(4).reversed()) { msg ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurfaceElevated)
                        .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = msg.senderName,
                                color = if (msg.isIncoming) SignalGreen else TechCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "📦 ${msg.payloadSizeBytes} Bytes",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = msg.translatedText,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Spoken: ${msg.originalText}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

