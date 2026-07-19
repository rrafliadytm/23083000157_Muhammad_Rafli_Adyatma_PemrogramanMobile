package com.example.week12_mynoteapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.week12_mynoteapp.ui.screens.DashboardScreen
import com.example.week12_mynoteapp.ui.screens.EditorScreen
import com.example.week12_mynoteapp.viewmodel.NoteViewModel

/**
 * NavGraph untuk mengatur alur navigasi antar layar dalam aplikasi.
 */
@Composable
fun MyNoteNavGraph(
    navController: NavHostController = rememberNavController(),
    // Menggunakan satu instance ViewModel yang dibagikan antar layar
    noteViewModel: NoteViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        // Rute Dashboard
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = noteViewModel,
                onAddNote = {
                    navController.navigate(Screen.Editor.createRoute())
                },
                onEditNote = { noteId ->
                    navController.navigate(Screen.Editor.createRoute(noteId))
                }
            )
        }

        // Rute Editor (dengan optional argument noteId)
        composable(
            route = Screen.Editor.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.StringType // Navigasi compose mengirim argumen sebagai string secara default
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val noteIdString = backStackEntry.arguments?.getString("noteId")
            val noteId = noteIdString?.toLongOrNull()

            EditorScreen(
                viewModel = noteViewModel,
                noteId = noteId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
