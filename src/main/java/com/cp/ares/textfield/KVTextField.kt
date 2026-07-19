package com.cp.ares.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AresKvTextField(modifier: Modifier = Modifier) {
    var key by rememberSaveable { mutableStateOf<String>("") }
    var value by rememberSaveable { mutableStateOf<String>("") }
    var addTitle by rememberSaveable { mutableStateOf<Boolean>(false) }
    var isSecured by rememberSaveable { mutableStateOf(false) }

    val titleFocusRequester = remember { FocusRequester() }

    // Balanced & Premium Palette
    val containerBgColor = Color(0xFFF8F9FA)
    val borderInactiveColor = Color(0xFFE5E7EB)
    val textPrimary = Color(0xFF1F2937)
    val textSecondary = Color(0xFF9CA3AF)

    // Cohesive Interactive Action Colors
    val lockSecuredColor = Color(0xFFF59E0B)   // Amber gold for data protection indicator
    val reminderBgColor = Color(0xFFEEF2FF)    // Light Indigo background tint
    val reminderContentColor = Color(0xFF4F46E5)// Clear Royal Indigo for the context guide reminder
    val sendButtonColor = Color(0xFF10B981)    // Vibrant Emerald Green for execution

    val windowShape = RoundedCornerShape(18.dp)

    val unifiedFieldColors = TextFieldDefaults.colors(
        focusedTextColor = textPrimary,
        unfocusedTextColor = textPrimary,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )
    val sharedTextStyle = TextStyle(fontSize = 16.sp)

    LaunchedEffect(addTitle) {
        if (addTitle) {
            titleFocusRequester.requestFocus()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(containerBgColor, windowShape)
            .border(1.dp, borderInactiveColor, windowShape)
    ) {

        // 1. Title Key Field (Prompt guide layout)
        if (addTitle) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(titleFocusRequester),
                maxLines = 2,
                textStyle = sharedTextStyle,
                placeholder = {
                    Text("Add Key / Context title...", color = textSecondary, fontSize = 16.sp)
                },
                colors = unifiedFieldColors,
                value = key,
                onValueChange = { text -> key = text }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = borderInactiveColor.copy(alpha = 0.5f),
                thickness = 1.dp
            )
        }

        // 2. Primary Value Field Window
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (focusState.isFocused && key.isBlank()) {
                        addTitle = false
                    }
                },
            maxLines = 4,
            textStyle = sharedTextStyle,
            value = value,
            onValueChange = { text -> value = text },
            placeholder = {
                Text("Add value...", color = textSecondary, fontSize = 16.sp)
            },
            colors = unifiedFieldColors,
            visualTransformation = if (isSecured) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                Row(
                    modifier = Modifier.padding(end = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Security Padlock
                    IconButton(
                        onClick = { isSecured = !isSecured },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isSecured) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = "Security State Toggle",
                            tint = if (isSecured) lockSecuredColor else textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    if (!addTitle) {
                        val isValuePopulated = value.isNotBlank()

                        // Action chip reminder - requires value to be present
                        Button(
                            enabled = isValuePopulated,
                            onClick = { addTitle = true },
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                            modifier = Modifier.size(width = 110.dp, height = 36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = reminderBgColor,
                                contentColor = reminderContentColor,
                                disabledContainerColor = reminderBgColor.copy(alpha = 0.4f),
                                disabledContentColor = reminderContentColor.copy(alpha = 0.3f)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Label,
                                contentDescription = "Reminder Label",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Add Key",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        // --- Added strict key verification restriction here ---
                        val isKeyPopulated = key.isNotBlank()

                        // Send Button: Remains visually faded until text is typed into the Key field
                        IconButton(
                            enabled = isKeyPopulated,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = sendButtonColor,
                                contentColor = Color.White,
                                disabledContainerColor = borderInactiveColor, // Balanced grey disabled backdrop
                                disabledContentColor = textSecondary.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier.size(36.dp),
                            onClick = { /* Submit key, value, isSecured payload */ }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Send,
                                contentDescription = "Transmit Statement",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AresKvTextFieldPrev() {
    AresKvTextField()
}