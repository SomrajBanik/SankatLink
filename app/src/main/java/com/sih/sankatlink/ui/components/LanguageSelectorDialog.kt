package com.sih.sankatlink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih.sankatlink.model.IndianLanguage
import com.sih.sankatlink.ui.theme.CardBorder
import com.sih.sankatlink.ui.theme.DarkSurface
import com.sih.sankatlink.ui.theme.DarkSurfaceElevated
import com.sih.sankatlink.ui.theme.EmergencyRed
import com.sih.sankatlink.ui.theme.SignalGreen
import com.sih.sankatlink.ui.theme.TechCyan
import com.sih.sankatlink.ui.theme.TextPrimary
import com.sih.sankatlink.ui.theme.TextSecondary
import com.sih.sankatlink.ui.theme.CardBorder
import com.sih.sankatlink.ui.theme.DarkSurface
import com.sih.sankatlink.ui.theme.DarkSurfaceElevated
import com.sih.sankatlink.ui.theme.EmergencyRed
import com.sih.sankatlink.ui.theme.SignalGreen
import com.sih.sankatlink.ui.theme.TechCyan
import com.sih.sankatlink.ui.theme.TextPrimary
import com.sih.sankatlink.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectorDialog(
    title: String,
    currentSelection: IndianLanguage,
    onLanguageSelected: (IndianLanguage) -> Unit,
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
                        text = title,
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "10 Indian Languages with 100% Offline Models",
                        text = "10 Indian Languages • Offline On-Device",
                        color = TextSecondary,
                        fontSize = 12.sp
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
            Spacer(modifier = Modifier.height(14.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(IndianLanguage.values()) { lang ->
                    val isSelected = lang == currentSelection
                    LanguageCard(
                        language = lang,
                        isSelected = isSelected,
                        onClick = {
                            onLanguageSelected(lang)
                            onDismiss()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun LanguageCard(
    language: IndianLanguage,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) TechCyan else CardBorder
    val bgColor = if (isSelected) TechCyan.copy(alpha = 0.12f) else DarkSurfaceElevated

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(if (isSelected) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = language.nativeName,
                    color = if (isSelected) TechCyan else TextPrimary,
                    fontSize = 16.sp,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${language.displayName} (${language.isoCode.uppercase()})",
                    text = "${language.displayName} (${language.scriptName})",
                    color = TextSecondary,
                    fontSize = 11.sp
                    fontSize = 10.5.sp
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = TechCyan,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

