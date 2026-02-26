package com.example.ethelterrycurrencyconverter

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ethelterrycurrencyconverter.ui.MainScreen
import com.example.ethelterrycurrencyconverter.ui.theme.EthelTerryCurrencyConverterTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CurrencyViewModel by viewModels()
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EthelTerryCurrencyConverterTheme {
                val isMusicEnabled = viewModel.isMusicEnabled.value
                val context = LocalContext.current

                DisposableEffect(isMusicEnabled) {
                    if (isMusicEnabled) {
                        val resId = context.resources.getIdentifier("background_music", "raw", context.packageName)
                        if (resId != 0) {
                            mediaPlayer = MediaPlayer.create(context, resId).apply {
                                isLooping = true
                                start()
                            }
                        }
                    } else {
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null
                    }
                    onDispose {
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
