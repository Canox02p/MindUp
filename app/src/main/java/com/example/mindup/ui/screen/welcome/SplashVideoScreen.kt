package com.example.mindup.ui.screen.welcome

import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.mindup.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun SplashVideoScreen(
    modifier: Modifier = Modifier,
    onFinished: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    // 1) Prepara el player
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val uri = Uri.parse("android.resource://${context.packageName}/${R.raw.animacion}")
            setMediaItem(MediaItem.fromUri(uri))
            repeatMode = Player.REPEAT_MODE_OFF
            volume = 0f // silenciar si quieres
            prepare()
        }
    }

    // 2) Overlay para el fade-out final
    val overlayAlpha = remember { Animatable(0f) }
    var fadeStarted by remember { mutableStateOf(false) }

    // 3) Listener para terminar y navegar
    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    onFinished()
                }
            }
        }
        exoPlayer.addListener(listener)
        exoPlayer.playWhenReady = true

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    // 4) Vigila el tiempo restante y dispara el fade-out en los últimos 600 ms
    LaunchedEffect(Unit) {
        while (isActive) {
            val dur = exoPlayer.duration
            val pos = exoPlayer.currentPosition
            if (!fadeStarted && dur > 0 && (dur - pos) in 1..600) {
                fadeStarted = true
                overlayAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 500) // velocidad del desvanecido
                )
                // Espera a que termine la animación; el onFinished se dispara al STATE_ENDED
            }
            delay(50)
        }
    }

    // 5) Player + overlay con fade
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    player = exoPlayer
                }
            }
        )

        // Overlay negro que sube su alpha (desvaneciendo el video)
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = overlayAlpha.value))
        )
    }
}
