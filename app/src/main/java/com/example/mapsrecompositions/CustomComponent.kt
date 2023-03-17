package com.example.mapsrecompositions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset

@Composable
fun CustomComposable(
    contentPaddingState: State<PaddingValues>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        Spacer(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp)
                .padding { contentPaddingState.value }
        ) {
            Text(
                text = "I can move :)",
                modifier = Modifier
                    .background(Color.Magenta)
                    .padding(8.dp)
            )
        }
    }
}

/**
 * Helper method to allow padding modifications to skip recompositions.
 */
fun Modifier.padding(
    paddingValuesProvider: () -> PaddingValues,
): Modifier = layout { measurable, constraints ->
    val paddingValues = paddingValuesProvider()
    val topPaddingPx = paddingValues.calculateTopPadding().roundToPx()
    val bottomPaddingPx = paddingValues.calculateBottomPadding().roundToPx()
    val leftPaddingPx = paddingValues.calculateLeftPadding(layoutDirection).roundToPx()
    val rightPaddingPx = paddingValues.calculateRightPadding(layoutDirection).roundToPx()

    val horizontal = (leftPaddingPx + rightPaddingPx).coerceAtLeast(0)
    val vertical = (topPaddingPx + bottomPaddingPx).coerceAtLeast(0)

    val placeable = measurable.measure(constraints.offset(-horizontal, -vertical))

    val layoutWidth = constraints.constrainWidth(placeable.width + horizontal)
    val layoutHeight = constraints.constrainHeight(placeable.height + vertical)

    layout(layoutWidth, layoutHeight) {
        placeable.placeRelative(leftPaddingPx, topPaddingPx)
    }
}