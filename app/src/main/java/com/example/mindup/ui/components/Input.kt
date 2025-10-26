package com.example.mindup.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mindup.ui.theme.DarkTextColor


@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            visualTransformation = visualTransformation,
            singleLine = true,
            isError = isError,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(64.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = if (isError) Color.Red else Color.Blue,
                unfocusedIndicatorColor = if (isError) Color.Red else Color(0xFFBDBDBD),
                disabledIndicatorColor = Color(0xFFEEEEEE),
                errorIndicatorColor = Color.Red,
                focusedTextColor = DarkTextColor,
                unfocusedTextColor = DarkTextColor,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                errorContainerColor = Color.White
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            ),
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 32.dp, top = 0.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun InputFieldPreview() {
    Input(
        value = "",
        onValueChange = {},
        label = "Correo electrónico",
        isError = true,
        errorMessage = "El correo es requeriodo."
    )
}
