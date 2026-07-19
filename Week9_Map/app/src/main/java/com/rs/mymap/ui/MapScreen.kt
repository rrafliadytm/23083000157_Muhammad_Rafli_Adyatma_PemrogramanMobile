package com.rs.mymap.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import androidx.compose.ui.tooling.preview.Preview
import com.example.week10_map.ui.theme.Week10_MapTheme
import com.google.maps.android.compose.*
import com.rs.mymap.data.api.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val unmerMalang = LatLng(-7.9729917, 112.6093472)
    val balaiKotaMalang = LatLng(-7.9776606, 112.6340866)
    val malangCenter = LatLng(-7.975, 112.622)
    
    val apiKey = "AIzaSyAMxzfxQjAg9Jr-WE5EtBpAE7xCXwz2B1Q"
    
    var routePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(malangCenter, 14f)
    }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val originStr = "${unmerMalang.latitude},${unmerMalang.longitude}"
                val destStr = "${balaiKotaMalang.latitude},${balaiKotaMalang.longitude}"
                
                val response = RetrofitClient.getClient(context).getDirections(
                    origin = originStr,
                    destination = destStr,
                    apiKey = apiKey
                )
                
                if (response.routes.isNotEmpty()) {
                    val encodedPoints = response.routes[0].overviewPolyline.points
                    val decodedPath = PolyUtil.decode(encodedPoints)
                    routePoints = decodedPath
                    
                    // Log each point to Logcat
                    decodedPath.forEachIndexed { index, latLng ->
                        Log.d("MapRS_Route", "Point $index: ${latLng.latitude}, ${latLng.longitude}")
                    }
                }
            } catch (e: Exception) {
                Log.e("MapRS_Error", "Failed to fetch directions", e)
            }
        }
    }

    MapScreenContent(
        routePoints = routePoints,
        cameraPositionState = cameraPositionState,
        unmerMalang = unmerMalang,
        balaiKotaMalang = balaiKotaMalang
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenContent(
    routePoints: List<LatLng>,
    cameraPositionState: CameraPositionState,
    unmerMalang: LatLng,
    balaiKotaMalang: LatLng
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text("RafliMap", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = true)
            ) {
                Marker(
                    state = MarkerState(position = unmerMalang),
                    title = "Universitas Merdeka Malang",
                    snippet = "Titik Asal"
                )
                
                Marker(
                    state = MarkerState(position = balaiKotaMalang),
                    title = "Balai Kota Malang",
                    snippet = "Titik Tujuan"
                )
                
                if (routePoints.isNotEmpty()) {
                    Polyline(
                        points = routePoints,
                        color = Color.Blue,
                        width = 10f
                    )
                }
            }
            
            // Modern Floating Information Card
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Rute: Unmer -> Balai Kota",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Menampilkan rute terbaik di Kota Malang",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    val unmerMalang = LatLng(-7.9729917, 112.6093472)
    val balaiKotaMalang = LatLng(-7.9776606, 112.6340866)
    val malangCenter = LatLng(-7.975, 112.622)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(malangCenter, 14f)
    }
    val routePoints = listOf(
        unmerMalang,
        LatLng(-7.975, 112.615),
        LatLng(-7.976, 112.625),
        balaiKotaMalang
    )

    Week10_MapTheme {
        MapScreenContent(
            routePoints = routePoints,
            cameraPositionState = cameraPositionState,
            unmerMalang = unmerMalang,
            balaiKotaMalang = balaiKotaMalang
        )
    }
}
