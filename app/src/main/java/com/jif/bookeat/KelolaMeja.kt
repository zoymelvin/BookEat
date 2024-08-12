package com.jif.bookeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class KelolaMeja : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var kelolaMejaAdapter: KelolaMejaAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelola_meja)

        recyclerView = findViewById(R.id.rv_meja)
        recyclerView.layoutManager = GridLayoutManager(this, 4) // Set layout manager

        // Inisialisasi adapter
        val dataProses = generateDummyMejaList().toMutableList()
        kelolaMejaAdapter = KelolaMejaAdapter(dataProses)
        recyclerView.adapter = kelolaMejaAdapter

        // Inisialisasi database reference
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().reference.child("resto").child(userID!!)

// Tambahkan orderByChild untuk mengurutkan berdasarkan nomor_meja
        databaseReference.child("meja").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear data lama sebelum menambahkan data baru
                kelolaMejaAdapter.clearData()

                // Iterasi melalui setiap item meja di Firebase dan tambahkan ke adapter
                for (mejaSnapshot in dataSnapshot.children) {
                    val nomorMeja = mejaSnapshot.child("nomor_meja").getValue(String::class.java)
                    val kapasitasMeja = mejaSnapshot.child("kapasitas_meja").getValue(String::class.java)

                    // Tambahkan item meja ke adapter
                    if (nomorMeja != null && kapasitasMeja != null) {
                        kelolaMejaAdapter.addItem(KMejaItem(nomorMeja, kapasitasMeja))
                    }
                }

                // Perbarui tampilan RecyclerView dengan memanggil notifyDataSetChanged pada objek adapter
                kelolaMejaAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error jika perlu
                Log.e("KelolaMeja", "Gagal membaca data meja: ${databaseError.message}")
            }
        })
        val back = findViewById<ImageButton>(R.id.btn_kelolaback)
        back.setOnClickListener {
            startActivity(Intent(this, DashboardResto::class.java))
        }
    }
    private fun generateDummyMejaList(): List<KMejaItem> {
        val mejaList = mutableListOf<KMejaItem>()
        for (i in 1..20) {
            val mejaItem = KMejaItem(i.toString(), "4",)
            mejaList.add(mejaItem)
        }
        return mejaList
    }
}

