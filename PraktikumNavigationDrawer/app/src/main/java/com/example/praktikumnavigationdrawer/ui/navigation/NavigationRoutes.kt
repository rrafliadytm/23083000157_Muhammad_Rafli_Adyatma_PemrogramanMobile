package com.example.praktikumnavigationdrawer.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Menu Utama")
    object Screen1 : Screen("screen_1", "Screen 1")
    object Screen2 : Screen("screen_2", "Screen 2")
    object Screen3 : Screen("screen_3", "Screen 3")
}
