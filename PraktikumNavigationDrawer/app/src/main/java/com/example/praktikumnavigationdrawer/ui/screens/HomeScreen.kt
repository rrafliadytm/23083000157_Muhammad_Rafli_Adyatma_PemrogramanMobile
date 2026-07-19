package com.example.praktikumnavigationdrawer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.praktikumnavigationdrawer.ui.components.DrawerContent
import com.example.praktikumnavigationdrawer.ui.navigation.Screen
import com.example.praktikumnavigationdrawer.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                selectedRoute = Screen.Home.route,
                onItemClick = { screen ->
                    scope.launch { drawerState.close() }
                    if (screen.route != Screen.Home.route) {
                        navController.navigate(screen.route)
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                Surface(shadowElevation = 4.dp) {
                    TopAppBar(
                        title = { 
                            Text(
                                text = "Dashboard", 
                                color = TopBarContent,
                                fontWeight = FontWeight.Bold
                            ) 
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu, 
                                    contentDescription = "Menu", 
                                    tint = TopBarContent
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        ),
                        modifier = Modifier.background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(PrimaryDark, Primary)
                            )
                        )
                    )
                }
            },
            containerColor = Color(0xFFF8F9FA)
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // Welcome Section
                item {
                    WelcomeBanner()
                }

                // Quick Actions Title
                item {
                    Text(
                        text = "Quick Actions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }

                // Grid of Actions (Simulated with Items)
                val actions = listOf(
                    ActionData("Profile", Icons.Default.Person, Primary),
                    ActionData("Favorites", Icons.Default.Favorite, Color(0xFFE91E63)),
                    ActionData("Stats", Icons.Default.BarChart, Color(0xFF4CAF50)),
                    ActionData("Settings", Icons.Default.Settings, Color(0xFFFF9800))
                )

                items(actions.chunked(2)) { rowActions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowActions.forEach { action ->
                            ActionCard(
                                action = action,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Recent Activity Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Recent Activity",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }

                items(5) { index ->
                    ActivityItem(index = index)
                }
            }
        }
    }
}

@Composable
fun WelcomeBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hello, Muhammad Rafli Adyatma",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Have a great day today.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WavingHand,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ActionCard(action: ActionData, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(action.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = null,
                    tint = action.color
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = action.title,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun ActivityItem(index: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Activity Notification #${index + 1}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    text = "2 hours ago",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

data class ActionData(val title: String, val icon: ImageVector, val color: Color)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PraktikumNavigationDrawerTheme {
        HomeScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeBannerPreview() {
    PraktikumNavigationDrawerTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            WelcomeBanner()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActionCardPreview() {
    PraktikumNavigationDrawerTheme {
        ActionCard(
            action = ActionData("Profile", Icons.Default.Person, Primary),
            modifier = Modifier
                .padding(16.dp)
                .width(160.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityItemPreview() {
    PraktikumNavigationDrawerTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ActivityItem(index = 0)
        }
    }
}
