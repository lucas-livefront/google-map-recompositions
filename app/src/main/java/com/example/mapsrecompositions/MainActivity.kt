@file:OptIn(ExperimentalMaterialApi::class)

package com.example.mapsrecompositions

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.mapsrecompositions.ui.theme.MapsRecompositionsTheme
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapsRecompositionsTheme {
                MapExample()
            }
        }
    }
}

@Composable
fun MapExample() {
    val markerStateSydney = remember { MarkerState(position = LatLng(-34.0, 151.0)) }
    val markerStateTokyo = remember { MarkerState(position = LatLng(35.66, 139.6)) }

    HalfBottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
    ) { contentPaddingState ->
        GoogleMap(
            contentPadding = contentPaddingState.value,
            modifier = Modifier.fillMaxSize(),
        ) {
            Log.d("GoogleMap", "Composition scope invalidated.")
            Marker(
                state = markerStateSydney,
                title = "Marker in Sydney"
            )
            Marker(
                state = markerStateTokyo,
                title = "Marker in Tokyo"
            )
        }
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapEffectExample() {
    val markerStateSydney = remember { MarkerState(position = LatLng(-34.0, 151.0)) }
    val markerStateTokyo = remember { MarkerState(position = LatLng(35.66, 139.6)) }
    val density = LocalDensity.current

    HalfBottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
    ) { contentPaddingState ->
        GoogleMap(modifier = Modifier.fillMaxSize()) {
            MapEffect(contentPaddingState.value) { map ->
                val paddingValues = contentPaddingState.value
                map.setPadding(
                    paddingValues.calculateLeftPadding(LayoutDirection.Ltr).roundToPx(density),
                    paddingValues.calculateTopPadding().roundToPx(density),
                    paddingValues.calculateRightPadding(LayoutDirection.Ltr).roundToPx(density),
                    paddingValues.calculateBottomPadding().roundToPx(density),
                )
            }
            Log.d("GoogleMap", "Composition scope invalidated.")
            Marker(
                state = markerStateSydney,
                title = "Marker in Sydney"
            )
            Marker(
                state = markerStateTokyo,
                title = "Marker in Tokyo"
            )
        }
    }
}

@Composable
fun CustomComponentExample() {
    HalfBottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
    ) { contentPaddingState ->
        CustomComposable(
            contentPaddingState = contentPaddingState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

enum class SwipeableAnchor { Left, Right }

@Composable
fun SwipeableExample() {
    val widthDp = 200.dp
    val heightDp = 100.dp
    val swipeableSizeDp = heightDp
    val swipeableSizePx = swipeableSizeDp.toPx()

    val swipeableState = rememberSwipeableState(initialValue = SwipeableAnchor.Left)
    val scope = rememberCoroutineScope()


    Box(
        modifier = Modifier
            .background(color = Color.LightGray)
            .size(width = widthDp, height = heightDp)
    ) {
        val anchors = remember {
            mapOf(
                0f to SwipeableAnchor.Left,
                swipeableSizePx to SwipeableAnchor.Right,
            )
        }

        Spacer(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(swipeableSizeDp)
                .offset {
                    IntOffset(
                        x = swipeableState.offset.value.roundToInt(),
                        y = 0,
                    )
                }
                .swipeable(
                    anchors = anchors,
                    state = swipeableState,
                    orientation = Orientation.Horizontal,
                )
                .background(color = Color.Blue)
                .clickable {
                    scope.launch {
                        scope
                            .launch {
                                Log.d("DAVID", "animate start")
                                swipeableState.animateTo(SwipeableAnchor.Right, tween(4000))
                                Log.d("DAVID", "animate end")
                            }
                            .invokeOnCompletion {
                                Log.d("DAVID", "animate completed ${it?.message}")
                            }
                        delay(100)
                        scope
                            .launch {
                                Log.d("DAVID", "snap start ${swipeableState.isAnimationRunning}")
                                swipeableState.snapTo(SwipeableAnchor.Left)
                                Log.d("DAVID", "snap end")
                            }
                            .invokeOnCompletion {
                                Log.d("DAVID", "snap completed ${it?.message}")
                            }
                    }
                }
        )
    }
}