# Portal Mahasiswa (Week 3 - Pemrograman Mobile) 🎓

🚀 **Aplikasi Android Manajemen Daftar Mahasiswa** yang dirancang dengan antarmuka modern, interaktif, dan responsif menggunakan **Kotlin** dan **Jetpack Compose (Material 3)**.

---

<!-- Badges -->
<p align="left">
  <img src="https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin Badge" />
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android Badge" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose Badge" />
  <img src="https://img.shields.io/badge/Material%203-7F39FB?style=for-the-badge&logo=materialdesign&logoColor=white" alt="Material 3 Badge" />
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle Badge" />
</p>

---

## 📖 Ringkasan Aplikasi

Aplikasi ini dikembangkan untuk mengelola data mahasiswa secara dinamis pada lingkungan lokal (in-memory state). Dibuat khusus untuk memenuhi tugas praktikum mata kuliah **Pemrograman Mobile** (Semester 6), proyek ini mengedepankan kesederhanaan struktur kode tanpa mengorbankan estetika antarmuka pengguna (*User Interface*).

---

## 🛠️ Fitur Aplikasi Secara Detail

Berikut adalah rincian fungsionalitas dan fitur teknis dari setiap komponen utama dalam aplikasi:

### 1. 📱 Dashboard Utama (Home Screen)
Halaman ini adalah pintu masuk utama aplikasi yang menyajikan seluruh daftar data mahasiswa.
* **Header Modern (`CenterAlignedTopAppBar`):** 
  * 🏷️ Judul `"Portal Mahasiswa"` ditulis menggunakan font *Extra Bold*.
  * 🎨 Warna latar belakang menggunakan `primaryContainer` dari skema warna tema dinamis.
* **Daftar Dinamis (`LazyColumn`):** 
  * ⚡ Menggunakan daftar gulir yang efisien untuk me-render data mahasiswa.
  * 📏 Jarak antarkartu diatur rapi sebesar `16.dp`.
* **Kartu Data Mahasiswa (`MahasiswaItemCard`):** 
  * 🔤 **Avatar Inisial Otomatis:** Mengambil huruf pertama dari nama mahasiswa (`mhs.nama.take(1).uppercase()`) dan ditampilkan di dalam lingkaran berwarna `secondaryContainer`.
  * 📝 **Informasi Ringkas:** Menampilkan Nama Lengkap (teks tebal) dan NIM (teks sekunder).
  * ➡️ **Indikator Navigasi:** Ikon panah kanan (`KeyboardArrowRight`) di sisi kanan kartu.
* **Tombol Tambah Mahasiswa (`ExtendedFloatingActionButton`):** 
  * ➕ Tombol melayang lebar dengan teks `"Mahasiswa Baru"` dan ikon tambah (`Add`).
  * 🛸 Memiliki sudut membulat elegan `16.dp`.

### 2. 🔍 Detail Profil Mahasiswa (Detail Screen)
Menampilkan informasi lengkap mengenai profil mahasiswa yang dipilih dari halaman utama.
* **🔗 Kirim Argumen via Rute (Navigation Arguments):** Data dikirim secara aman antar layar melalui rute navigasi: `detail/{nim}/{nama}/{email}/{jurusan}`.
* **👤 Header Profil:**
  * 🎨 Avatar besar (`120.dp`) dengan inisial nama berukuran besar (`displayLarge`) dan efek bayangan (`shadowElevation = 8.dp`).
  * 📛 Nama lengkap mahasiswa ditulis menggunakan tipografi berukuran besar dan tebal (`headlineMedium` & `ExtraBold`).
  * 🎓 Jurusan/Program studi disorot di bawah nama dengan warna utama tema (`primary`).
* **🗂️ Kartu Informasi Terstruktur:**
  * 🎴 Kontainer kartu (`Card`) dengan sudut membulat lebar (`28.dp`) dan transparansi latar belakang (`surfaceVariant.copy(alpha = 0.4f)`).
  * 📋 Informasi disajikan dalam baris (`DetailRowItem`) yang masing-masing dilengkapi ikon representatif:
    * **NIM:** Dilengkapi dengan ikon informasi (`Info` ℹ️).
    * **Email:** Dilengkapi dengan ikon surat (`Email` ✉️).
    * **Jurusan:** Dilengkapi dengan ikon penunjuk tempat (`Place` 📍).
* **⬅️ Navigasi Kembali:** Dilengkapi tombol kembali (`ArrowBack`) pada bilah judul (`TopAppBar`).

### 3. ✍️ Formulir Pendaftaran Mahasiswa Baru (Tambah Screen)
Halaman formulir interaktif untuk memasukkan data mahasiswa baru ke dalam daftar.
* **💾 State Input yang Aman (`rememberSaveable`):** Kolom input menggunakan `rememberSaveable` agar teks yang sedang diketik tidak hilang ketika perangkat mengalami rotasi layar.
* **✏️ Komponen Input Terstandarisasi (`StyledTextField`):**
  * 📦 Memanfaatkan `OutlinedTextField` dengan sudut membulat `16.dp`.
  * 🎨 Warna garis tepi fokus (`focusedBorderColor`) yang dinamis.
  * 🔏 Membatasi input menjadi satu baris saja (`singleLine = true`).
  * ⌨️ Kolom input terdiri dari:
    * **NIM** (Ikon `Info` ℹ️)
    * **Nama Lengkap** (Ikon `Person` 👤)
    * **Email Kampus** (Ikon `Email` ✉️)
    * **Program Studi** (Ikon `Place` 📍)
* **✅ Validasi Tombol Simpan Otomatis:**
  * 🚫 Tombol **"Simpan Sekarang"** memiliki logika validasi bawaan (`enabled = nim.isNotBlank() && nama.isNotBlank()`).
  * 🟢 Tombol hanya aktif jika kolom **NIM** dan **Nama Lengkap** telah terisi karakter. Jika kosong, tombol akan dinonaktifkan secara otomatis.
  * 💾 Menyimpan data akan menggabungkan objek baru ke dalam daftar (`listMahasiswa = listMahasiswa + baru`) lalu memanggil `navController.popBackStack()`.

---

## 🗺️ Alur Navigasi & Arsitektur Kode

Navigasi dalam aplikasi ini diatur menggunakan pustaka **Jetpack Navigation Compose** dengan struktur diagram navigasi berikut:

```mermaid
graph TD
    A[HomeScreen / Route: 'home'] -->|Klik Tombol Tambah| B[TambahMahasiswaScreen / Route: 'tambah']
    A -->|Klik Kartu Mahasiswa| C[DetailScreen / Route: 'detail/{nim}/{nama}/{email}/{jurusan}']
    B -->|Simpan & PopBackStack| A
    C -->|Klik Tombol Back| A
```

### Konfigurasi Rute Navigasi di `MainActivity.kt`
```kotlin
NavHost(navController = navController, startDestination = "home") {
    composable("home") {
        HomeScreen(...)
    }
    composable(
        route = "detail/{nim}/{nama}/{email}/{jurusan}",
        arguments = listOf(
            navArgument("nim") { type = NavType.StringType },
            ...
        )
    ) { backStackEntry ->
        // Mengurai argumen navigasi dan menampilkan DetailScreen
        DetailScreen(...)
    }
    composable("tambah") {
        TambahMahasiswaScreen(...)
    }
}
```

---

## 🛠️ Tech Stack & Spesifikasi Proyek

Berikut rincian spesifikasi build Gradle dan pustaka dependencies yang terkonfigurasi pada file `app/build.gradle.kts`:

### SDK & Compiler Settings
* **Compile SDK Version:** `36` (Minor API Level: `1`)
* **Minimum SDK Version:** `26` (Android 8.0 Oreo)
* **Target SDK Version:** `36`
* **Java Compatibility:** Java 11 (`JavaVersion.VERSION_11`)
* **Kotlin Compiler Extension:** Menggunakan Kotlin Compose Compiler Plugin terbaru (`alias(libs.plugins.kotlin.compose)`)

### Pustaka Dependensi (Dependencies)
* **Jetpack Compose UI Stack:** `androidx.compose.ui`, `androidx.compose.ui.graphics`, `androidx.compose.ui.tooling.preview`
* **Material Design 3:** `androidx.compose.material3` (Komponen Material modern)
* **Icons:** `androidx.compose.material.icons.core` dan auto-mirrored icons.
* **Navigation:** `androidx.navigation.compose` (Untuk pengaturan rute perpindahan layar)
* **Activity Compose:** `androidx.activity.compose` (Integrasi Compose dengan Android Lifecycle & Activity)

---

## 📂 Struktur Berkas Proyek

Berikut adalah detail berkas source code utama proyek:

* **[MainActivity.kt](file:///E:/Lainnya/Tugas/Semester%206/Pemrograman%20Mobile/Project/Week3_DaftarMahasiswa/app/src/main/java/com/example/week3_daftarmahasiswa/MainActivity.kt)**
  * Berisi deklarasi model data `data class Mahasiswa`.
  * Berisi class `MainActivity` sebagai titik masuk (*entry point*) aplikasi.
  * Berisi Composable function utama `MahasiswaApp()` yang mengelola state data daftar mahasiswa dan setup navigasi.
  * Berisi implementasi tampilan layar: `HomeScreen`, `DetailScreen`, dan `TambahMahasiswaScreen` beserta komponen-komponen pembantunya.
* **UI Theme Directory (`com.example.week3_daftarmahasiswa.ui.theme`):**
  * **[Theme.kt](file:///E:/Lainnya/Tugas/Semester%206/Pemrograman%20Mobile/Project/Week3_DaftarMahasiswa/app/src/main/java/com/example/week3_daftarmahasiswa/ui/theme/Theme.kt):** Mengatur penyesuaian warna sistem, deteksi mode gelap (*Dark Mode*), dan inisialisasi framework Material 3 Theme.
  * **[Color.kt](file:///E:/Lainnya/Tugas/Semester%206/Pemrograman%20Mobile/Project/Week3_DaftarMahasiswa/app/src/main/java/com/example/week3_daftarmahasiswa/ui/theme/Color.kt):** Palet warna kustom untuk tema terang dan gelap.
  * **[Type.kt](file:///E:/Lainnya/Tugas/Semester%206/Pemrograman%20Mobile/Project/Week3_DaftarMahasiswa/app/src/main/java/com/example/week3_daftarmahasiswa/ui/theme/Type.kt):** Pengaturan tipografi teks (font styles) bawaan aplikasi.

---

## 💻 Cara Instalasi dan Menjalankan Aplikasi

1. **Persiapan Perangkat Lunak:**
   * Unduh dan instal [Android Studio](https://developer.android.com/studio).
   * Pastikan SDK Platform Android 14/15 (API 34/35/36) sudah terpasang lewat SDK Manager Android Studio.
2. **Klon atau Buka Proyek:**
   * Buka Android Studio, pilih **File > Open**, lalu pilih folder `Week3_DaftarMahasiswa`.
3. **Sinkronisasi Gradle:**
   * Android Studio akan otomatis mengunduh dependencies yang diperlukan melalui Gradle. Pastikan koneksi internet aktif.
4. **Jalankan Aplikasi:**
   * Buka **Device Manager** untuk membuat emulator Android Virtual Device (AVD), atau hubungkan HP Android asli Anda dengan mengaktifkan mode *USB Debugging*.
   * Tekan tombol **Run** (segitiga hijau) di pojok kanan atas Android Studio.

---

## 📋 Data Uji Coba Bawaan (Default Mock Data)

Secara default, saat pertama kali dibuka, aplikasi sudah dilengkapi data simulasi berikut:

| NIM | Nama Lengkap | Email Kampus | Program Studi |
| :--- | :--- | :--- | :--- |
| `23083000` | Budi Santoso | 23083000@gmail.com | Informatika |
| `23083001` | Siti Aminah | 23083001@gmail.com | Sistem Informasi |
| `23083002` | Andi Wijaya | 23083002@mail.com | Teknik Elektro |
