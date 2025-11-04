package com.example.mindup.ui.screen.pages

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
@Composable
fun MindUpTitle(
    text: String,
    sizeSp: Int = 26,
    weight: FontWeight = FontWeight.ExtraBold
) {
    val first = text.firstOrNull()?.toString() ?: ""
    val rest = if (text.length > 1) text.drop(1) else ""

    val dark = MaterialTheme.colorScheme.secondary
    val light = MaterialTheme.colorScheme.primary

    Text(
        buildAnnotatedString {
            withStyle(SpanStyle(color = dark, fontSize = sizeSp.sp, fontWeight = weight)) {
                append(first)
            }
            withStyle(SpanStyle(color = light, fontSize = sizeSp.sp, fontWeight = weight)) {
                append(rest)
            }
        }
    )
}
