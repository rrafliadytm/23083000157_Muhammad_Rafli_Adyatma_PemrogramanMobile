package com.example.week6_weatherforecast.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.week6_weatherforecast.data.model.DayForecast
import com.example.week6_weatherforecast.data.model.GeocodingResult
import com.example.week6_weatherforecast.ui.state.WeatherUiState
import com.example.week6_weatherforecast.ui.theme.Week6_WeatherForecastTheme
import com.example.week6_weatherforecast.ui.viewmodel.WeatherViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    WeatherScreenContent(
        uiState = uiState,
        onSearch = { viewModel.fetchWeather(it) },
        onQueryChange = { viewModel.onSearchQueryChange(it) },
        onRefresh = { viewModel.fetchWeather(uiState.selectedCity?.name ?: "Malang", isRefreshing = true) },
        onRetry = { viewModel.fetchWeather(it.ifBlank { "Malang" }) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreenContent(
    uiState: WeatherUiState,
    onSearch: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onRetry: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Update search query when a city is selected
    LaunchedEffect(uiState.selectedCity) {
        uiState.selectedCity?.let {
            searchQuery = it.name
        }
    }

    // Gradient background for a modern feel
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.surface
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Weather Forecast",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        uiState.selectedCity?.let { city ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Rounded.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${city.name}, ${city.country}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                    
                    Surface(
                        onClick = { /* Settings or Profile */ },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                // Modern Pill-shaped Search Bar with Dropdown Suggestions
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { 
                            searchQuery = it
                            onQueryChange(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp),
                        placeholder = { Text("Search city...") },
                        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { 
                                    searchQuery = "" 
                                    onQueryChange("")
                                }) {
                                    Icon(Icons.Rounded.Close, contentDescription = null)
                                }
                            } else if (uiState.isSearching) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp).padding(4.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            onSearch(searchQuery)
                            focusManager.clearFocus()
                        }),
                        shape = RoundedCornerShape(28.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    )

                    // Search Suggestions Dropdown
                    if (uiState.searchResults.isNotEmpty()) {
                        androidx.compose.ui.window.Popup(
                            alignment = Alignment.TopCenter,
                            onDismissRequest = { onQueryChange("") },
                            offset = androidx.compose.ui.unit.IntOffset(0, 160)
                        ) {
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(8.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column {
                                    uiState.searchResults.take(5).forEach { result ->
                                        DropdownMenuItem(
                                            text = {
                                                Column {
                                                    Text(result.name, fontWeight = FontWeight.Bold)
                                                    if (result.admin1 != null || result.country != null) {
                                                        Text(
                                                            text = listOfNotNull(result.admin1, result.country).joinToString(", "),
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.outline
                                                        )
                                                    }
                                                }
                                            },
                                            onClick = {
                                                searchQuery = result.name
                                                onSearch(result.name)
                                                focusManager.clearFocus()
                                            },
                                            leadingIcon = {
                                                Icon(Icons.Rounded.LocationCity, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                            }
                                        )
                                        if (result != uiState.searchResults.take(5).last()) {
                                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(innerPadding)
        ) {
            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize()
            ) {
                if (uiState.isLoading && !uiState.isRefreshing) {
                    ShimmerLoadingList()
                } else if (uiState.errorMessage != null) {
                    ErrorView(uiState.errorMessage) { onRetry(searchQuery) }
                } else {
                    WeatherList(uiState.forecasts)
                }
            }
        }
    }
}

@Composable
fun WeatherList(forecasts: List<DayForecast>) {
    if (forecasts.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Rounded.CloudQueue, null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
                Spacer(Modifier.height(8.dp))
                Text("No data available. Try searching for a city.", color = MaterialTheme.colorScheme.outline)
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(forecasts) { forecast ->
                WeatherDayCard(forecast)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun WeatherDayCard(forecast: DayForecast) {
    val date = LocalDate.parse(forecast.date)
    val formatter = DateTimeFormatter.ofPattern("EEEE, d MMM", Locale.getDefault())
    val formattedDate = date.format(formatter)
    val isToday = date == LocalDate.now()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isToday) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f) 
                            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isToday) 4.dp else 1.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isToday) "Today" else formattedDate,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isToday) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                    )
                    
                    val conditionText = when {
                        forecast.precipitationProbability > 60 -> "Stormy"
                        forecast.precipitationProbability > 20 -> "Cloudy / Rain"
                        else -> "Sunny"
                    }
                    Text(
                        text = conditionText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isToday) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f) 
                                else MaterialTheme.colorScheme.secondary
                    )
                }

                val (icon, color) = when {
                    forecast.precipitationProbability > 60 -> Icons.Rounded.Thunderstorm to Color(0xFF5C6BC0)
                    forecast.precipitationProbability > 20 -> Icons.Rounded.Cloud to Color(0xFF90A4AE)
                    else -> Icons.Rounded.WbSunny to Color(0xFFFFB300)
                }
                
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Thermostat,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "${forecast.tempMax.toInt()}°",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "/ ${forecast.tempMin.toInt()}°",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoChip(Icons.Rounded.Umbrella, "${forecast.precipitationProbability}%")
                    if (forecast.precipitationSum > 0.0) {
                        InfoChip(Icons.Rounded.WaterDrop, "${forecast.precipitationSum} mm")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            
            // Subtle progress for rain probability
            LinearProgressIndicator(
                progress = { forecast.precipitationProbability / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = if (forecast.precipitationProbability > 50) Color(0xFF7E57C2) else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun InfoChip(icon: ImageVector, text: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(4.dp))
            Text(text = text, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ShimmerLoadingList() {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        repeat(4) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .shimmerEffect()
            )
        }
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        MaterialTheme.colorScheme.surfaceVariant,
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )

    background(brush, shape = RoundedCornerShape(24.dp))
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = MaterialTheme.colorScheme.errorContainer,
            shape = CircleShape,
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.CloudOff, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(40.dp))
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Oops! Something went wrong",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            color = MaterialTheme.colorScheme.outline,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
        ) {
            Icon(Icons.Rounded.Refresh, null)
            Spacer(Modifier.width(8.dp))
            Text("Try Again")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    Week6_WeatherForecastTheme {
        val sampleCity = GeocodingResult(
            name = "Malang",
            latitude = -7.9839,
            longitude = 112.6214,
            country = "Indonesia",
            admin1 = "East Java"
        )
        val sampleForecasts = listOf(
            DayForecast(LocalDate.now().toString(), 30.0, 22.0, 0.0, 10),
            DayForecast(LocalDate.now().plusDays(1).toString(), 28.0, 21.0, 5.0, 70),
            DayForecast(LocalDate.now().plusDays(2).toString(), 29.0, 22.0, 2.0, 40)
        )
        val sampleUiState = WeatherUiState(
            isLoading = false,
            forecasts = sampleForecasts,
            selectedCity = sampleCity,
            errorMessage = null,
            isRefreshing = false,
            searchResults = listOf(
                GeocodingResult("Malang", -7.98, 112.62, "Indonesia", "East Java"),
                GeocodingResult("Malangbong", -7.06, 108.10, "Indonesia", "West Java")
            )
        )
        
        WeatherScreenContent(
            uiState = sampleUiState,
            onSearch = {},
            onQueryChange = {},
            onRefresh = {},
            onRetry = {}
        )
    }
}
