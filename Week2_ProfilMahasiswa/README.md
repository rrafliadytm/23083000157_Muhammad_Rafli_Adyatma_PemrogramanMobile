# 📱 Aplikasi Profil & Transkrip Nilai Mahasiswa (Jetpack Compose)

Repositori ini dikembangkan sebagai tugas praktikum **Week 2 - Pemrograman Mobile (Semester 6)**. Proyek ini mendemonstrasikan implementasi antarmuka deklaratif modern Android dengan menggunakan **Jetpack Compose** dan **Material Design 3 (M3)**. 

Aplikasi ini memiliki fitur lengkap untuk menampilkan profil personal, melakukan simulasi edit profil secara dinamis berbasis state, serta menampilkan rekapitulasi data nilai akademik mahasiswa dengan pewarnaan dinamis untuk setiap kategori huruf nilai dan ikon visual kustom yang representatif.

---

## 👤 Identitas Mahasiswa
* **Nama Lengkap**: Muhammad Rafli Adyatma
* **NIM**: 23083000157
* **Jurusan**: S-1 Sistem Informasi
* **Semester**: 6

---

## 📁 Struktur Berkas Proyek

Berikut adalah struktur folder dan komponen penting di dalam proyek ini:

```
Week2_ProfilMahasiswa/
├── app/
│   ├── build.gradle.kts                ← Dependensi aplikasi & modul compiler compose
│   └── src/main/
│       ├── AndroidManifest.xml          ← Konfigurasi manifest Android & entry point
│       ├── java/com/example/profilmahasiswa/
│       │   ├── MainActivity.kt          ← Kelas Java/Kotlin utama yang menjalankan UI
│       │   ├── screens/
│       │   │   ├── ProfileScreen.kt     ← UI Utama Profil Mahasiswa
│       │   │   ├── ProfileEdit.kt       ← UI Formulir Pengeditan Profil
│       │   │   └── DataNilai.kt         ← UI Ringkasan Nilai Akademik
│       │   └── ui/theme/
│       │       └── Theme.kt             ← Konfigurasi warna, font, & engine tema Light/Dark
│       └── res/
│           └── values/
│               └── colors.xml           ← Definisi palet warna kustom (Hex Code)
```

---

## 🌟 Detail Fitur & Visualisasi Ikon Baru

Aplikasi ini terbagi menjadi **3 halaman utama (screens)** dengan fungsionalitas dan detail sebagai berikut:

### 1. Halaman Profil Utama (`ProfileScreen.kt`)
Halaman ini menyajikan representasi ringkas profil mahasiswa dengan tata letak yang bersih dan terstruktur.

* **Profile Photo Section (`ProfilePhotoSection`)**:
  * Menampilkan foto profil dalam bingkai lingkaran (`CircleShape`), berukuran `120.dp`, dikelilingi border berwarna primer sebesar `3.dp`, dengan latar belakang gradasi warna linear.
  * Terdapat badge status aktif berwarna hijau (`#4CAF50`) dengan ikon centang putih (`Icons.Default.Check`) di pojok kanan bawah foto.
* **Informasi Utama**:
  * Menampilkan nama mahasiswa secara tebal (`FontWeight.Bold`, `24.sp`).
  * Menampilkan NIM (`NIM: 23083000157`) dengan warna teks sekunder.
  * Menampilkan jurusan dengan ikon sekolah (`Icons.Default.School`).
* **Contact Info Card (`ContactInfoCard`)**:
  * Menggunakan `Card` Material 3 dengan warna kontras `surfaceVariant`.
  * Merender baris informasi kontak secara konsisten dengan ikon-ikon representatif:
    * **Email**: `ahmad.fauzi@student.ac.id` dengan ikon ✉️ `Icons.Default.Email`.
    * **Telepon**: `+62 812-3456-7890` dengan ikon 📞 `Icons.Default.Phone`.
    * **Alamat**: `Malang, Jawa Timur` dengan ikon 📍 `Icons.Default.LocationOn`.
* **Academic Stats Card (`AcademicStatsCard`)** *(Dengan Penambahan Ikon)*:
  * Menyusun tiga statistik penting berdampingan secara proporsional dalam satu baris (`Row`) menggunakan rasio bobot yang sama (`Modifier.weight(1f)`) dan menyematkan ikon kustom di atas angka nilai:
    * 🏆 **IPK**: `3.75` (Warna Biru Primer) - Ikon `Icons.Default.EmojiEvents`.
    * 📖 **SKS**: `120` (Warna Ungu Tersier) - Ikon `Icons.Default.MenuBook`.
    * 📅 **Semester**: `6` (Warna Teal Sekunder) - Ikon `Icons.Default.CalendarMonth`.

---

### 2. Halaman Edit Profil (`ProfileEdit.kt`)
Halaman ini mensimulasikan proses manipulasi dan pembaharuan data pribadi mahasiswa secara dinamis.

* **Header TopAppBar Kustom**:
  * Judul: `"Edit Profil Mahasiswa"` dengan warna latar belakang biru gelap `#1F2A44` (`colorPrimary`) dan warna teks krem cerah `#F7F3E8` (`colorPrimaryonContainer`).
* **Foto Profil Skala Besar (`ProfilePhotoSectionStandalone`)** *(Dengan Penambahan Ikon)*:
  * Foto profil berukuran lebih besar (`180.dp`) dengan ketebalan border lingkaran `5.dp`.
  * **Lencana Status Dinamis**: Saat mode baca, menampilkan badge status aktif centang (`Icons.Default.Check`) berwarna hijau. Saat mode edit aktif, badge secara otomatis berubah menjadi biru dan menampilkan ikon kamera 📷 (`Icons.Default.CameraAlt`) untuk memberi isyarat visual bahwa foto dapat diubah.
* **Formulir Edit Interaktif**:
  * Ketika `isEditing` bernilai `false`, field input berada dalam keadaan terkunci (disabled), garis tepi dihilangkan, dan hanya menampilkan data teks mentah.
  * Ketika `isEditing` diaktifkan (`true`), field input berubah menjadi `OutlinedTextField` yang aktif sehingga pengguna dapat mengetik pembaharuan data.
* **Dynamic Action Button**:
  * Tombol aksi di bagian bawah yang beradaptasi dengan mode edit:
    * **Mode Baca (View)**: Teks **"Edit"**, ikon `Icons.Default.Edit`, warna biru.
    * **Mode Ubah (Edit)**: Teks **"Simpan"**, ikon `Icons.Default.Save`, warna hijau sukses (`colorSubmit` / `#4CAF50`).

---

### 3. Halaman Rekapitulasi Data Nilai (`DataNilai.kt`)
Halaman ini menampilkan laporan evaluasi nilai akademik mahasiswa untuk seluruh mata kuliah yang ditempuh pada Semester 6. Fitur ini dirancang menggunakan arsitektur data-driven (berbasis list objek) agar kode lebih rapi dan modular.

* **Header Identitas Mahasiswa**:
  * Menggunakan `Card` khusus berwarna biru laut (`seaBlue` / `#BACBFF`) yang menampilkan NIM (`23083000157`), Nama (`Muhammad Rafli A`), dan Semester (`6`) dalam tata letak horizontal proporsional.
* **Tabel Transkrip Nilai Akademik** *(Dengan Penambahan Ikon)*:
  * Setiap baris mata kuliah sekarang menampilkan kontainer ikon berlatar belakang warna primer halus (`primaryContainer`) dengan ikon representatif:

| Kode | Mata Kuliah | Nilai Angka | Huruf Mutu | Visualisasi Ikon Kustom | Visualisasi Latar Belakang Huruf |
| :--- | :--- | :---: | :---: | :--- | :--- |
| **TIF401** | Pemrograman Mobile | 85 | **A** | 📱 `Icons.Default.Smartphone` | Hijau Pekat (`#4CB050`) |
| **TIF402** | Basis Data Lanjut | 78 | **B+** | 💾 `Icons.Default.Storage` | Kuning (`#CCDF2B`) |
| **TIF403** | Jaringan Komputer | 92 | **A** | 📶 `Icons.Default.Wifi` | Hijau Pekat (`#4CB050`) |
| **TIF404** | Kecerdasan Buatan | 88 | **A** | 🧠 `Icons.Default.Memory` | Hijau Pekat (`#4CB050`) |
| **TIF405** | Sistem Operasi | 74 | **B** | ⚙️ `Icons.Default.Settings` | Jingga (`#FCC00F`) |
| **TIF406** | Statistika | 81 | **A-** | 📊 `Icons.Default.Analytics` | Hijau Rumput (`#8BC14A`) |

* **Kartu IP Sementara** *(Dengan Penambahan Ikon)*:
  * Menyajikan nilai indeks prestasi semester berjalan secara mencolok: **IP Sementara: 3.67** di dalam kotak abu-abu dengan sematan ikon statistik naik 📈 (`Icons.Default.TrendingUp`) berwarna putih di sebelahnya.

---

## 🛠️ Konsep & Teknologi Android yang Digunakan

1. **Jetpack Compose Layouting**:
   * `Column`: Mengurutkan komponen secara vertikal dengan dukungan scroll dinamis.
   * `Row`: Menyusun komponen secara horizontal dengan proporsi flex/weight.
   * `Box`: Untuk meletakkan lencana status online atau ikon kamera di atas foto profil.
2. **State & Recomposition**:
   * Penggunaan delegate `by remember { mutableStateOf(...) }` untuk menampung nilai state.
   * Compose otomatis me-render ulang UI pada bagian yang terpengaruh saja saat state berubah.
3. **Modifiers**:
   * Pengaturan ukuran (`size()`, `height()`, `width()`).
   * Distribusi ruang layout (`fillMaxSize()`, `fillMaxWidth()`, `weight()`).
   * Desain visual (`padding()`, `background()`, `border()`, `clip()`).
4. **Resources Integration**:
   * Integrasi warna dinamis dari file XML Android (`R.color.xxx`) menggunakan fungsi `colorResource(id = R.color.xxx)`.
5. **Preview System**:
   * Anotasi `@Preview` dengan parameter `showBackground = true` dan `showSystemUi = true` untuk memvisualisasikan tampilan secara real-time pada mode Terang (Light) maupun Gelap (Dark) langsung di Android Studio.

---

## 🚀 Cara Menjalankan Aplikasi

### 1. Sinkronisasi Awal
1. Pastikan Anda telah memasang **Android Studio** (min. Flamingo/Hedgehog) dan mengonfigurasi **JDK 17**.
2. Buka proyek melalui Android Studio (**File** ➔ **Open** ➔ pilih folder `Week2_ProfilMahasiswa`).
3. Biarkan Android Studio mengunduh Gradle wrapper dan melakukan sinkronisasi dependensi proyek.

### 2. Memilih Halaman yang Aktif
Untuk mengganti halaman mana yang ingin ditampilkan pada emulator atau perangkat fisik Anda, silakan buka berkas `MainActivity.kt` dan ubah fungsi Composable di dalam blok `setContent` sebagai berikut:

```kotlin
setContent {
    ProfilMahasiswaTheme {
        // Hapus tanda komentar (//) pada baris halaman yang ingin Anda jalankan:
        
        ProfileScreen()       // Menampilkan Halaman Profil Utama
        // ProfileEditScreen() // Menampilkan Halaman Edit Profil
        // DataNilai()         // Menampilkan Halaman Transkrip Nilai Akademik
    }
}
```

### 3. Menggunakan Android Studio Preview
Anda tidak harus menjalankan emulator untuk melihat desain setiap layar. 
1. Buka salah satu file screen di folder `screens/` (misalnya `DataNilai.kt`).
2. Di pojok kanan atas jendela kode, klik tombol **Split** atau **Design**.
3. Android Studio akan membangun dan merender preview dari fungsi `@Preview` yang ada di bagian bawah kode program.
