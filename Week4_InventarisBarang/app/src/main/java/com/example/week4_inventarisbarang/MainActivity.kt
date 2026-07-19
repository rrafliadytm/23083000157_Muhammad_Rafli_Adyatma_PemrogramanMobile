package com.example.week4_inventarisbarang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.week4_inventarisbarang.data.BarangRepository
import com.example.week4_inventarisbarang.navigation.AppNavigation
import com.example.week4_inventarisbarang.ui.theme.Week4_InventarisBarangTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val repository = BarangRepository(this)
        
        setContent {
            Week4_InventarisBarangTheme {
                val navController = rememberNavController()
                AppNavigation(navController, repository)
            }
        }
    }
}
