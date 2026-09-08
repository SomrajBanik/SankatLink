package com.sih.bahubhashini.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sih.bahubhashini.theme.EmergencyRed
import com.sih.bahubhashini.theme.SignalGreen
import com.sih.bahubhashini.theme.TechCyan
import com.sih.bahubhashini.ui.theme.EmergencyRed
import com.sih.bahubhashini.ui.theme.SignalGreen
import com.sih.bahubhashini.ui.theme.TechCyan

@Composable
fun WaveformVisualizer(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    barColor: Color = TechCyan,
    maxBarHeight: Dp = 48.dp,
    barCount: Int = 18
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")

    val anim1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar1"
    )

    val anim2 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar2"
    )

    val anim3 by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar3"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(maxBarHeight),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until barCount) {
            val scale = if (!isActive) {
                0.15f
            } else {
                when (i % 3) {
                    0 -> anim1
                    1 -> anim2
                    else -> anim3
                } * (0.5f + 0.5f * kotlin.math.sin(i.toDouble() / barCount * Math.PI).toFloat())
            }

            val currentHeight = maxBarHeight * scale.coerceIn(0.12f, 1f)

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(currentHeight)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isActive) barColor else barColor.copy(alpha = 0.3f))
            )

            if (i < barCount - 1) {
                Box(modifier = Modifier.width(3.dp))
            }
        }
    }
}

