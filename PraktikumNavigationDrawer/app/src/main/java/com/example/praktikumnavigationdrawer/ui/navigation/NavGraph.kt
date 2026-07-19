package com.example.praktikumnavigationdrawer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.praktikumnavigationdrawer.ui.screens.HomeScreen
import com.example.praktikumnavigationdrawer.ui.screens.Screen1
import com.example.praktikumnavigationdrawer.ui.screens.Screen2
import com.example.praktikumnavigationdrawer.ui.screens.Screen3

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Screen1.route) {
            Screen1(navController = navController)
        }
        composable(route = Screen.Screen2.route) {
            Screen2(navController = navController)
        }
        composable(route = Screen.Screen3.route) {
            Screen3(navController = navController)
        }
    }
}
