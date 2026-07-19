package com.example.profilmahasiswa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.profilmahasiswa.screens.DataNilai
import com.example.profilmahasiswa.screens.ProfileEditScreen
import com.example.profilmahasiswa.screens.ProfileScreen
import com.example.profilmahasiswa.ui.theme.ProfilMahasiswaTheme

/**
 * MainActivity - Entry point aplikasi.
 *
 * Di Jetpack Compose, kita tidak lagi menggunakan XML layout.
 * Semua UI didefinisikan sebagai fungsi @Composable di dalam setContent { }.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Menggunakan full screen edge-to-edge

        setContent {
            // Theme membungkus seluruh UI agar konsisten
            ProfilMahasiswaTheme {
                // Memanggil screen utama
                ProfileEditScreen()
                //ProfileScreen()
                //ProfileScreen()
                //ProfileScreen()
                //ProfileScreen()
                //ProfileScreen()
                //ProfileScreen()
                //DataNilai()

            }
        }
    }
}
