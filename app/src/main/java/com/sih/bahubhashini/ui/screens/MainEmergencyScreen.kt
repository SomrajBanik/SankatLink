package com.sih.bahubhashini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih.bahubhashini.model.AppMode
import com.sih.bahubhashini.model.IndianLanguage
import com.sih.bahubhashini.model.PeerDevice
import com.sih.bahubhashini.ui.theme.AlertAmber
import com.sih.bahubhashini.ui.theme.CardBorder
import com.sih.bahubhashini.ui.theme.DarkBackground
import com.sih.bahubhashini.ui.theme.DarkSurface
import com.sih.bahubhashini.ui.theme.DarkSurfaceElevated
import com.sih.bahubhashini.ui.theme.DarkSurfaceVariant
import com.sih.bahubhashini.ui.theme.EmergencyRed
import com.sih.bahubhashini.ui.theme.EmergencyRedContainer
import com.sih.bahubhashini.ui.theme.OnEmergencyRed
import com.sih.bahubhashini.ui.theme.SignalGreen
import com.sih.bahubhashini.ui.theme.TechCyan
import com.sih.bahubhashini.ui.theme.TextPrimary
import com.sih.bahubhashini.ui.theme.TextSecondary
import com.sih.bahubhashini.ui.components.ConnectionStatusBar
import com.sih.bahubhashini.ui.components.LanguageSelectorDialog
import com.sih.bahubhashini.viewmodel.BahuBhashiniViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainEmergencyScreen(
    viewModel: BahuBhashiniViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    var showSourceLangDialog by remember { mutableStateOf(false) }
    var showTargetLangDialog by remember { mutableStateOf(false) }
    var showPeersSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            EmergencyTopAppBar(
                onOpenPeers = { showPeersSheet = true }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Connection Status Bar
            ConnectionStatusBar(
                connectedPeersCount = uiState.connectedPeers.count { it.isConnected },
                transportType = "Wi-Fi Direct & BLE",
                onOpenPeersSheet = { showPeersSheet = true },
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )

            // Language Selector Bar (Source ⇄ Target)
            LanguageSelectorBar(
                sourceLang = uiState.sourceLang,
                targetLang = uiState.targetLang,
                onSourceClick = { showSourceLangDialog = true },
                onTargetClick = { showTargetLangDialog = true },
                onSwap = { viewModel.swapLanguages() },
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )

            // Segmented Mode Selector
            ModeSelectorBar(
                currentMode = uiState.activeMode,
                onModeSelected = { viewModel.setMode(it) },
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Active Mode Screen Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (uiState.activeMode) {
                    AppMode.EMERGENCY_VOICE_NOTE -> {
                        VoiceNoteScreen(
                            messages = uiState.messages,
                            sourceLang = uiState.sourceLang,
                            currentlyPlayingId = uiState.currentlyPlayingMessageId,
                            onPlayMessage = { viewModel.playMessage(it) },
                            onSendEmergencyPhrase = { viewModel.sendInstantSosPhrase(it) },
                            onTestSiren = { viewModel.testEmergencySiren() },
                            onRecordVoiceNote = {
                                viewModel.onPttDown()
                                // simulate 2s recording
                                viewModel.onPttUp()
                            }
                        )
                    }
                    AppMode.WALKIE_TALKIE -> {
                        WalkieTalkieScreen(
                            isPttPressed = uiState.isPttPressed,
                            vadSpeechActive = uiState.vadSpeechActive,
                            isTranscribing = uiState.isTranscribing,
                            liveTranscriptPreview = uiState.liveTranscriptPreview,
                            messages = uiState.messages,
                            sourceLang = uiState.sourceLang,
                            targetLang = uiState.targetLang,
                            onPttDown = { viewModel.onPttDown() },
                            onPttUp = { viewModel.onPttUp() }
                        )
                    }
                    AppMode.PHONE_CALL -> {
                        PhoneModeScreen(
                            isCallActive = uiState.isCallActive,
                            callDurationSec = uiState.activeCallDurationSec,
                            isSpeakerOn = uiState.isSpeakerOn,
                            isMuted = uiState.isMuted,
                            sourceLang = uiState.sourceLang,
                            targetLang = uiState.targetLang,
                            messages = uiState.messages,
                            onToggleCall = {
                                if (uiState.isCallActive) viewModel.endPhoneCall() else viewModel.startPhoneCall()
                            },
                            onToggleSpeaker = { viewModel.toggleSpeaker() },
                            onToggleMute = { viewModel.toggleMute() }
                        )
                    }
                }
            }
        }
    }

    // Source Language Selection Dialog
    if (showSourceLangDialog) {
        LanguageSelectorDialog(
            title = "Select Source Language (You Speak)",
            currentSelection = uiState.sourceLang,
            onLanguageSelected = { viewModel.setSourceLanguage(it) },
            onDismiss = { showSourceLangDialog = false }
        )
    }

    // Target Language Selection Dialog
    if (showTargetLangDialog) {
        LanguageSelectorDialog(
            title = "Select Target Language (Peer Receives)",
            currentSelection = uiState.targetLang,
            onLanguageSelected = { viewModel.setTargetLanguage(it) },
            onDismiss = { showTargetLangDialog = false }
        )
    }

    // Discovered Nearby Peers Bottom Sheet
    if (showPeersSheet) {
        NearbyPeersBottomSheet(
            peers = uiState.connectedPeers,
            onDismiss = { showPeersSheet = false }
        )
    }
}

@Composable
private fun EmergencyTopAppBar(
    onOpenPeers: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(EmergencyRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Emergency,
                        contentDescription = "Logo",
                        tint = OnEmergencyRed,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "BAHU-BHASHINI",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "10-Lang Offline Mesh Voice • Zero Internet",
                        color = TechCyan,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            IconButton(
                onClick = onOpenPeers,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant)
            ) {
                Icon(
                    imageVector = Icons.Default.WifiTethering,
                    contentDescription = "Nearby Peers",
                    tint = TechCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun LanguageSelectorBar(
    sourceLang: IndianLanguage,
    targetLang: IndianLanguage,
    onSourceClick: () -> Unit,
    onTargetClick: () -> Unit,
    onSwap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceElevated)
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Source Language Chip
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DarkSurfaceVariant)
                    .clickable { onSourceClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Column {
                    Text(
                        text = "YOU SPEAK",
                        color = TextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${sourceLang.nativeName} (${sourceLang.displayName})",
                        color = TextPrimary,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Swap Button
            IconButton(
                onClick = onSwap,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(DarkSurface)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = "Swap Languages",
                    tint = TechCyan,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Target Language Chip
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DarkSurfaceVariant)
                    .clickable { onTargetClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Column {
                    Text(
                        text = "PEER HEARS",
                        color = TextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${targetLang.nativeName} (${targetLang.displayName})",
                        color = TextPrimary,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ModeSelectorBar(
    currentMode: AppMode,
    onModeSelected: (AppMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppMode.values().forEach { mode ->
            val isSelected = mode == currentMode
            val icon = when (mode) {
                AppMode.EMERGENCY_VOICE_NOTE -> Icons.Default.RecordVoiceOver
                AppMode.WALKIE_TALKIE -> Icons.Default.Radio
                AppMode.PHONE_CALL -> Icons.Default.Call
            }

            val activeColor = when (mode) {
                AppMode.EMERGENCY_VOICE_NOTE -> EmergencyRed
                AppMode.WALKIE_TALKIE -> AlertAmber
                AppMode.PHONE_CALL -> SignalGreen
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) activeColor else DarkSurfaceElevated)
                    .border(1.dp, if (isSelected) activeColor else CardBorder, RoundedCornerShape(10.dp))
                    .clickable { onModeSelected(mode) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = icon,
                        contentDescription = mode.title,
                        tint = if (isSelected) Color.White else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = mode.title,
                        color = if (isSelected) Color.White else TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NearbyPeersBottomSheet(
    peers: List<PeerDevice>,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = DarkSurface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "NEARBY OFFLINE PEERS",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Local Wi-Fi Direct & BLE Mesh (No Internet)",
                        color = TextSecondary,
                        fontSize = 11.5.sp
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(peers) { peer ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceElevated)
                            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(if (peer.isConnected) SignalGreen else Color.Gray)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = peer.name,
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${peer.transport} • RSSI: ${peer.rssi} dBm • ~${peer.distanceEstimateMeters.toInt()}m away",
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (peer.isConnected) SignalGreen.copy(alpha = 0.2f) else DarkSurfaceVariant)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (peer.isConnected) "Connected" else "Pair",
                                    color = if (peer.isConnected) SignalGreen else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

