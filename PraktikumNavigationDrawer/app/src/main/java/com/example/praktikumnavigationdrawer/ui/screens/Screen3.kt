package com.example.praktikumnavigationdrawer.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.praktikumnavigationdrawer.ui.navigation.Screen

@Composable
fun Screen3(navController: NavHostController) {
    ScreenContent(
        title = Screen.Screen3.title,
        onBackClick = { navController.popBackStack() }
    )
}
