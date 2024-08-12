package com.jif.bookeat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat

class HalRiwayat : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HalRiwayatAdapter
    private lateinit var riwayatPesananRef: DatabaseReference

    companion object {
        fun newInstance() = HalRiwayat()
    }

    private lateinit var viewModel: HalRiwayatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hal_proses, container, false)

        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.rv_proses)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inisialisasi Adapter
        adapter = HalRiwayatAdapter(mutableListOf())
        recyclerView.adapter = adapter

        // Inisialisasi DatabaseReference
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        riwayatPesananRef = FirebaseDatabase.getInstance().reference.child("resto")
            .child(userID ?: "").child("riwayatpesanan")

        // Tambahkan ValueEventListener untuk mendengarkan perubahan data
        riwayatPesananRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Ambil data dari dataSnapshot dan ubah ke dalam List<RiwayatItem>
                val riwayatItems = mutableListOf<RiwayatItem>()

                for (riwayatSnapshot in dataSnapshot.children) {
                    // Iterasi melalui setiap menu dalam pesanan
                    for (menuSnapshot in riwayatSnapshot.children) {
                        val jumlah = menuSnapshot.child("jumlah").getValue(Int::class.java) ?: 0
                        val namaMenu = menuSnapshot.key ?: ""
                        val harga = menuSnapshot.child("harga").getValue(Int::class.java) ?: 0
                        val nomorMeja = menuSnapshot.child("nomorMeja").getValue(String::class.java) ?: ""
                        val waktu = menuSnapshot.child("logs").getValue(String::class.java) ?: ""

                        val riwayatItem = RiwayatItem(jumlah, namaMenu, harga, nomorMeja, waktu)
                        riwayatItems.add(riwayatItem)
                    }
                }

                // Set data ke dalam adapter
                adapter.setData(riwayatItems)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("HalRiwayat", "Gagal membaca data riwayat pesanan: ${databaseError.message}")
            }
        })

        return view
    }

    // Fungsi untuk mendapatkan data proses
    private fun getDataProses(): List<RiwayatItem> {
        // Implementasikan logika untuk mendapatkan data proses
        // Misalnya, ambil data dari sumber daya atau database
        // Contoh sederhana:
        return listOf()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HalRiwayatViewModel::class.java)
        // TODO: Use the ViewModel
    }
}