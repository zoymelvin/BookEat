package com.jif.bookeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class DaftarMenu : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var menuAdapter: DaftarMenuAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_menu)

        recyclerView = findViewById(R.id.rv_pesanan)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi adapter dengan fungsi callback untuk edit dan delete
        menuAdapter = DaftarMenuAdapter(mutableListOf(), this::editMenuItem, this::deleteMenuItem)
        recyclerView.adapter = menuAdapter

        val userID = FirebaseAuth.getInstance().currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().reference.child("resto").child(userID!!).child("menu")

        databaseReference.orderByChild("menuID").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tempList = mutableListOf<DaftarMenuItem>()
                for (menuSnapshot in dataSnapshot.children) {
                    val id = menuSnapshot.key ?: ""
                    val namaMenu = menuSnapshot.child("namaMenu").getValue(String::class.java) ?: ""
                    val imageUrl = menuSnapshot.child("imageUrl").getValue(String::class.java) ?: ""
                    val hargaInt = menuSnapshot.child("hargaMenu").getValue(Int::class.java) ?: 0
                    val harga = "Rp$hargaInt"

                    val menuItem = DaftarMenuItem(id, namaMenu, imageUrl, harga)
                    tempList.add(menuItem)
                }
                menuAdapter.clearData()
                menuAdapter.addAll(tempList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("DaftarMenu", "Gagal membaca data menu: ${databaseError.message}")
            }
        })

        setupButtons()
    }

    private fun editMenuItem(menuItem: DaftarMenuItem) {
        val intent = Intent(this, EditMenu::class.java)
        intent.putExtra("MENU_ID", menuItem.id)
        startActivity(intent)
    }

    private fun deleteMenuItem(menuItem: DaftarMenuItem) {
        databaseReference.child(menuItem.id).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Menu berhasil dihapus.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal menghapus menu.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupButtons() {
        findViewById<ImageButton>(R.id.ib_addmenu).setOnClickListener {
            startActivity(Intent(this, TambahMenu::class.java))
        }

        findViewById<ImageButton>(R.id.btn_kelolaback).setOnClickListener {
            startActivity(Intent(this, DashboardResto::class.java))
        }
    }
}