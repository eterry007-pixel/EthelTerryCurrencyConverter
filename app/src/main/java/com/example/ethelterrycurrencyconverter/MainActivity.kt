package com.example.ethelterrycurrencyconverter

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
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
                val isMusicEnabled by viewModel.isMusicEnabled
                val context = LocalContext.current

                DisposableEffect(isMusicEnabled) {
                    if (isMusicEnabled) {
                        // Correctly identifying the resource in the 'raw' folder
                        val resId = context.resources.getIdentifier("background_music", "raw", context.packageName)
                        if (resId != 0) {
                            try {
                                mediaPlayer = MediaPlayer.create(context, resId).apply {
                                    isLooping = true
                                    start()
                                }
                            } catch (e: Exception) {
                                Log.e("MusicError", "Error starting music: ${e.message}")
                            }
                        } else {
                            Log.e("MusicError", "Resource background_music not found")
                        }
                    } else {
                        stopMusic()
                    }
                    onDispose {
                        stopMusic()
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

    private fun stopMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
    }
}
