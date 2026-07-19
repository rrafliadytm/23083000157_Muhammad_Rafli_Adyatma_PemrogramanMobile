package com.example.profilmahasiswa.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.profilmahasiswa.R
import com.example.profilmahasiswa.ui.theme.ProfilMahasiswaTheme

// =============================================================
// PROFIL MAHASISWA SCREEN
// Demonstrasi: Column, Row, Box, Modifier, State, Card, Button
// =============================================================

/**
 * ProfileScreen - Halaman utama profil mahasiswa.
 *
 * Konsep yang dipelajari:
 * 1. Scaffold - Layout dasar dengan TopAppBar
 * 2. Column - Menyusun elemen secara vertikal
 * 3. Row - Menyusun elemen secara horizontal
 * 4. Box - Menumpuk elemen (overlapping)
 * 5. Modifier - Styling (padding, background, border, size)
 * 6. State - remember + mutableStateOf untuk data yang berubah
 * 7. Card - Material 3 card component
 * 8. Button - Interaksi pengguna
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    // ============================================
    // STATE MANAGEMENT
    // State adalah data yang bisa berubah dan memicu recomposition.
    // 'remember' menyimpan nilai agar tidak reset saat recomposition.
    // 'mutableStateOf' membuat state yang observable oleh Compose.
    // ============================================
    var editCount by remember { mutableStateOf(0) }
    var isEditing by remember { mutableStateOf(false) }

    // Data profil - menggunakan state agar bisa diubah
    var nama by remember { mutableStateOf("Muhammad Rafli Adyatma") }
    var nim by remember { mutableStateOf("23083000157") }
    var jurusan by remember { mutableStateOf("S-1 Sistem Informasi") }

    // Scaffold menyediakan struktur layout standar Material 3
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profil Mahasiswa",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        // ============================================
        // COLUMN - Layout Vertikal
        // Semua children disusun dari atas ke bawah.
        // Modifier.verticalScroll() membuat konten bisa di-scroll.
        // ============================================
        Column(
            modifier = Modifier
                .fillMaxSize()                              // Mengisi seluruh layar
                .padding(innerPadding)                      // Padding dari Scaffold
                .verticalScroll(rememberScrollState())      // Bisa di-scroll
                .padding(16.dp),                            // Padding konten
            horizontalAlignment = Alignment.CenterHorizontally  // Semua children di tengah
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // ============================================
            // BOX - Overlapping Layout
            // Digunakan untuk menumpuk foto profil dengan badge.
            // ============================================
            ProfilePhotoSection()

            Spacer(modifier = Modifier.height(16.dp))

            // ============================================
            // INFORMASI UTAMA
            // Menggunakan Column untuk menyusun teks vertikal
            // ============================================
            Text(
                text = nama,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "NIM: $nim",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ============================================
            // ROW - Layout Horizontal
            // Icon dan teks disusun berdampingan.
            // ============================================
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Jurusan",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = jurusan,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ============================================
            // CARD - Info Kontak
            // Card adalah komponen Material 3 dengan elevation/shadow.
            // Di dalamnya kita menggunakan Column + Row.
            // ============================================
            ContactInfoCard()

            Spacer(modifier = Modifier.height(16.dp))

            // ============================================
            // CARD - Statistik Akademik
            // Menggunakan Row untuk layout grid-style.
            // ============================================
            AcademicStatsCard()

            Spacer(modifier = Modifier.height(24.dp))

            // ============================================
            // BUTTON & STATE
            // Button mengubah state, Compose otomatis re-render.
            // ============================================
            Button(
                onClick = {
                    editCount++           // Mengubah state → trigger recomposition
                    isEditing = !isEditing
                },
                modifier = Modifier
                    .fillMaxWidth()                         // Tombol lebar penuh
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),         // Sudut membulat
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isEditing)
                        MaterialTheme.colorScheme.error     // Merah saat editing
                    else
                        MaterialTheme.colorScheme.primary   // Biru normal
                )
            ) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.Close else Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditing) "Batal Edit" else "Edit Profil",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Menampilkan jumlah klik - contoh penggunaan state
            Text(
                text = "Tombol diklik $editCount kali",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// =============================================================
// COMPOSABLE COMPONENTS (Reusable)
// Setiap section dibuat sebagai fungsi @Composable terpisah
// agar kode lebih rapi dan bisa di-reuse.
// =============================================================

/**
 * ProfilePhotoSection - Foto profil dengan Box (overlapping).
 *
 * Box digunakan untuk menumpuk:
 * 1. Foto profil (background)
 * 2. Badge status (di atas foto, pojok kanan bawah)
 */
@Composable
fun ProfilePhotoSection() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        // Foto profil dengan Modifier berantai
        Box(
            modifier = Modifier
                .size(120.dp)                              // Ukuran 120x120 dp
                .clip(CircleShape)                         // Crop menjadi lingkaran
                .background(                               // Background gradient
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                )
                .border(                                   // Border lingkaran
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder icon sebagai pengganti foto
            // Di project nyata, gunakan: Image(painter = painterResource(R.drawable.foto))
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto Profil",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        // Badge status - ditumpuk di pojok kanan bawah
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)                // Posisi di kanan bawah
                .size(28.dp)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50))             // Hijau = aktif
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Aktif",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * ContactInfoCard - Card berisi informasi kontak.
 *
 * Demonstrasi:
 * - Card component dengan elevation
 * - Column di dalam Card
 * - Row untuk setiap baris info (icon + teks)
 * - Modifier: fillMaxWidth, padding, border
 */
@Composable
fun ContactInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),                               // Lebar penuh
        shape = RoundedCornerShape(16.dp),                 // Sudut membulat 16dp
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)             // Padding di dalam Card
        ) {
            // Header
            Text(
                text = "📞 Informasi Kontak",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Setiap baris kontak menggunakan Row
            ContactRow(
                icon = Icons.Default.Email,
                label = "Email",
                value = "ahmad.fauzi@student.ac.id"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ContactRow(
                icon = Icons.Default.Phone,
                label = "Telepon",
                value = "+62 812-3456-7890"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ContactRow(
                icon = Icons.Default.LocationOn,
                label = "Alamat",
                value = "Malang, Jawa Timur"
            )
        }
    }
}

/**
 * ContactRow - Satu baris informasi kontak.
 *
 * Menggunakan Row untuk menyusun icon, label, dan value secara horizontal.
 * Ini adalah contoh Composable yang reusable dengan parameter.
 *
 * @param icon Icon yang ditampilkan di kiri
 * @param label Judul field (contoh: "Email")
 * @param value Isi field (contoh: "ahmad@mail.com")
 */
@Composable
fun ContactRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon di dalam lingkaran berwarna
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Label dan value di dalam Column (vertikal)
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * AcademicStatsCard - Card berisi statistik akademik.
 *
 * Demonstrasi:
 * - Row dengan Modifier.weight() untuk distribusi ruang yang rata
 * - Column di dalam Row
 * - Box untuk elemen dekoratif
 */
@Composable
fun AcademicStatsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "📊 Statistik Akademik",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Row dengan 3 kolom menggunakan weight()
            // weight() membagi ruang secara proporsional
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatItem(
                    modifier = Modifier.weight(1f),    // 1/3 dari lebar
                    value = "3.75",
                    label = "IPK",
                    icon = Icons.Default.EmojiEvents,
                    color = MaterialTheme.colorScheme.primary
                )
                StatItem(
                    modifier = Modifier.weight(1f),    // 1/3 dari lebar
                    value = "120",
                    label = "SKS",
                    icon = Icons.Default.MenuBook,
                    color = MaterialTheme.colorScheme.tertiary
                )
                StatItem(
                    modifier = Modifier.weight(1f),    // 1/3 dari lebar
                    value = "6",
                    label = "Semester",
                    icon = Icons.Default.CalendarMonth,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

/**
 * StatItem - Satu item statistik (angka + label).
 *
 * @param modifier Modifier dari parent (termasuk weight)
 * @param value Angka yang ditampilkan besar
 * @param label Keterangan di bawah angka
 * @param icon Icon yang ditampilkan di atas angka
 * @param color Warna aksen
 */
@Composable
fun StatItem(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    icon: ImageVector,
    color: Color
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// =============================================================
// PREVIEW
// @Preview memungkinkan kita melihat tampilan UI di Android Studio
// tanpa harus menjalankan aplikasi di emulator/device.
// =============================================================

@Preview(
    showBackground = true,
    showSystemUi = true,            // Menampilkan system UI (status bar, etc.)
    name = "Profil Mahasiswa - Light Mode"
)
@Composable
fun ProfileScreenPreview() {
    ProfilMahasiswaTheme(darkTheme = false) {
        ProfileScreen()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Profil Mahasiswa - Dark Mode"
)
@Composable
fun ProfileScreenDarkPreview() {
    ProfilMahasiswaTheme(darkTheme = true) {
        ProfileScreen()
    }
}

// Preview untuk komponen individual
@Preview(showBackground = true, name = "Contact Card")
@Composable
fun ContactInfoCardPreview() {
    ProfilMahasiswaTheme {
        ContactInfoCard()
    }
}

@Preview(showBackground = true, name = "Academic Stats Card")
@Composable
fun AcademicStatsCardPreview() {
    ProfilMahasiswaTheme {
        AcademicStatsCard()
    }
}
