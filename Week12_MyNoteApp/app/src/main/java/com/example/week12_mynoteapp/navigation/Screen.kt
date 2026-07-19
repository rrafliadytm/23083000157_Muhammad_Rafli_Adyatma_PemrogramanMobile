package com.example.week12_mynoteapp.navigation

/**
 * Sealed class untuk mendefinisikan rute navigasi dalam aplikasi.
 */
sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Editor : Screen("editor?noteId={noteId}") {
        fun createRoute(noteId: Long? = null): String {
            return if (noteId != null) "editor?noteId=$noteId" else "editor"
        }
    }
}
