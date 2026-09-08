package com.sih.bahubhashini.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih.bahubhashini.ui.theme.DarkSurfaceVariant
import com.sih.bahubhashini.ui.theme.SignalGreen
import com.sih.bahubhashini.ui.theme.TechCyan
import com.sih.bahubhashini.ui.theme.TextPrimary
import com.sih.bahubhashini.ui.theme.TextSecondary

@Composable
fun ConnectionStatusBar(
    connectedPeersCount: Int,
    transportType: String,
    onOpenPeersSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceVariant)
            .clickable { onOpenPeersSheet() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Pulsing mesh status dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (connectedPeersCount > 0) SignalGreen else Color.Gray)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (connectedPeersCount > 0) "OFFLINE P2P MESH ACTIVE" else "SEARCHING LOCAL PEERS",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = if (transportType.contains("Wi-Fi")) Icons.Default.WifiTethering else Icons.Default.Bluetooth,
                            contentDescription = null,
                            tint = TechCyan,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = if (connectedPeersCount > 0)
                            "$connectedPeersCount nearby peer(s) • Low-bandwidth text payload (<100 B)"
                        else
                            "No internet needed • Wi-Fi Direct & BLE ready",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            // Peer Count Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(TechCyan.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (connectedPeersCount > 0) "$connectedPeersCount Peers" else "Scan",
                    color = TechCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

