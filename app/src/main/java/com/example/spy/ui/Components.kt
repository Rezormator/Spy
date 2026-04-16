package com.example.spy.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.spy.viewmodel.GameViewModel
import com.example.spy.ui.theme.*
import kotlin.math.abs

@Composable
fun SelectionDialog(
    title: String,
    range: IntRange,
    currentValue: Int,
    onDismiss: () -> Unit,
    onValueChange: (Int) -> Unit
) {
    Dialog(onDismissRequest = {}, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(modifier = Modifier.fillMaxSize().background(CompassDarkBg)) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title.lowercase(),
                    color = CompassBlueAccent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    WheelNumberPicker(range, currentValue, onValueChange)
                }

                Surface(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    color = CompassCardBg,
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Confirm", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Normal)
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun WheelNumberPicker(range: IntRange, currentValue: Int, onValueChange: (Int) -> Unit) {
    val items = range.toList()
    val itemHeight = 80.dp
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = items.indexOf(currentValue).coerceAtLeast(0))
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val layoutInfo = listState.layoutInfo
            val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
            val closestItem = layoutInfo.visibleItemsInfo.minByOrNull { abs((it.offset + it.size / 2) - center) }
            closestItem?.let { onValueChange(items[it.index]) }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val vP = maxHeight / 2 - itemHeight / 2
        Surface(
            modifier = Modifier.fillMaxWidth(0.9f).height(itemHeight),
            color = CompassCardBg.copy(alpha = 0.7f),
            shape = RoundedCornerShape(0.dp)
        ) {}

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = vP),
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items.size) { index ->
                val isSelected = items[index] == currentValue
                Box(modifier = Modifier.height(itemHeight), contentAlignment = Alignment.Center) {
                    Text(
                        text = items[index].toString(),
                        fontSize = 40.sp,
                        color = if (isSelected) Color.White else CompassTextGray.copy(alpha = 0.3f),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = if (isSelected) FontWeight.Normal else FontWeight.Thin
                    )
                }
            }
        }
    }
}

@Composable
fun LocationsDialog(viewModel: GameViewModel, onDismiss: () -> Unit) {
    var newLocationName by remember { mutableStateOf("") }

    Dialog(onDismissRequest = {}, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(modifier = Modifier.fillMaxSize().background(CompassDarkBg)) {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                Text(
                    text = "locations",
                    color = CompassBlueAccent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 24.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = newLocationName,
                        onValueChange = { if (it.length <= 50) newLocationName = it },
                        label = { Text("new target (max 50)", color = CompassTextGray, fontSize = 14.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(0.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CompassBlueAccent,
                            unfocusedBorderColor = CompassBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = { viewModel.addLocation(newLocationName); newLocationName = "" },
                        modifier = Modifier.background(CompassBlueAccent, RoundedCornerShape(0.dp)).size(56.dp)
                    ) {
                        Icon(Icons.Default.Add, null, tint = Color.White)
                    }
                }

                LazyColumn(modifier = Modifier.weight(1f).padding(top = 16.dp)) {
                    items(viewModel.locations) { location ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    null,
                                    tint = CompassBlueAccent.copy(alpha = 0.6f),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    text = location,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Light,
                                    fontFamily = FontFamily.Monospace,
                                    lineHeight = 24.sp,
                                    softWrap = true,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            IconButton(
                                onClick = { viewModel.removeLocation(location) },
                                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Red)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Red.copy(alpha = 0.5f)
                                )
                            }
                        }
                        HorizontalDivider(color = CompassBorder)
                    }
                }

                Surface(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(80.dp).padding(top = 16.dp),
                    color = CompassCardBg,
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Back", color = Color.White, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}