package com.example.week4_inventarisbarang.data

import android.content.Context
import android.content.SharedPreferences
import com.example.week4_inventarisbarang.model.Barang
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BarangRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("inventaris_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val KEY_DAFTAR = "daftar_barang"

    fun getSemuaBarang(): List<Barang> {
        val json = prefs.getString(KEY_DAFTAR, null) ?: return emptyList()
        val type = object : TypeToken<List<Barang>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun simpanSemuaBarang(daftar: List<Barang>) {
        val json = gson.toJson(daftar)
        prefs.edit().putString(KEY_DAFTAR, json).apply()
    }

    fun tambahBarang(barang: Barang) {
        val daftar = getSemuaBarang().toMutableList()
        daftar.add(barang)
        simpanSemuaBarang(daftar)
    }

    fun getBarangById(id: Long): Barang? {
        return getSemuaBarang().find { it.id == id }
    }

    fun hapusBarang(id: Long) {
        val daftar = getSemuaBarang().filter { it.id != id }
        simpanSemuaBarang(daftar)
    }

    fun updateBarang(barang: Barang) {
        val daftar = getSemuaBarang().map {
            if (it.id == barang.id) barang else it
        }
        simpanSemuaBarang(daftar)
    }
}
