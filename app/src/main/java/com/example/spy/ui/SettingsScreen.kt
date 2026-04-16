package com.example.spy.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import com.example.spy.viewmodel.*
import com.example.spy.ui.theme.*

@Composable
fun SettingsScreen(viewModel: GameViewModel) {
    var sP by remember { mutableStateOf(false) }
    var sS by remember { mutableStateOf(false) }
    var sT by remember { mutableStateOf(false) }
    var sL by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(CompassDarkBg).padding(24.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("game", color = CompassBlueAccent, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text("Spy", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Light)
        }

        Column(modifier = Modifier.fillMaxWidth().background(CompassCardBg)) {
            TacticalRow(Icons.Default.Group, "Players", "${viewModel.playersCount}") { sP = true }
            HorizontalDivider(color = CompassBorder, thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
            TacticalRow(Icons.Default.PersonSearch, "Spies", "${viewModel.spiesCount}") { sS = true }
            HorizontalDivider(color = CompassBorder, thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
            TacticalRow(Icons.Default.Timer, "Time", "${viewModel.gameMinutes}m") { sT = true }
            HorizontalDivider(color = CompassBorder, thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
            TacticalRow(Icons.Default.LocationOn, "Locations", "${viewModel.locations.size}") { sL = true }
        }

        Spacer(Modifier.weight(1f))

        Surface(
            onClick = { viewModel.startGame() },
            modifier = Modifier.fillMaxWidth().height(80.dp),
            color = CompassCardBg,
            shape = RoundedCornerShape(0.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("Start game", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Normal)
            }
        }
    }

    if (sP) SelectionDialog("Players", 3..100, viewModel.playersCount, { sP = false }) { viewModel.playersCount = it }
    if (sS) SelectionDialog("Spies", 1..(viewModel.playersCount - 1), viewModel.spiesCount, { sS = false }) { viewModel.spiesCount = it }
    if (sT) SelectionDialog("Time", 1..60, viewModel.gameMinutes, { sT = false }) { viewModel.gameMinutes = it }
    if (sL) LocationsDialog(viewModel) { sL = false }
}

@Composable
fun TacticalRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = CompassBlueAccent, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(16.dp))
            Text(label, color = CompassTextGray, fontSize = 14.sp)
        }
        Text(value, color = Color.White, fontSize = 20.sp, fontFamily = FontFamily.Monospace)
    }
}