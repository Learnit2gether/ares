package com.cp.ares.listitem


// --- Explicitly declaring core vector graphics paths locally to bypass external asset pack dependencies safely ---
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AresKvReadOnlyItem(
    keyText: String,
    valueText: String,
    isInitiallySecured: Boolean,
    modifier: Modifier = Modifier,
    onCopyTriggered: () -> Unit = {},
    onDeleteConfirmed: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {
    var isRevealed by rememberSaveable { mutableStateOf(!isInitiallySecured) }
    var showDeleteConfirmation by rememberSaveable { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val indication = LocalIndication.current

    val containerBgColor = Color(0xFFF8F9FA)
    val borderInactiveColor = Color(0xFFE5E7EB)
    val textPrimary = Color(0xFF1F2937)
    val textSecondary = Color(0xFF9CA3AF)
    val errorColor = MaterialTheme.colorScheme.error

    val windowShape = RoundedCornerShape(12.dp)

    val displayedValue = if (isInitiallySecured && !isRevealed) {
        "••••••••••••••••"
    } else {
        valueText
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .border(1.dp, borderInactiveColor, windowShape)
            .clip(windowShape)
            .combinedClickable(
                interactionSource,
                indication,
                onLongClick = { showDeleteConfirmation = true },
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onCopyTriggered()
                }
            ),
        color = containerBgColor,
        shape = windowShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .animateContentSize(animationSpec = tween(durationMillis = 200))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Fixed layout alignment prevents uneven cards sizes
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp)
                ) {
                    Text(
                        text = keyText.ifBlank { "Untitled" },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = displayedValue,
                        fontSize = 15.sp,
                        color = textPrimary,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (!showDeleteConfirmation) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isInitiallySecured) {
                            IconButton(
                                onClick = { isRevealed = !isRevealed },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isRevealed) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = "Toggle Visibility",
                                    tint = textSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        IconButton(onClick = onEditClick, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = textSecondary,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }
            }

            Crossfade(targetState = showDeleteConfirmation, label = "DeleteConfirmationInline") { isConfirming ->
                if (isConfirming) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Delete permanently?",
                            color = errorColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            TextButton(
                                onClick = { showDeleteConfirmation = false },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Cancel", fontSize = 12.sp, color = textSecondary)
                            }
                            Button(
                                onClick = {
                                    showDeleteConfirmation = false
                                    onDeleteConfirmed()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = errorColor),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("Delete", fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AresKvReadOnlyItemPreview() {
    MaterialTheme {
        Column {
            AresKvReadOnlyItem(
                keyText = "Username",
                valueText = "john_doe",
                isInitiallySecured = false
            )
            AresKvReadOnlyItem(
                keyText = "Password",
                valueText = "secret123",
                isInitiallySecured = true
            )
        }
    }
}
