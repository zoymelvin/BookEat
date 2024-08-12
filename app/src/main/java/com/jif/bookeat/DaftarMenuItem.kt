package com.jif.bookeat

import java.util.Date

data class DaftarMenuItem(
    var id: String,
    val namaMenu: String,
    val fotoMenu: String, // Gunakan tipe data yang sesuai dengan sumber daya gambar (contoh: R.drawable.menu1)
    val harga: String,
    val _terjual: Int = 0,
    val _disukai: Int = 0,
    val _rating: Double = 0.0,
    val _datetime: Date = Date(),
    val _jmlhterjual: Int = 0,
    val _hargatotal: Int = 0,
)