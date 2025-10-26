package com.example.mindup.ui.components


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun ProfileMapComponent(
    modifier: Modifier = Modifier,
    interactive: Boolean = false // 👈 por defecto el mapa no roba el scroll
) {
    val upp = LatLng(20.35623, -99.02671) // 📍 Coordenadas de tu casa o ubicación
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(upp, 15f)
    }

    // Configuración de gestos del mapa
    val ui = if (interactive) {
        MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = true
        )
    } else {
        // 👇 Sin gestos, para que no interfiera con el scroll de la pantalla
        MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = true,
            scrollGesturesEnabled = false,
            zoomGesturesEnabled = false,
            tiltGesturesEnabled = false
        )
    }

    GoogleMap(
        modifier = modifier, // el tamaño lo controla el contenedor padre
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

