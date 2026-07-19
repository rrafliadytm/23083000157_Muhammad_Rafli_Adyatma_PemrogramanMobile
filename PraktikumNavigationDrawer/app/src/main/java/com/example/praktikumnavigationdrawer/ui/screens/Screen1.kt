package com.example.praktikumnavigationdrawer.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.praktikumnavigationdrawer.ui.navigation.Screen
import com.example.praktikumnavigationdrawer.ui.theme.PraktikumNavigationDrawerTheme

@Composable
fun Screen1(navController: NavHostController) {
    ScreenContent(
        title = Screen.Screen1.title,
        onBackClick = { navController.popBackStack() }
    )
}

@Preview(showBackground = true)
@Composable
fun Screen1Preview() {
    PraktikumNavigationDrawerTheme {
        Screen1(navController = rememberNavController())
    }
}
