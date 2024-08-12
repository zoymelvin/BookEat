package com.jif.bookeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.NumberFormat

class StrukDetailPesanan : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var StrukAdapter: StrukDetailAdapter
    private lateinit var databaseReference: DatabaseReference
    private var totalHarga: Int = 0
    private lateinit var btnKembaliKeMenu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_struk_detail_pesanan)
        recyclerView = findViewById(R.id.rv_struk)
        recyclerView.layoutManager = LinearLayoutManager(this) // Set layout manager

        // Inisialisasi adapter
        StrukAdapter = StrukDetailAdapter(mutableListOf()) // Inisialisasi dengan daftar kosong
        recyclerView.adapter = StrukAdapter

        // Inisialisasi database reference
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().reference.child("resto").child(userID!!)

// Tambahkan orderByChild untuk mengurutkan berdasarkan nomor_meja
        databaseReference.child("detailpesanan").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear data lama sebelum menambahkan data baru
                StrukAdapter.clearData()
                // Iterasi melalui setiap item detailpesanan di Firebase
                for (detailPesananSnapshot in dataSnapshot.children) {
                    // Iterasi melalui setiap item menu di dalam detailpesanan
                    for (menuSnapshot in detailPesananSnapshot.children) {
                        val jumlah = menuSnapshot.child("jumlah").getValue(Int::class.java) ?: 0
                        val namaMenu = menuSnapshot.child("namaMenu").getValue(String::class.java) ?: ""
                        val hargaInt = menuSnapshot.child("harga").getValue(Int::class.java) ?: 0
                        val noMeja = menuSnapshot.child("nomorMeja").getValue(String::class.java) ?: ""

                        // Hitung total harga untuk setiap item (harga * jumlah)
                        val totalHargaItem = jumlah * hargaInt
                        totalHarga += totalHargaItem
                        val totalHargaTextView: TextView = findViewById(R.id.tv_totalharga)
                        totalHargaTextView.text = "Total = Rp ${NumberFormat.getNumberInstance().format(totalHarga)}"

                        val menuItem = StrukItem(jumlah, namaMenu, hargaInt, noMeja)
                        StrukAdapter.addMenu(menuItem)
                    }
                }
                // Perbarui tampilan RecyclerView dengan memanggil notifyDataSetChanged pada objek adapter
                StrukAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("DaftarMenu", "Gagal membaca data detail pesanan: ${databaseError.message}")
            }
        })

        // Inisialisasi button
        btnKembaliKeMenu = findViewById(R.id.btn_kembalikemenu)
        btnKembaliKeMenu.setOnClickListener {
            // Panggil metode untuk melakukan pemindahan data
            pindahkanDataKeRiwayatPesanan()

            // Tambahkan logika lainnya jika diperlukan
            // ...

            // Kembali ke menu atau lakukan tindakan lain
            kembaliKeMenu()
        }
    }
    private fun pindahkanDataKeRiwayatPesanan() {
        // Implementasikan logika pemindahan data ke riwayatpesanan di sini
        // Gunakan pendekatan yang telah disediakan sebelumnya
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val detailPesananRef = FirebaseDatabase.getInstance().reference.child("resto").child(userID!!).child("detailpesanan")

        detailPesananRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Iterasi melalui setiap item detail pesanan
                for (detailPesananSnapshot in dataSnapshot.children) {

                    // Pindahkan data ke riwayatpesanan
                    val riwayatPesananRef = FirebaseDatabase.getInstance().reference.child("resto").child(userID)
                        .child("riwayatpesanan")
                    // Tambahkan nilai ke anak-anak baru, gunakan push() untuk mendapatkan ID unik
                    val riwayatPesananChildRef = riwayatPesananRef.push()
                    riwayatPesananChildRef.setValue(detailPesananSnapshot.value)
                    // Hapus data dari detailpesanan
                    detailPesananSnapshot.ref.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("PindahData", "Gagal membaca data detail pesanan: ${databaseError.message}")
            }
        })
    }

    private fun kembaliKeMenu() {
        // Tambahkan logika untuk kembali ke menu atau tindakan lainnya
        // ...

        // Contoh kembali ke menu
        val intent = Intent(this, DashboardResto::class.java)
        startActivity(intent)

        // Tutup aktivitas saat ini jika diperlukan
        finish()
    }
}