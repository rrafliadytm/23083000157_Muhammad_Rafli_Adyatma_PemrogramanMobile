# 📝 MyNoteApp (RafliNote Master) ✨

Aplikasi catatan (*Note-Taking App*) berbasis Android modern yang dirancang untuk platform Android menggunakan **Jetpack Compose**, **Kotlin**, dan **Material Design 3**. Proyek ini dibuat dengan arsitektur **MVVM (Model-View-ViewModel)** bersih dan state management reaktif berbasis Kotlin **StateFlow**.

Aplikasi ini dikembangkan untuk proyek praktikum **Pemrograman Mobile (Semester 6)**.

---

## 🚀 Teknologi & Library Stack

Di bawah ini adalah teknologi utama yang digunakan untuk membangun **MyNoteApp**:

![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Material 3](https://img.shields.io/badge/Material_3-7F39FB?style=for-the-badge&logo=materialdesign&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)

---

## 🌟 Fitur Utama Aplikasi & Detail Implementasi

Berikut adalah rincian seluruh fitur yang ditawarkan oleh aplikasi **MyNoteApp (RafliNote Master)** beserta penjelasan teknis implementasinya:

### 🏠 1. Dashboard Catatan & Empty State
Halaman utama ([DashboardScreen.kt](file:///E:/Lainnya/Tugas/Semester%206/Pemrograman%20Mobile/Project/Week12_MyNoteApp/app/src/main/java/com/example/week12_mynoteapp/ui/screens/DashboardScreen.kt)) berfungsi sebagai wadah untuk menampilkan seluruh catatan yang telah dibuat.
- **📥 Tampilan Empty State**: Jika tidak ada catatan dalam memori, aplikasi secara cerdas menampilkan pesan instruktif:
  > *“Belum ada catatan. Ketuk + untuk membuat baru.”*
- **📜 Scrollable List**: Menggunakan komponen performan `LazyColumn` untuk merender daftar catatan dalam bentuk kartu secara efisien meskipun jumlah catatan sangat banyak.
- **➕ Floating Action Button (FAB) Modern**: Extended FAB bertuliskan **"+ Catatan"** di pojok kanan bawah yang memudahkan pengguna menambahkan catatan baru dengan satu ketukan.

### 🎴 2. Kartu Catatan Dinamis (Note Card)
Setiap item catatan dibungkus dalam komponen `Card` Material 3 yang elegan dengan fitur:
- **🔍 Preview Konten Terbatas**: Menampilkan pratinjau teks catatan hingga **maksimal 3 baris** saja. Jika teks melebihi 3 baris, aplikasi secara otomatis memotongnya dengan efek elipsis (`...`) menggunakan `TextOverflow.Ellipsis` untuk menjaga kerapian tata letak UI.
- **📅 Format Tanggal Real-Time**: Menampilkan waktu pembuatan atau pembaruan terakhir catatan dengan format yang mudah dibaca (*locale-aware*): `dd MMM yyyy, HH:mm` (Contoh: `19 Jul 2026, 22:18`).
- **🗑️ Aksi Cepat Hapus**: Terdapat tombol ikon tempat sampah (`IconButton` dengan `Icons.Default.Delete`) langsung di dalam kartu untuk menghapus catatan tersebut secara instan tanpa perlu masuk ke menu editor.

### ✍️ 3. Editor Catatan Imersif
Halaman [EditorScreen.kt](file:///E:/Lainnya/Tugas/Semester%206/Pemrograman%20Mobile/Project/Week12_MyNoteApp/app/src/main/java/com/example/week12_mynoteapp/ui/screens/EditorScreen.kt) menyediakan antarmuka menulis yang bersih dan fokus:
- **🔄 Mode Dinamis**: Judul AppBar berubah secara dinamis tergantung pada konteks tindakan pengguna:
  - **"Catatan Baru"** 🆕: Jika pengguna sedang membuat catatan kosong baru.
  - **"Edit Catatan"** ✏️: Jika pengguna sedang memodifikasi catatan yang sudah ada.
- **💻 Input Teks Tanpa Batas**: Menggunakan komponen `TextField` yang dikustomisasi dengan latar belakang transparan dan tanpa garis pembatas bawah. Fokus penuh tertuju pada teks yang ditulis.
- **✔️ Tombol Aksi Cerdas**:
  - Tombol **Batal (Panah Kembali)** untuk kembali ke Dashboard tanpa menyimpan perubahan.
  - Tombol **Simpan (Centang)** yang hanya akan aktif (`enabled = textState.isNotBlank()`) ketika kolom teks sudah terisi karakter non-spasi. Ini mencegah penyimpanan catatan kosong yang tidak disengaja.

### 🧠 4. Manajemen State & Pengurutan Otomatis
Logika bisnis pada [NoteViewModel.kt](file:///E:/Lainnya/Tugas/Semester%206/Pemrograman%20Mobile/Project/Week12_MyNoteApp/app/src/main/java/com/example/week12_mynoteapp/viewmodel/NoteViewModel.kt) menangani manipulasi data secara reaktif:
- **💾 In-Memory Storage**: Menyimpan data menggunakan `MutableStateFlow` bertipe `List<Note>`. Setiap perubahan data akan langsung memicu pembaruan UI secara otomatis.
- **🔀 Pengurutan Waktu Terbaru**: Saat catatan diperbarui, catatan tersebut otomatis diurutkan kembali berdasarkan waktu perubahan terbaru secara menurun (`sortedByDescending { it.updatedAt }`). Catatan yang paling terakhir Anda edit akan selalu berada di posisi paling atas di Dashboard.
- **📌 Penyematan Instan Catatan Baru**: Catatan baru secara default langsung dimasukkan ke posisi paling awal daftar catatan (`listOf(newNote) + currentNotes`).

### 📱 5. UI Modern & Edge-to-Edge
- **🖼️ Tampilan Edge-to-Edge**: Didukung penuh pada [MainActivity.kt](file:///E:/Lainnya/Tugas/Semester%206/Pemrograman%20Mobile/Project/Week12_MyNoteApp/app/src/main/java/com/example/week12_mynoteapp/MainActivity.kt) menggunakan `enableEdgeToEdge()`. Konten aplikasi akan dirender secara penuh hingga ke bawah bilah navigasi sistem dan bilah status untuk visualisasi yang tanpa batas (*frameless*).
- **🌓 Tema Gelap/Terang Otomatis**: Dilengkapi dengan konfigurasi tema dinamis Material 3 pada paket `ui/theme` yang menyesuaikan dengan setelan tema sistem operasi Android.

---

## 🔄 Ringkasan Alur Fitur & Interaksi

| Fitur | Komponen UI | Trigger Aksi | Logika Bisnis (`ViewModel`) | Target Halaman / Rute |
| :--- | :--- | :--- | :--- | :--- |
| **Buka Aplikasi** 🏁 | `MainActivity` | Startup Aplikasi | Inisialisasi daftar kosong | `Screen.Dashboard` |
| **Tambah Catatan Baru** ➕ | `ExtendedFloatingActionButton` | Klik tombol **+ Catatan** | - | Navigasi ke `Screen.Editor` (tanpa ID) |
| **Menulis & Validasi** ✏️ | `TextField` | Input teks karakter | Mengaktifkan tombol Simpan jika teks tidak kosong | Tetap di halaman Editor |
| **Simpan Catatan Baru** 💾 | `IconButton` (Centang) | Klik tombol Simpan | Menghasilkan ID unik (`System.currentTimeMillis()`) lalu menyisipkan ke daftar teratas | Navigasi kembali ke Dashboard |
| **Pilih/Edit Catatan** 🔍 | `NoteCard` | Klik pada kartu catatan | Membaca catatan berdasarkan ID lewat `getNoteById(id)` | Navigasi ke `Screen.Editor` (membawa argumen `noteId`) |
| **Simpan Pembaruan** 🔄 | `IconButton` (Centang) | Klik tombol Simpan | Mengganti konten lama dan memperbarui tanggal (`updatedAt`), lalu mengurutkan ulang daftar secara descending | Navigasi kembali ke Dashboard |
| **Batal Mengedit** ❌ | `IconButton` (ArrowBack) | Klik tombol Batal | Tidak melakukan perubahan apa pun | Navigasi kembali ke Dashboard |
| **Hapus Catatan** 🗑️ | `IconButton` (Trash) | Klik tombol Hapus | Menghapus item dari daftar dengan `filterNot { it.id == id }` | Tetap di Dashboard |

---

## 📂 Struktur Proyek & Peta Kode Sumber

Aplikasi ini tersusun atas berkas-berkas kode sumber utama berikut:

```text
Week12_MyNoteApp/
└── 📦 app/
    └── 📂 src/main/java/com/example/week12_mynoteapp/
        ├── 📄 MainActivity.kt ─── Entry point aplikasi yang memuat tema dan navigasi utama.
        │
        ├── 📂 model/
        │   └── 📄 Note.kt ─── Data class entitas Catatan (id, content, updatedAt).
        │
        ├── 📂 navigation/
        │   ├── 📄 Screen.kt ─── Definisi rute navigasi ("dashboard" & "editor?noteId={noteId}").
        │   └── 📄 MyNoteNavGraph.kt ─── Pengatur alur halaman Compose dan penanganan passing argumen noteId.
        │
        ├── 📂 viewmodel/
        │   └── 📄 NoteViewModel.kt ─── Otak pengolah data catatan (tambah, edit, hapus, & pencarian).
        │
        └── 📂 ui/
            ├── 📂 screens/
            │   ├── 📄 DashboardScreen.kt ─── Halaman utama yang memuat daftar kartu catatan (NoteCard).
            │   └── 📄 EditorScreen.kt ─── Halaman pengeditan catatan dengan validasi teks.
            └── 📂 theme/
                └── 📄 Color.kt, Type.kt, Theme.kt ─── Konfigurasi tema warna, tipografi, dan skema Material 3.
```

---

## ⚙️ Persyaratan Sistem & Dependensi

Proyek ini dikonfigurasi menggunakan Gradle dengan Kotlin DSL. Berikut adalah informasi ringkas dependensi utamanya:
- **🔌 Min SDK**: `24` (Android 7.0 Nougat)
- **🎯 Target SDK**: `34` (Android 14)
- **☕ Kompatibilitas Kotlin**: `1.9.0` / `2.0+`
- **📦 Komponen Utama**:
  - `androidx.compose.ui:ui`
  - `androidx.compose.material3:material3`
  - `androidx.navigation:navigation-compose`
  - `androidx.lifecycle:lifecycle-viewmodel-compose`

---

## 🚀 Cara Menjalankan Proyek

1. **📂 Buka Project**:
   Jalankan Android Studio, lalu pilih **Open** dan pilih folder [Week12_MyNoteApp](file:///E:/Lainnya/Tugas/Semester%206/Pemrograman%20Mobile/Project/Week12_MyNoteApp).
2. **🔄 Gradle Sync**:
   Biarkan gradle mengunduh semua library yang dibutuhkan hingga proses sinkronisasi selesai.
3. **📱 Run Application**:
   Pastikan emulator atau HP Android fisik (dengan USB Debugging aktif) sudah tersambung, kemudian klik ikon **Run** (segitiga hijau) atau tekan tombol `Shift + F10` pada keyboard.
