package com.rs.mymap.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.week10_mapdirection.ui.theme.Week10_MapDirectionTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.*
import com.rs.mymap.data.api.RetrofitClient
import com.rs.mymap.data.model.*
import kotlinx.coroutines.launch

@Composable
fun MapScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val apiKey = "AIzaSyAMxzfxQjAg9Jr-WE5EtBpAE7xCXwz2B1Q"

    // Initial Coordinates
    val unmerMalang = remember { LatLng(-7.9729917, 112.6093472) }
    val balaiKotaMalang = remember { LatLng(-7.9776606, 112.6340866) }

    // State for inputs
    var originInput by remember { mutableStateOf("-7.9729917, 112.6093472") }
    var destinationInput by remember { mutableStateOf("-7.9776606, 112.6340866") }
    
    // State for data
    var allRoutes by remember { mutableStateOf<List<Route>>(emptyList()) }
    var selectedRouteIndex by remember { mutableIntStateOf(0) }
    var polylinePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var distance by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var travelMode by remember { mutableStateOf("driving") }
    
    // Marker states
    var originLatLng by remember { mutableStateOf(unmerMalang) }
    var destinationLatLng by remember { mutableStateOf(balaiKotaMalang) }
    
    // Autocomplete states
    var originPredictions by remember { mutableStateOf<List<Prediction>>(emptyList()) }
    var destinationPredictions by remember { mutableStateOf<List<Prediction>>(emptyList()) }
    var isOriginDropdownExpanded by remember { mutableStateOf(false) }
    var isDestinationDropdownExpanded by remember { mutableStateOf(false) }
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(unmerMalang, 14f)
    }

    fun fetchAutocomplete(input: String, isOrigin: Boolean) {
        Log.d("MapScreen", "fetchAutocomplete: input=$input, isOrigin=$isOrigin")
        if (input.length < 3) {
            Log.d("MapScreen", "fetchAutocomplete: input too short")
            if (isOrigin) originPredictions = emptyList() else destinationPredictions = emptyList()
            if (isOrigin) isOriginDropdownExpanded = false else isDestinationDropdownExpanded = false
            return
        }
        scope.launch {
            try {
                val response = RetrofitClient.getClient(context).getAutocomplete(input, apiKey)
                Log.d("MapScreen", "fetchAutocomplete: status=${response.status}, predictionsCount=${response.predictions.size}")
                if (response.status == "OK") {
                    if (isOrigin) {
                        originPredictions = response.predictions
                        isOriginDropdownExpanded = response.predictions.isNotEmpty()
                        Log.d("MapScreen", "Origin predictions updated: ${originPredictions.size}, expanded=$isOriginDropdownExpanded")
                    } else {
                        destinationPredictions = response.predictions
                        isDestinationDropdownExpanded = response.predictions.isNotEmpty()
                        Log.d("MapScreen", "Destination predictions updated: ${destinationPredictions.size}, expanded=$isDestinationDropdownExpanded")
                    }
                } else {
                    Log.w("MapScreen", "Autocomplete status not OK: ${response.status}")
                }
            } catch (e: Exception) {
                Log.e("MapScreen", "Autocomplete error", e)
            }
        }
    }

    fun selectPlace(prediction: Prediction, isOrigin: Boolean) {
        scope.launch {
            try {
                val response = RetrofitClient.getClient(context).getPlaceDetails(prediction.placeId, apiKey)
                if (response.status == "OK") {
                    val loc = response.result.geometry.location
                    val latLng = LatLng(loc.lat, loc.lng)
                    if (isOrigin) {
                        originInput = prediction.description
                        originLatLng = latLng
                        isOriginDropdownExpanded = false
                    } else {
                        destinationInput = prediction.description
                        destinationLatLng = latLng
                        isDestinationDropdownExpanded = false
                    }
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            } catch (e: Exception) {
                Log.e("MapScreen", "Place details error", e)
            }
        }
    }

    fun selectRoute(route: Route, index: Int) {
        selectedRouteIndex = index
        val encodedPolyline = route.overviewPolyline.points
        val decodedPoints = PolyUtil.decode(encodedPolyline)
        polylinePoints = decodedPoints
        
        if (route.legs.isNotEmpty()) {
            val leg = route.legs[0]
            distance = leg.distance.text
            duration = leg.duration.text
            
            originLatLng = decodedPoints.first()
            destinationLatLng = decodedPoints.last()
            
            scope.launch {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(originLatLng, 15f)
                )
            }
        }
    }

    // Function to fetch directions
    fun findRoute() {
        scope.launch {
            try {
                isLoading = true
                // Clean spaces
                val origin = originInput.replace(" ", "")
                val destination = destinationInput.replace(" ", "")
                
                val response = RetrofitClient.getClient(context).getDirections(
                    origin = origin,
                    destination = destination,
                    apiKey = apiKey,
                    mode = travelMode
                )

                if (response.routes.isNotEmpty()) {
                    allRoutes = response.routes
                    selectRoute(response.routes[0], 0)
                    isBottomSheetVisible = true
                } else {
                    Toast.makeText(context, "Rute tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MapScreen", "Error fetching directions", e)
                Toast.makeText(context, "Gagal memuat rute. Periksa koneksi internet.", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }

    fun resetLocation() {
        originInput = "-7.9729917, 112.6093472"
        destinationInput = "-7.9776606, 112.6340866"
        originLatLng = unmerMalang
        destinationLatLng = balaiKotaMalang
        allRoutes = emptyList()
        polylinePoints = emptyList()
        distance = ""
        duration = ""
        isBottomSheetVisible = false
        selectedRouteIndex = 0
        travelMode = "driving"
        scope.launch {
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(unmerMalang, 14f))
        }
    }

    // Initial fetch
    LaunchedEffect(Unit) {
        findRoute()
    }

    MapScreenContent(
        modifier = modifier,
        originInput = originInput,
        onOriginInputChange = { 
            originInput = it
            fetchAutocomplete(it, true)
        },
        destinationInput = destinationInput,
        onDestinationInputChange = { 
            destinationInput = it
            fetchAutocomplete(it, false)
        },
        originPredictions = originPredictions,
        destinationPredictions = destinationPredictions,
        isOriginDropdownExpanded = isOriginDropdownExpanded,
        onOriginDropdownExpandedChange = { isOriginDropdownExpanded = it },
        isDestinationDropdownExpanded = isDestinationDropdownExpanded,
        onDestinationDropdownExpandedChange = { isDestinationDropdownExpanded = it },
        onPredictionClick = { prediction, isOrigin -> selectPlace(prediction, isOrigin) },
        isLoading = isLoading,
        onFindRouteClick = { findRoute() },
        allRoutes = allRoutes,
        selectedRouteIndex = selectedRouteIndex,
        onRouteSelected = { route, index -> selectRoute(route, index) },
        isBottomSheetVisible = isBottomSheetVisible,
        onBottomSheetDismiss = { isBottomSheetVisible = false },
        distance = distance,
        duration = duration,
        polylinePoints = polylinePoints,
        originLatLng = originLatLng,
        destinationLatLng = destinationLatLng,
        cameraPositionState = cameraPositionState,
        travelMode = travelMode,
        onTravelModeChange = { 
            travelMode = it
            if (allRoutes.isNotEmpty()) {
                findRoute()
            }
        },
        onResetClick = { resetLocation() },
        onShowBottomSheet = { isBottomSheetVisible = true }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenContent(
    modifier: Modifier = Modifier,
    originInput: String,
    onOriginInputChange: (String) -> Unit,
    destinationInput: String,
    onDestinationInputChange: (String) -> Unit,
    originPredictions: List<Prediction>,
    destinationPredictions: List<Prediction>,
    isOriginDropdownExpanded: Boolean,
    onOriginDropdownExpandedChange: (Boolean) -> Unit,
    isDestinationDropdownExpanded: Boolean,
    onDestinationDropdownExpandedChange: (Boolean) -> Unit,
    onPredictionClick: (Prediction, Boolean) -> Unit,
    isLoading: Boolean,
    onFindRouteClick: () -> Unit,
    allRoutes: List<Route>,
    selectedRouteIndex: Int,
    onRouteSelected: (Route, Int) -> Unit,
    isBottomSheetVisible: Boolean,
    onBottomSheetDismiss: () -> Unit,
    onShowBottomSheet: () -> Unit,
    distance: String,
    duration: String,
    polylinePoints: List<LatLng>,
    originLatLng: LatLng,
    destinationLatLng: LatLng,
    cameraPositionState: CameraPositionState,
    travelMode: String,
    onTravelModeChange: (String) -> Unit,
    onResetClick: () -> Unit
) {
    val uiSettings = remember { MapUiSettings(zoomControlsEnabled = false) }
    val originMarkerState = rememberMarkerState(position = originLatLng)
    val destinationMarkerState = rememberMarkerState(position = destinationLatLng)
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(originLatLng) {
        originMarkerState.position = originLatLng
    }
    LaunchedEffect(destinationLatLng) {
        destinationMarkerState.position = destinationLatLng
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings
        ) {
            Marker(
                state = originMarkerState,
                title = "Titik Asal",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )
            Marker(
                state = destinationMarkerState,
                title = "Titik Tujuan"
            )
            
            if (polylinePoints.isNotEmpty()) {
                Polyline(
                    points = polylinePoints,
                    color = Color(0xFF1A73E8),
                    width = 12f
                )
            }
        }

        // Top Input Card
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Navigasi RafliMap",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onResetClick) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset")
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val modes = listOf(
                        "driving" to Icons.Default.DirectionsCar,
                        "bicycling" to Icons.AutoMirrored.Filled.DirectionsBike,
                        "walking" to Icons.AutoMirrored.Filled.DirectionsWalk
                    )
                    modes.forEach { (mode, icon) ->
                        FilterChip(
                            selected = travelMode == mode,
                            onClick = { onTravelModeChange(mode) },
                            label = { Icon(icon, contentDescription = mode) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = isOriginDropdownExpanded,
                    onExpandedChange = { onOriginDropdownExpandedChange(it) }
                ) {
                    OutlinedTextField(
                        value = originInput,
                        onValueChange = onOriginInputChange,
                        label = { Text("Titik Asal (lat, lng)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, true),
                        leadingIcon = {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.Blue
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (originPredictions.isNotEmpty()) {
                        ExposedDropdownMenu(
                            expanded = isOriginDropdownExpanded,
                            onDismissRequest = { onOriginDropdownExpandedChange(false) }
                        ) {
                            originPredictions.forEach { prediction ->
                                DropdownMenuItem(
                                    text = { Text(prediction.description) },
                                    onClick = { onPredictionClick(prediction, true) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = isDestinationDropdownExpanded,
                    onExpandedChange = { onDestinationDropdownExpandedChange(it) }
                ) {
                    OutlinedTextField(
                        value = destinationInput,
                        onValueChange = onDestinationInputChange,
                        label = { Text("Titik Tujuan (lat, lng)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, true),
                        leadingIcon = {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (destinationPredictions.isNotEmpty()) {
                        ExposedDropdownMenu(
                            expanded = isDestinationDropdownExpanded,
                            onDismissRequest = { onDestinationDropdownExpandedChange(false) }
                        ) {
                            destinationPredictions.forEach { prediction ->
                                DropdownMenuItem(
                                    text = { Text(prediction.description) },
                                    onClick = { onPredictionClick(prediction, false) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                
                Button(
                    onClick = onFindRouteClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Icon(Icons.Default.Search, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cari Rute")
                }
            }
        }

        // Bottom Info Card (Stats)
        AnimatedVisibility(
            visible = !isLoading && distance.isNotEmpty() && !isBottomSheetVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onShowBottomSheet() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Jarak", style = MaterialTheme.typography.labelMedium)
                        Text(distance, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                    VerticalDivider(modifier = Modifier.height(40.dp), thickness = 1.dp)
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Waktu", style = MaterialTheme.typography.labelMedium)
                        Text(duration, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // FAB to show route options
        AnimatedVisibility(
            visible = allRoutes.isNotEmpty() && !isBottomSheetVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = if (distance.isNotEmpty()) 100.dp else 0.dp)
        ) {
            FloatingActionButton(
                onClick = onShowBottomSheet,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Daftar Rute")
            }
        }

        if (isBottomSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = onBottomSheetDismiss,
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                ) {
                    Text(
                        text = "Pilihan Rute",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                    
                    LazyColumn {
                        itemsIndexed(allRoutes) { index, route ->
                            val isSelected = index == selectedRouteIndex
                            val leg = route.legs.firstOrNull()
                            
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onRouteSelected(route, index) }
                                    .padding(horizontal = 20.dp, vertical = 12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val modeIcon = when (travelMode) {
                                        "walking" -> Icons.AutoMirrored.Filled.DirectionsWalk
                                        "bicycling" -> Icons.AutoMirrored.Filled.DirectionsBike
                                        else -> Icons.Default.DirectionsCar
                                    }
                                    Icon(
                                        imageVector = modeIcon,
                                        contentDescription = null,
                                        tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Rute ${index + 1}: via ${route.summary}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified
                                        )
                                        Text(
                                            text = "${leg?.duration?.text ?: ""} • ${leg?.distance?.text ?: ""}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                            if (index < allRoutes.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 20.dp),
                                    thickness = 0.5.dp,
                                    color = Color.LightGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    val sampleRoutes = listOf(
        Route(
            summary = "Jl. Magelang",
            overviewPolyline = OverviewPolyline(""),
            legs = listOf(
                Leg(
                    distance = TextValue("2.5 km", 2500),
                    duration = TextValue("10 min", 600),
                    startAddress = "",
                    endAddress = ""
                )
            )
        ),
        Route(
            summary = "Jl. Ahmad Yani",
            overviewPolyline = OverviewPolyline(""),
            legs = listOf(
                Leg(
                    distance = TextValue("3.2 km", 3200),
                    duration = TextValue("15 min", 900),
                    startAddress = "",
                    endAddress = ""
                )
            )
        )
    )

    Week10_MapDirectionTheme {
        MapScreenContent(
            originInput = "-7.9729917, 112.6093472",
            onOriginInputChange = {},
            destinationInput = "-7.9776606, 112.6340866",
            onDestinationInputChange = {},
            originPredictions = emptyList(),
            destinationPredictions = emptyList(),
            isOriginDropdownExpanded = false,
            onOriginDropdownExpandedChange = {},
            isDestinationDropdownExpanded = false,
            onDestinationDropdownExpandedChange = {},
            onPredictionClick = { _, _ -> },
            isLoading = false,
            onFindRouteClick = {},
            allRoutes = sampleRoutes,
            selectedRouteIndex = 0,
            onRouteSelected = { _, _ -> },
            isBottomSheetVisible = false,
            onBottomSheetDismiss = {},
            onShowBottomSheet = {},
            distance = "2.5 km",
            duration = "10 min",
            polylinePoints = listOf(
                LatLng(-7.9729917, 112.6093472),
                LatLng(-7.9776606, 112.6340866)
            ),
            originLatLng = LatLng(-7.9729917, 112.6093472),
            destinationLatLng = LatLng(-7.9776606, 112.6340866),
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(LatLng(-7.9729917, 112.6093472), 14f)
            },
            travelMode = "driving",
            onTravelModeChange = {},
            onResetClick = {}
        )
    }
}
