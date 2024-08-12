package com.jif.bookeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView

class BuatPesanan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buat_pesanan)

        val back = findViewById<ImageButton>(R.id.btn_kelolaback)
        back.setOnClickListener {
            startActivity(Intent(this, DashboardResto::class.java))
        }

        val pilihmenu = MilihMenu()

        // Menampilkan fragment di dalam container
        supportFragmentManager.beginTransaction()
            .replace(R.id.tampilfragment,pilihmenu)
            .commit()
    }
}