package com.example.week12_mynoteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.week12_mynoteapp.navigation.MyNoteNavGraph
import com.example.week12_mynoteapp.ui.theme.MyNoteTheme

/**
 * Titik masuk utama aplikasi (Entry Point).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Mengaktifkan tampilan edge-to-edge untuk UI yang modern
        enableEdgeToEdge()
        
        setContent {
            // Membungkus aplikasi dengan tema kustom MyNote
            MyNoteTheme {
                // Menjalankan NavGraph utama
                MyNoteNavGraph()
            }
        }
    }
}
