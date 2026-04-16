package com.example.spy.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.spy.viewmodel.*
import com.example.spy.ui.theme.*

@Composable
fun RevealScreen(viewModel: GameViewModel) {
    val role = viewModel.roles[viewModel.currentPlayerIndex]
    val isSpy = role == "SPY"

    Column(
        modifier = Modifier.fillMaxSize().background(CompassDarkBg).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "player #${viewModel.currentPlayerIndex + 1}",
            color = CompassBlueAccent,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(40.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clickable {
                    if (!viewModel.isCardVisible) viewModel.isCardVisible = true else viewModel.nextPlayer()
                },
            color = when {
                !viewModel.isCardVisible -> CompassCardBg.copy(0.5f)
                isSpy -> Color(0xFF441111)
                else -> CompassCardBg
            },
            shape = RoundedCornerShape(0.dp),
            border = BorderStroke(1.dp, if (viewModel.isCardVisible && isSpy) Color.Red else CompassBorder)
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (viewModel.isCardVisible) {
                    Text(
                        text = role,
                        fontSize = 40.sp,
                        color = if (isSpy) Color.Red else Color.White,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = "Tap to reveal",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}