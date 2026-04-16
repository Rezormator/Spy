package com.example.spy.ui

import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun GameScreen(viewModel: GameViewModel, vibrator: Vibrator) {
    val m = viewModel.timeLeft / 60
    val s = viewModel.timeLeft % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CompassDarkBg)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = CompassBlueAccent,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "game in progress",
                color = CompassBlueAccent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Text(
            text = String.format("%02d:%02d", m, s),
            fontSize = 80.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.ExtraLight,
            color = Color.White,
            modifier = Modifier.padding(vertical = 40.dp)
        )

        Spacer(modifier = Modifier.height(64.dp))

        Surface(
            onClick = { viewModel.stopGameManually() },
            modifier = Modifier.fillMaxWidth().height(80.dp),
            color = CompassCardBg,
            shape = RoundedCornerShape(0.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Stop game",
                        color = Color.Red.copy(alpha = 0.7f),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}