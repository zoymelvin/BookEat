package com.jif.bookeat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditMenu : AppCompatActivity() {
    private lateinit var namamenu: EditText
    private lateinit var deksripsimenu: EditText
    private lateinit var hargamenu: EditText
    private lateinit var ivMenuPhoto: ImageView
    private lateinit var selectedImageUri: Uri
    private var menuId: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_menu)

        auth = FirebaseAuth.getInstance()
        val userID = auth.currentUser?.uid ?: return
        reference = FirebaseDatabase.getInstance().reference.child("resto").child(userID)
        storageReference = FirebaseStorage.getInstance().reference.child("resto_photos").child(userID)

        namamenu = findViewById(R.id.et_namamenu)
        deksripsimenu = findViewById(R.id.et_deskripsimenu)
        hargamenu = findViewById(R.id.et_hargamenu)
        ivMenuPhoto = findViewById(R.id.iv_upload_menu)

        menuId = intent.getStringExtra("MENU_ID") ?: return
        if (menuId != null) {
            loadExistingData(menuId!!)
        }

        findViewById<ImageButton>(R.id.btn_upload_menu).setOnClickListener {
            pickImage()
        }

        findViewById<ImageButton>(R.id.btn_kelolaback).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.btn_editmenu).setOnClickListener {
            updateMenu(menuId!!)
            finish()
        }
    }

    private fun loadExistingData(menuId: String) {
        reference.child("menu").child(menuId).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                namamenu.setText(dataSnapshot.child("namaMenu").value.toString())
                deksripsimenu.setText(dataSnapshot.child("deskripsiMenu").value.toString())
                hargamenu.setText(dataSnapshot.child("hargaMenu").value.toString())
                val imageUrl = dataSnapshot.child("imageUrl").value.toString()
                if (imageUrl.isNotEmpty()) {
                    Glide.with(this).load(imageUrl).into(ivMenuPhoto)
                }
            } else {
                Toast.makeText(this, "Menu tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mengambil data dari database", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data!!
            ivMenuPhoto.setImageURI(selectedImageUri)
        }
    }

    private fun updateMenu(menuId: String) {
        val namaMenu = namamenu.text.toString().trim()
        val deskripsiMenu = deksripsimenu.text.toString().trim()
        val hargaMenu = hargamenu.text.toString().trim()

        // Memeriksa apakah semua kolom telah diisi
        if (namaMenu.isEmpty() || deskripsiMenu.isEmpty() || hargaMenu.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        // Memeriksa apakah harga menu adalah angka yang valid
        val hargaMenuInt = hargaMenu.toIntOrNull()
        if (hargaMenuInt == null) {
            Toast.makeText(this, "Harga menu harus berupa angka", Toast.LENGTH_SHORT).show()
            return
        }

        // Memeriksa apakah gambar telah dipilih dan diinisialisasi
        if (::selectedImageUri.isInitialized) {
            // Jika gambar baru dipilih, unggah gambar tersebut dan perbarui database
            uploadMenuImage(menuId, namaMenu, deskripsiMenu, hargaMenuInt)
        } else {
            // Jika tidak ada gambar baru yang dipilih, perbarui database saja
            updateMenuInDatabase(menuId, namaMenu, deskripsiMenu, hargaMenuInt, null)
        }
    }

    private fun uploadMenuImage(menuId: String, namaMenu: String, deskripsiMenu: String, hargaMenuInt: Int) {
        val photoRef = storageReference.child("menu_photos/$menuId/menu_photo.jpg")

        photoRef.putFile(selectedImageUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    // Jika berhasil mengunggah, perbarui database dengan URL gambar baru
                    updateMenuInDatabase(menuId, namaMenu, deskripsiMenu, hargaMenuInt, uri.toString())
                }
            }
            .addOnFailureListener {
                // Menangani kegagalan pengunggahan gambar
                Toast.makeText(this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateMenuInDatabase(menuId: String, namaMenu: String, deskripsiMenu: String, hargaMenuInt: Int, imageUrl: String?) {
        val menuData = hashMapOf<String, Any>(
            "namaMenu" to namaMenu,
            "deskripsiMenu" to deskripsiMenu,
            "hargaMenu" to hargaMenuInt
        )
        imageUrl?.let {
            menuData["imageUrl"] = it
        }

        // Memperbarui data menu di database
        reference.child("menu").child(menuId).updateChildren(menuData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Perubahan menu berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()  // Menutup activity setelah berhasil menyimpan perubahan
                } else {
                    Toast.makeText(this, "Gagal menyimpan perubahan", Toast.LENGTH_SHORT).show()
                }
            }
    }
}