package com.example.praktikumnavigationdrawer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praktikumnavigationdrawer.ui.navigation.Screen
import com.example.praktikumnavigationdrawer.ui.theme.*

@Composable
fun DrawerContent(
    selectedRoute: String,
    onItemClick: (Screen) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
        modifier = Modifier.width(300.dp)
    ) {
        // Modern Header with Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(PrimaryDark, Primary)
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Android Developer",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "developer@example.com",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Items
        val items = listOf(
            ScreenItem(Screen.Home, Icons.Default.Home),
            ScreenItem(Screen.Screen1, Icons.Default.Favorite),
            ScreenItem(Screen.Screen2, Icons.Default.Notifications),
            ScreenItem(Screen.Screen3, Icons.Default.Settings)
        )

        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            items.forEach { item ->
                val isSelected = selectedRoute == item.screen.route
                
                NavigationDrawerItem(
                    label = { 
                        Text(
                            text = item.screen.title,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        ) 
                    },
                    selected = isSelected,
                    onClick = { onItemClick(item.screen) },
                    icon = { 
                        Icon(
                            imageVector = item.icon, 
                            contentDescription = null,
                            tint = if (isSelected) Primary else Color.Gray
                        ) 
                    },
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = DrawerItemSelected,
                        selectedTextColor = DrawerItemSelectedText,
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Footer Item
        Text(
            text = "Version 1.0.0",
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.CenterHorizontally),
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

private data class ScreenItem(val screen: Screen, val icon: ImageVector)
