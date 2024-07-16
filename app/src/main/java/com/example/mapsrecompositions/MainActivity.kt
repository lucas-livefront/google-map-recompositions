@file:OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)

package com.example.mapsrecompositions

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.mapsrecompositions.ui.theme.MapsRecompositionsTheme
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

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
private fun AccessibilityExample() {
    val markerStateSydney = remember { MarkerState(position = LatLng(-34.0, 151.0)) }
    val markerStateTokyo = remember { MarkerState(position = LatLng(35.66, 139.6)) }

    BottomSheetScaffold(
        sheetContent = {
            Spacer(
                modifier = Modifier
                    .background(
                        color = Color.Gray,
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                    )
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            )
        },
    ) {
        GoogleMap(
            modifier = Modifier
                .clearAndSetSemantics { invisibleToUser() }
                .focusable()
                .fillMaxSize()
        ) {
            Marker(
                state = markerStateSydney,
                title = "Marker in Sydney",
            )
            Marker(
                state = markerStateTokyo,
                title = "Marker in Tokyo"
            )
        }
    }
}

@Composable
private fun AccessibilityTwoExample() {
    val markerStateSydney = remember { MarkerState(position = LatLng(-34.0, 151.0)) }
    val markerStateTokyo = remember { MarkerState(position = LatLng(35.66, 139.6)) }

    BottomSheetScaffold(
        sheetContent = {
            Spacer(
                modifier = Modifier
                    .background(
                        color = Color.Gray,
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                    )
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .semantics {
                        contentDescription = "I am a monster"
                        traversalIndex = 0f
                    }
            )
        },
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .semantics {
                    contentDescription = "I am a sheet"
                    traversalIndex = 1f
                }
        ) {
            Marker(
                state = markerStateSydney,
                title = "Marker in Sydney",
            )
            Marker(
                state = markerStateTokyo,
                title = "Marker in Tokyo"
            )
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
