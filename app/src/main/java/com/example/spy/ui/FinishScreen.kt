package com.example.spy.ui

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spy.viewmodel.GameViewModel
import com.example.spy.ui.theme.*

@Composable
fun FinishScreen(viewModel: GameViewModel, vibrator: Vibrator) {
    LaunchedEffect(Unit) {
        if (viewModel.timeLeft == 0L) {
            val pattern = longArrayOf(0, 500, 200, 500)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, 0)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(CompassDarkBg).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (viewModel.timeLeft == 0L) "Time is up" else "Game stopped",
            color = if (viewModel.timeLeft == 0L) Color.Red else CompassBlueAccent,
            fontSize = 32.sp,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Spies were players:",
            color = CompassTextGray,
            fontSize = 14.sp
        )

        Text(
            text = viewModel.spyNumbers.joinToString(", "),
            color = Color.White,
            fontSize = 40.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Spacer(modifier = Modifier.height(64.dp))

        Surface(
            onClick = { viewModel.backToMenu(vibrator) },
            modifier = Modifier.fillMaxWidth().height(80.dp),
            color = CompassCardBg,
            shape = RoundedCornerShape(0.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "Back to menu",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}