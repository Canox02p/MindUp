package com.example.mindup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mindup.ui.screen.container.ScreenContainer
import com.example.mindup.ui.theme.MindUpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindUpTheme {
                ScreenContainer()
            }
        }
    }
}


