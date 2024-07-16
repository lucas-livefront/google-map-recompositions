package com.example.mapsrecompositions

import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HalfBottomSheetScaffold(
    modifier: Modifier = Modifier,
    content: @Composable (State<PaddingValues>) -> Unit,
) {
    val density = LocalDensity.current
    val paddingValuesState = remember { mutableStateOf(PaddingValues(0.dp)) }
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val anchoredDraggableState = remember {
        AnchoredDraggableState(
            initialValue = Anchor.Collapsed,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimationSpec,
        )
    }

    BoxWithConstraints(
        modifier = modifier,
    ) {
        val peakPx = 50.dp.toPx()
        val maxHeightPx = maxHeight.toPx()

        LaunchedEffect(
            maxHeightPx,
            peakPx,
        ) {
            anchoredDraggableState.updateAnchors(
                DraggableAnchors {
                    Anchor.Collapsed at (maxHeightPx - peakPx)
                    Anchor.Expanded at maxHeightPx / 2
                }
            )
        }

        LaunchedEffect(anchoredDraggableState) {
            snapshotFlow { anchoredDraggableState.safeOffset }
                .onEach {
                    paddingValuesState.value = PaddingValues(
                        bottom = maxHeight - it.toDp(density),
                    )
                }
                .collect()
        }

        content(paddingValuesState)

        Surface(
            modifier = Modifier
                .offset { anchoredDraggableState.safeOffset.toIntOffset() }
                .anchoredDraggable(
                    state = anchoredDraggableState,
                    orientation = Orientation.Vertical,
                )
        ) {
            Spacer(
                modifier = Modifier
                    .background(
                        color = Color.Gray,
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                    )
                    .fillMaxSize()
            )
        }
    }
}

enum class Anchor {
    Collapsed,
    Expanded,
}

@OptIn(ExperimentalFoundationApi::class)
val <T> AnchoredDraggableState<T>.safeOffset: Float
    get() = this
        .offset
        .takeUnless { it.isNaN() }
        ?: 0f