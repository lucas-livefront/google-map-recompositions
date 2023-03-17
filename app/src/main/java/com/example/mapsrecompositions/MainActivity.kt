@file:OptIn(ExperimentalMaterialApi::class)

package com.example.mapsrecompositions

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.mapsrecompositions.ui.theme.MapsRecompositionsTheme
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
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