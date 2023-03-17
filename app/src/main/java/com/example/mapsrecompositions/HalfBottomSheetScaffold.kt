package com.example.mapsrecompositions

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation.Vertical
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Surface
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
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
import com.example.mapsrecompositions.Anchor.Collapsed
import com.example.mapsrecompositions.Anchor.Expanded
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HalfBottomSheetScaffold(
    modifier: Modifier = Modifier,
    swipeableState: SwipeableState<Anchor> = rememberSwipeableState(initialValue = Collapsed),
    content: @Composable (State<PaddingValues>) -> Unit,
) {
    val density = LocalDensity.current
    val paddingValuesState = remember { mutableStateOf(PaddingValues(0.dp)) }

    BoxWithConstraints {
        val peakPx = 50.dp.toPx()
        val maxHeightPx = maxHeight.toPx()

        val anchors = remember(maxHeightPx, peakPx) {
            mapOf(
                maxHeightPx - peakPx to Collapsed,
                maxHeightPx / 2 to Expanded,
            )
        }

        LaunchedEffect(swipeableState) {
            snapshotFlow { swipeableState.offset.value }
                .onEach {
                    paddingValuesState.value = PaddingValues(
                        bottom = maxHeight - it.toDp(density),
                    )
                }
                .collect()
        }

        content(paddingValuesState)

        Surface(
            modifier = modifier
                .offset { swipeableState.offset.value.toIntOffset() }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    orientation = Vertical,
                    resistance = null,
                    thresholds = { _, _ -> FractionalThreshold(fraction = 0.5f) },
                ),
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