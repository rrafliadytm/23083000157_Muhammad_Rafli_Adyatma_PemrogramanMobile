# 📋 Aplikasi Inventaris Barang (InventarisKu)

<p align="center">
  <a href="#">
    <img src="https://img.shields.io/badge/Kotlin-2.0.0-purple.svg?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
  </a>
  <a href="#">
    <img src="https://img.shields.io/badge/Android-CompileSDK_36-green.svg?style=for-the-badge&logo=android&logoColor=white" alt="Android SDK"/>
  </a>
  <a href="#">
    <img src="https://img.shields.io/badge/Jetpack_Compose-Material_3-3DDC84.svg?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose"/>
  </a>
  <a href="#">
    <img src="https://img.shields.io/badge/Storage-SharedPreferences_&_GSON-orange.svg?style=for-the-badge&logo=json&logoColor=white" alt="Storage"/>
  </a>
</p>

Aplikasi Android modern untuk mengelola stok dan inventaris barang secara praktis. Aplikasi ini dirancang dengan antarmuka yang bersih, intuitif, dan responsif menggunakan **Kotlin** dan **Jetpack Compose** berbasis panduan desain **Material 3**. 

Penyimpanan data bersifat lokal dan persisten menggunakan kombinasi **SharedPreferences** dan library **Google GSON** untuk serialisasi objek JSON. Pendekatan ini membuat aplikasi sangat ringan tanpa perlu mengonfigurasi database SQLite atau Room, namun data tetap tersimpan aman meskipun perangkat dimatikan atau aplikasi ditutup.

Proyek ini dikembangkan sebagai bagian dari tugas praktikum mata kuliah **Pemrograman Mobile**.

---

## 🎨 Peningkatan Visual & Ikon Cerdas (New Updates)

Untuk meningkatkan pengalaman pengguna dan estetika aplikasi, kami telah menerapkan sistem ikonografi visual modern di seluruh halaman menggunakan pustaka ikon Material:

*   **🏷️ Input SKU**: Didampingi oleh `Icons.Default.Tag` untuk visualisasi kode penanda yang ringkas.
*   **📦 Input & Info Stok**: Didampingi oleh `Icons.Default.Inventory` untuk representasi fisik stok barang di gudang.
*   **⚖️ Input & Info Berat**: Didampingi oleh `Icons.Default.Scale` untuk menampung bobot barang dalam satuan kg.
*   **🛍️ Input Nama Barang**: Didampingi oleh `Icons.Default.ShoppingBag` pada form tambah/edit.
*   **🗂️ Input & Tampilan Kategori**: Didampingi oleh `Icons.Default.Category` baik di baris input maupun langsung di dalam setiap kartu daftar barang (`BarangCard`).
*   **💳 Input Harga**: Didampingi oleh `Icons.Default.Payments` untuk visualisasi transaksi keuangan.
*   **📝 Input Deskripsi**: Didampingi oleh `Icons.Default.Description` pada editor teks multiline.

---

## 📸 Demo Tampilan & Fitur Utama

Aplikasi **InventarisKu** dirancang dengan arsitektur UI berbasis *State-driven* yang membagi fungsionalitas ke dalam beberapa modul tampilan utama:

### 1. Dashboard Utama (`DaftarBarangScreen`)
Dashboard merupakan gerbang utama aplikasi yang menyajikan ikhtisar seluruh stok gudang Anda secara real-time.
*   **Large Top App Bar**: Header modern Material 3 yang mencantumkan nama aplikasi "InventarisKu" beserta sub-judul informatif *"Kelola stok barang Anda"*.
*   **Pencarian Dinamis (Live Search)**: Kotak pencarian dengan sudut membulat (*Rounded Corner 16.dp*) dan ikon pencarian di sisi kiri. Pencarian berjalan secara instan (*recomposition*) ketika pengguna mengetik kata kunci, mencakup pencarian berdasarkan **Nama Barang** maupun **Kategori**.
*   **Kartu Inventaris Kreatif (`BarangCard`)**:
    *   **Desain Elegan**: Berbentuk kartu (*Card*) dengan sudut membulat (*Rounded Corner 20.dp*) dengan opasitas tipis pada latar belakang (*surfaceVariant.copy(alpha = 0.3f)*) dan elevasi 2.dp untuk efek kedalaman visual.
    *   **Pratinjau Gambar**: Menampilkan foto barang yang diambil dari galeri perangkat secara asinkron menggunakan library **Coil**. Jika barang tidak memiliki foto, aplikasi secara otomatis menampilkan ikon kontainer `Inventory2` sebagai representasi visual default (*fallback*).
    *   **Tampilan Kategori Visual**: Menampilkan label kategori di bawah nama barang dengan tambahan ikon `Category` kecil berwarna sekunder untuk mempertegas identitas kategori barang.
    *   **Indikator Stok Cerdas**: Label visual berwarna dinamis. Jika stok barang **kurang dari 5 unit**, label akan berubah warna menjadi **Merah (Error Container)** untuk memberikan peringatan visual cepat bahwa stok hampir habis. Jika stok aman (5 unit atau lebih), label berwarna **Hijau (Tertiary Container)**.
    *   **Format Rupiah Otomatis**: Semua data harga langsung dikonversi ke format mata uang Rupiah secara formal (misalnya `Rp 15.000.000,00`) menggunakan API `java.text.NumberFormat` dengan konfigurasi region lokal Indonesia (`Locale("id", "ID")`).
    *   **Aksi Cepat Hapus**: Ikon tong sampah di sisi kanan kartu memungkinkan pengguna menghapus barang secara instan langsung dari halaman daftar tanpa harus masuk ke halaman detail.
*   **Empty State Adaptif**:
    *   **Gudang Kosong**: Jika belum ada barang yang terdaftar sama sekali, aplikasi menampilkan grafis ikon `Inventory2` besar berwarna pudar dengan petunjuk pesan *"Gudang masih kosong. Mulai tambah barang inventaris Anda"*.
    *   **Hasil Pencarian Tidak Ditemukan**: Jika pengguna mencari barang namun kata kuncinya tidak cocok dengan data mana pun, pesan berganti secara dinamis menjadi *"Barang tidak ditemukan. Coba kata kunci lain"*.

### 2. Detail Spesifikasi Produk (`DetailBarangScreen`)
Menyediakan halaman rincian lengkap untuk melihat spesifikasi teknis dari satu item barang terpilih.
*   **Center Aligned Top App Bar**: Header yang memosisikan judul "Detail Produk" tepat di tengah demi estetika seimbang, dilengkapi tombol navigasi kembali (*Back Arrow*) di sebelah kiri dan pintasan edit (*Edit Icon*) di sebelah kanan.
*   **Header Gambar Besar**: Area visualisasi gambar berukuran tinggi 300.dp dengan sudut melengkung halus (*Rounded Corner 24.dp*) untuk menampilkan detail foto produk secara maksimal dengan skala *Crop*.
*   **Spesifikasi Terstruktur (Detail Row)**:
    *   Setiap properti didampingi oleh ikon simbolis berwarna primer:
        *   🏷️ **SKU (Stock Keeping Unit)**: Menggunakan ikon `Tag` untuk menampilkan kode unik identifikasi inventaris.
        *   📦 **Stok Tersedia**: Menggunakan ikon `Inventory` untuk menampilkan volume unit barang saat ini.
        *   ⚖️ **Berat Barang**: Menggunakan ikon `Scale` untuk menampilkan berat barang dalam satuan kilogram (kg).
    *   Informasi Kategori dan Nama Barang ditampilkan dengan ukuran teks besar (*Headline Medium* & *Extra Bold*).
*   **Deskripsi Panjang**: Bagian khusus di bawah spesifikasi untuk menampung penjelasan detail mengenai kegunaan atau kondisi barang. Jika deskripsi dikosongkan oleh pengguna pada saat input, tampilan akan secara otomatis menampilkan teks penampung *"Tidak ada deskripsi."*.

### 3. Formulir Dinamis Tambah & Edit (`TambahBarangScreen`)
Formulir input pintar serbaguna yang berjalan secara dinamis tergantung pada konteks navigasi yang diakses.
*   **Fungsi Ganda (Insert & Update)**: Jika dibuka dari tombol tambah di dashboard, halaman akan bertindak sebagai form input barang baru (dengan judul header *"Tambah Barang"*). Jika dibuka dari halaman detail barang tertentu, form akan otomatis terisi dengan data barang yang dipilih sebelumnya (*autofill*) dan berfungsi untuk memperbarui data tersebut (dengan judul header *"Edit Barang"*).
*   **Gallery Image Picker Terintegrasi**:
    *   Mengintegrasikan launcher native Android `rememberLauncherForActivityResult` dengan kontrak `ActivityResultContracts.OpenDocument()`.
    *   Menggunakan filter MIME type `image/*` untuk membuka galeri foto bawaan sistem Android.
    *   **Penanganan Izin Akses URI Persisten**: Mengimplementasikan baris kode `context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)` di dalam blok *try-catch*. Hal ini krusial agar aplikasi tetap berhak membaca URI foto lokal yang dipilih pengguna dari galeri meskipun aplikasi di-restart atau perangkat dinyalakan ulang.
*   **Tata Letak Form yang Efisien dengan Ikon Input**:
    *   Setiap input teks dilengkapi dengan **Leading Icon** representatif berwarna primer (seperti keranjang belanja, ikon koin bayar, timbangan berat, dst.) untuk menyajikan visualisasi yang menarik dan mudah dipahami.
    *   Field **Nama Barang**, **Kategori**, dan **Deskripsi** ditampilkan dalam lebar penuh.
    *   Field numerik dikelompokkan secara horizontal berdampingan untuk menghemat ruang layar:
        *   Baris 1: **Harga (Rp)** dan **Stok**
        *   Baris 2: **SKU** dan **Berat (kg)**
*   **Optimasi Keyboard**: Tipe input keyboard disesuaikan secara otomatis dengan jenis data yang dimasukkan (misalnya `KeyboardType.Number` untuk Stok/Harga, dan `KeyboardType.Decimal` untuk Berat).
*   **Validasi Keamanan Input**: Aplikasi mencegah penyimpanan data jika field wajib seperti **Nama Barang** masih kosong (`nama.isBlank()`), guna menghindari kekacauan indeks pada penyimpanan inventaris.

---

## 🛠️ Spesifikasi Teknologi & Library

Aplikasi ini dibangun menggunakan standar pengembangan Android modern:

| Komponen | Spesifikasi / Library | Keterangan |
| :--- | :--- | :--- |
| **Bahasa Pemrograman** | Kotlin v2.0.0+ | Bahasa modern berorientasi objek dengan dukungan penuh Compose compiler |
| **UI Framework** | Jetpack Compose (Material 3) | Toolkit deklaratif native untuk mendesain antarmuka Android |
| **Ikon Ekstensi** | Compose Material Icons Extended | Pustaka ikon tambahan Google untuk menyajikan ikonografi spesifik |
| **Navigasi** | Navigation Compose v2.8.5 | Manajemen alur perpindahan halaman dan pengiriman argumen bertipe data |
| **Image Loading** | Coil Compose v2.7.0 | Pemuatan gambar asinkron berkinerja tinggi langsung dari URI penyimpanan lokal |
| **Serialisasi Data** | Google GSON v2.11.0 | Mengonversi daftar objek Kotlin menjadi string JSON untuk penyimpanan lokal |
| **Penyimpanan Lokal** | SharedPreferences | API bawaan Android untuk menyimpan data pasangan key-value secara persisten |
| **Min SDK** | API 26 (Android 8.0 Oreo) | Aplikasi dapat berjalan pada 90%+ perangkat Android aktif |
| **Target SDK** | API 35 (Android 15) | Memenuhi standar kepatuhan Google Play Store terbaru |
| **Java JVM** | JDK 17 | Standar kompilator modern untuk kestabilan build Gradle |

---

## 📂 Struktur Arsitektur Proyek

Struktur folder di dalam modul `app` mengikuti pola organisasi fungsional yang bersih dan mudah dipahami:

```text
com.example.week4_inventarisbarang/
│
├── MainActivity.kt                  # Activity utama, memicu edge-to-edge, inisialisasi Repository, & host NavController
│
├── model/
│   └── Barang.kt                     # Data Class representasi entitas Barang beserta parameter default-nya
│
├── data/
│   └── BarangRepository.kt           # Lapisan data penanggung jawab operasi CRUD (Create, Read, Update, Delete) ke SharedPreferences
│
├── navigation/
│   └── AppNavigation.kt              # Deklarasi rute navigasi ("daftar", "detail/{id}", "tambah", "edit/{id}")
│
├── screen/
│   ├── DaftarBarangScreen.kt         # Tampilan dashboard daftar inventaris, pencarian, status stok, dan hapus cepat
│   ├── DetailBarangScreen.kt         # Tampilan rincian spesifikasi teknis barang dan navigasi ke halaman edit
│   └── TambahBarangScreen.kt         # Formulir input/edit barang beserta picker gambar galeri & validasi input
│
└── ui/theme/
    ├── Color.kt                      # Definisi skema warna primer, sekunder, tersier, dan varian Material 3
    ├── Theme.kt                      # Pengaturan tema global yang mendukung penyesuaian otomatis Light/Dark Mode
    └── Type.kt                       # Konfigurasi tipografi, ukuran font, dan berat teks (weight)
```

---

## 🔐 Keamanan & Izin Akses (Permissions)

Untuk memuat gambar barang yang dipilih dari penyimpanan ponsel pengguna secara lokal, aplikasi mendeklarasikan izin akses media di dalam berkas `AndroidManifest.xml`. Deklarasi ini ditulis secara adaptif agar kompatibel dengan kebijakan privasi sistem Android versi lama maupun versi terbaru:

1.  **Android 12 (SDK 32) ke Bawah**:
    Menggunakan izin standar untuk membaca penyimpanan eksternal secara umum:
    ```xml
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:maxSdkVersion="32" />
    ```
2.  **Android 13 (SDK 33) ke Atas**:
    Mengikuti kebijakan privasi terbaru Android yang lebih ketat dengan meminta izin khusus untuk media gambar saja (*Granular Media Permission*):
    ```xml
    <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
    ```

Aplikasi ini juga menggunakan mekanisme **Persistable URI Permission** untuk memastikan bahwa ketika pengguna memilih gambar, hak akses baca terhadap file tersebut dipertahankan oleh OS Android untuk aplikasi ini bahkan setelah sistem mengalami reboot (*device restart*).

---

## 🚀 Cara Menjalankan Proyek di Mesin Lokal

Ikuti panduan berikut untuk melakukan kompilasi dan menjalankan aplikasi ini pada komputer atau laptop Anda:

### 1. Prasyarat Sistem
*   Pastikan Anda telah mengunduh dan menginstal **Android Studio** versi stabil terbaru (sangat direkomendasikan versi *Ladybug / Koala* atau yang lebih baru).
*   Pastikan Java Development Kit (**JDK 17**) sudah terpasang dan dipilih sebagai Gradle JDK pada pengaturan Android Studio Anda (`Settings -> Build, Execution, Deployment -> Build Tools -> Gradle`).
*   Koneksi internet yang stabil untuk mengunduh dependensi Gradle saat sinkronisasi pertama kali.

### 2. Langkah-Langkah Pemasangan
1.  **Download / Kloning Repositori**:
    Unduh zip proyek ini dan ekstraksi ke direktori lokal Anda, atau lakukan kloning menggunakan Git Bash:
    ```bash
    git clone https://github.com/username/Week4_InventarisBarang.git
    ```
2.  **Buka di Android Studio**:
    *   Buka Android Studio.
    *   Pilih menu **Open** atau **Import Project**.
    *   Arahkan ke folder hasil ekstraksi `Week4_InventarisBarang`, lalu klik **OK**.
3.  **Sinkronisasi Gradle**:
    *   Tunggu beberapa menit hingga proses sinkronisasi Gradle (*Gradle sync*) selesai mengunduh seluruh dependensi perpustakaan yang dibutuhkan (Jetpack Compose, GSON, Coil, dll.).
    *   Pastikan tidak ada pesan error pada jendela *Build Output* di bagian bawah.

### 3. Menjalankan Aplikasi
*   **Menggunakan Emulator**:
    *   Buka *Device Manager* di Android Studio, lalu jalankan salah satu Virtual Device (Emulator) Anda yang menggunakan API tingkat 26 ke atas.
    *   Klik tombol **Run** (ikon segitiga hijau `▶`) di toolbar bagian atas.
*   **Menggunakan Perangkat Fisik (HP Android)**:
    *   Aktifkan menu **Developer Options** (Opsi Pengembang) dan aktifkan fitur **USB Debugging** pada perangkat HP Android Anda.
    *   Hubungkan HP ke laptop/komputer Anda dengan kabel USB yang kompatibel.
    *   Pilih nama perangkat fisik HP Anda pada daftar drop-down perangkat di Android Studio.
    *   Klik tombol **Run** (ikon segitiga hijau `▶`) untuk mulai menginstal aplikasi langsung ke HP Anda.
