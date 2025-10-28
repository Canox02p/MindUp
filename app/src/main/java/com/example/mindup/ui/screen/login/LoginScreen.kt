package com.example.mindup.ui.screen.login
import com.example.mindup.R
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    // Arranca animación
    var start by remember { mutableStateOf(false) }

    // Alpha (0 -> 1)
    val alpha by animateFloatAsState(
        targetValue = if (start) 1f else 0f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "alpha"
    )

    // Scale con pequeño “bounce” (0.8 -> 1.05 -> 1.0)
    var scaleTarget by remember { mutableStateOf(0.8f) }
    val scale by animateFloatAsState(
        targetValue = scaleTarget,
        animationSpec = tween(durationMillis = 850, easing = FastOutSlowInEasing),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        start = true
        scaleTarget = 1.05f
        delay(300)
        scaleTarget = 1.0f
        // Espera total visible ~1.4s y navega a login
        delay(1100)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Reemplaza R.drawable.logo por tu recurso
        Image(
            painter = painterResource(id =R.drawable.logo_mindup),
            contentDescription = "Logo",
            modifier = Modifier
                .size(160.dp)
                .scale(scale)
                .alpha(alpha)
        )
    }
}


