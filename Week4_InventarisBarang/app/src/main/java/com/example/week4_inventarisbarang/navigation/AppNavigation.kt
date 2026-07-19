package com.example.week4_inventarisbarang.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.week4_inventarisbarang.data.BarangRepository
import com.example.week4_inventarisbarang.screen.DaftarBarangScreen
import com.example.week4_inventarisbarang.screen.DetailBarangScreen
import com.example.week4_inventarisbarang.screen.TambahBarangScreen

@Composable
fun AppNavigation(navController: NavHostController, repository: BarangRepository) {
    NavHost(
        navController = navController,
        startDestination = "daftar"
    ) {
        composable("daftar") {
            DaftarBarangScreen(navController = navController, repository = repository)
        }

        composable(
            route = "detail/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            DetailBarangScreen(navController = navController, repository = repository, barangId = id)
        }

        composable("tambah") {
            TambahBarangScreen(navController = navController, repository = repository)
        }

        composable(
            route = "edit/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            TambahBarangScreen(navController = navController, repository = repository, editBarangId = id)
        }
    }
}
