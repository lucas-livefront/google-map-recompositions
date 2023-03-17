package com.example.mapsrecompositions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

/**
 * A helper for converting a [Float] into an [IntOffset].
 */
fun Float.toIntOffset(): IntOffset = IntOffset(
    x = 0,
    y = roundToInt(),
)

/**
 * A function for converting [Dp] to pixels.
 */
@Composable
fun Dp.toPx(): Float = with(LocalDensity.current) { toPx() }

/**
 * A function for converting float pixels to [Dp].
 */
fun Float.toDp(density: Density): Dp = with(density) { this@toDp.toDp() }
