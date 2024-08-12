package com.jif.bookeat

data class KMejaItem(
    val nomorMeja: String,
    val kapasitasMeja: String,
    var isSelected: Boolean = false // true untuk Menu tersedia dan false untuk Menu kosong
)
