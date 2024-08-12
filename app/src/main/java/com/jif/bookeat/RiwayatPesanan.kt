package com.jif.bookeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext

class RiwayatPesanan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_pesanan)

        val spinner: Spinner = findViewById(R.id.actv_pilihfragment)
        val options = resources.getStringArray(R.array.fragment_options)

        // Buat adapter untuk spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Atur listener untuk spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = options[position]
                handleItemSelected(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Implementasikan tindakan jika tidak ada item yang dipilih
                // Misalnya, hapus fragment atau lakukan sesuatu sesuai kebutuhan
                val fragmentTransaction = RiwayatPesanan().supportFragmentManager.beginTransaction()
                val fragment = BlankFragment() // Gantilah BlankFragment dengan fragment kosong yang Anda miliki
                fragmentTransaction.replace(R.id.fragmentContainerr, fragment)
                fragmentTransaction.commit()
            }
        }
        val back = findViewById<ImageButton>(R.id.btn_kelolaback)
        back.setOnClickListener {
            startActivity(Intent(this, DashboardResto::class.java))
        }
    }
    private fun handleItemSelected(selectedItem: String) {
        when (selectedItem) {
            "Proses" -> {
                // Tampilkan fragment untuk proses
                val hal_proses = HalProses()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment, hal_proses)
                    commit()
                }
                // Gantilah dengan tindakan yang sesuai
                Toast.makeText(this, resources.getString(R.string.proses_message), Toast.LENGTH_SHORT).show()
            }
            "Riwayat" -> {
                // Tampilkan fragment untuk riwayat
                val hal_riwayat = HalRiwayat()
                // Gantilah dengan tindakan yang sesuai
                Toast.makeText(this, resources.getString(R.string.riwayat_message), Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment, hal_riwayat)
                    commit()
                }
            }
        }
    }
}