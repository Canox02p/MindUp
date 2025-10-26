package com.example.mindup.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun ProfileMapComponent(
    modifier: Modifier = Modifier,
    interactive: Boolean = false
) {
    val upp = LatLng(20.35623, -99.02671)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(upp, 15f)
    }

    val ui = if (interactive) {
        MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = true
        )
    } else {

        MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = true,
            scrollGesturesEnabled = false,
            zoomGesturesEnabled = false,
            tiltGesturesEnabled = false
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = ui,
        properties = MapProperties(isMyLocationEnabled = false)
    ) {
        Marker(
            state = MarkerState(upp),
            title = "Casa",
            snippet = "Ubicación"
        )
    }
}

