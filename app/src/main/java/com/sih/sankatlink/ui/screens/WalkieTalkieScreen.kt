package com.sih.sankatlink.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Radio
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
import com.sih.sankatlink.model.AudioMessage
import com.sih.sankatlink.model.IndianLanguage
import com.sih.sankatlink.ui.components.WaveformVisualizer
import com.sih.sankatlink.ui.theme.CardBorder
import com.sih.sankatlink.ui.theme.DarkSurfaceElevated
import com.sih.sankatlink.ui.theme.DarkSurfaceVariant
import com.sih.sankatlink.ui.theme.SignalGreen
import com.sih.sankatlink.ui.theme.TechCyan
import com.sih.sankatlink.ui.theme.TextPrimary
import com.sih.sankatlink.ui.theme.TextSecondary

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
        targetValue = if (isPttPressed) 0.94f else 1.0f,
        label = "pttScale"
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 14.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MESH CHANNEL",
                color = TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp
            )
            Text(
                text = "Direct RF / Wi-Fi P2P",
                color = TechCyan,
                fontSize = 9.5.sp
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(channels) { ch ->
                val isSelected = ch == selectedChannel
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) TechCyan.copy(alpha = 0.15f) else DarkSurfaceVariant)
                        .border(
                            1.dp,
                            if (isSelected) TechCyan.copy(alpha = 0.6f) else CardBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedChannel = ch }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Radio,
                            contentDescription = null,
                            tint = if (isSelected) TechCyan else TextSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = ch,
                            color = if (isSelected) TextPrimary else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(DarkSurfaceElevated)
                .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(if (vadSpeechActive) SignalGreen else Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (vadSpeechActive) "VAD: SPEECH DETECTED" else "VAD: STANDBY (SILERO ONNX)",
                            color = if (vadSpeechActive) SignalGreen else TextPrimary,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Auto-clips silence • Zero idle bandwidth",
                            color = TextSecondary,
                            fontSize = 9.5.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DarkSurfaceVariant)
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${sourceLang.nativeName} ➔ ${targetLang.nativeName}",
                        color = TechCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        WaveformVisualizer(
            isActive = isPttPressed || isTranscribing,
            barColor = if (isPttPressed) SignalGreen else TechCyan,
            maxBarHeight = 32.dp,
            barCount = 24
        )

        Box(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurfaceVariant)
                .padding(horizontal = 14.dp, vertical = 5.dp)
        ) {
            Text(
                text = if (isTranscribing)
                    "⚡ $liveTranscriptPreview"
                else if (isPttPressed)
                    "🟢 TRANSMITTING LIVE (PTT ACTIVE)"
                else
                    "HOLD BUTTON TO TALK • RELEASE TO BROADCAST",
                color = if (isPttPressed) SignalGreen else TextSecondary,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(140.dp)
                .scale(buttonScale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = if (isPttPressed)
                            listOf(SignalGreen.copy(alpha = 0.9f), SignalGreen.copy(alpha = 0.6f))
                        else
                            listOf(DarkSurfaceElevated, DarkSurfaceVariant)
                    )
                )
                .border(
                    width = if (isPttPressed) 3.dp else 2.dp,
                    color = if (isPttPressed) SignalGreen else TechCyan.copy(alpha = 0.4f),
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
                    tint = if (isPttPressed) Color(0xFF0B111E) else TechCyan,
                    modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isPttPressed) "TRANSMITTING" else "PUSH TO TALK",
                    color = if (isPttPressed) Color(0xFF0B111E) else TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.4.sp
                )
                Text(
                    text = "Auto-Plays on Loudspeaker",
                    color = if (isPttPressed) Color(0xFF0B111E).copy(alpha = 0.8f) else TextSecondary,
                    fontSize = 8.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RECENT CHANNEL BROADCASTS",
                color = TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp
            )
            Text(
                text = "Auto-Synthesized",
                color = TechCyan,
                fontSize = 9.5.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (messages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No broadcasts yet. Press PTT to speak.",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            } else {
                messages.takeLast(4).reversed().forEach { msg ->
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
                                    text = "📦 ${msg.payloadSizeBytes} B",
                                    color = TextSecondary,
                                    fontSize = 9.5.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = msg.translatedText,
                                color = TextPrimary,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Spoken: ${msg.originalText}",
                                color = TextSecondary,
                                fontSize = 10.5.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
    }
}