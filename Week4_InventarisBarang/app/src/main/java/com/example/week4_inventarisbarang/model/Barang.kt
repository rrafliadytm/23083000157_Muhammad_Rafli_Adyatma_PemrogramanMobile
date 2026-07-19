package com.example.week4_inventarisbarang.model

/**
 * Data class Barang merepresentasikan satu item barang.
 */
data class Barang(
    val id: Long = System.currentTimeMillis(),
    val nama: String = "",
    val kategori: String = "",
    val harga: Double = 0.0,
    val stok: Int = 0,
    val sku: String = "",
    val berat: Double = 0.0,
    val deskripsi: String = "",
    val gambarUri: String = ""
)
