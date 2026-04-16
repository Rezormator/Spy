package com.example.spy

import android.content.Context
import android.os.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.spy.ui.theme.SpyTheme
import com.example.spy.viewmodel.GameState
import com.example.spy.viewmodel.GameViewModel
import com.example.spy.ui.*

class MainActivity : ComponentActivity() {
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        setContent {
            SpyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    when (viewModel.currentState) {
                        GameState.SETTINGS -> SettingsScreen(viewModel)
                        GameState.REVEAL -> RevealScreen(viewModel)
                        GameState.GAME -> GameScreen(viewModel, vibrator)
                        GameState.FINISH -> FinishScreen(viewModel, vibrator)
                    }
                }
            }
        }
    }
}