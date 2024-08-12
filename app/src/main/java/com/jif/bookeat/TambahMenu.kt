package com.jif.bookeat

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class TambahMenu : AppCompatActivity() {
    private lateinit var namamenu: EditText
    private lateinit var deksripsimenu: EditText
    private lateinit var hargamenu: EditText
    private lateinit var Buttonmenu: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var selectedImageUri: Uri
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_menu)

        auth = FirebaseAuth.getInstance()
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        reference = FirebaseDatabase.getInstance().reference.child("resto").child(userID!!)

        val storage = FirebaseStorage.getInstance()
        storageReference = storage.reference.child("resto_photos").child(userID)

        namamenu = findViewById(R.id.et_namamenu)
        deksripsimenu = findViewById(R.id.et_deskripsimenu)
        hargamenu = findViewById(R.id.et_hargamenu)

        val uploadMenu: ImageButton = findViewById(R.id.btn_upload_menu)
        uploadMenu.setOnClickListener {
            pickImage()
        }
        val back = findViewById<ImageButton>(R.id.btn_kelolaback)
        back.setOnClickListener {
            startActivity(Intent(this, DaftarMenu::class.java))
        }
        val buatmenu = findViewById<Button>(R.id.btn_tambahmenu)
        buatmenu.setOnClickListener {
            buatMenuBaru()
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
            // Tampilkan gambar yang dipilih ke pengguna (opsional)
            val viewimg = findViewById<ImageView>(R.id.iv_upload_menu)
            viewimg.setImageURI(selectedImageUri)
            // imgPreview.setImageURI(selectedImageUri)
        }
    }

    private fun buatMenuBaru() {
        // Validasi input sebelum membuat menu
        val namaMenu = namamenu.text.toString().trim()
        val deskripsiMenu = deksripsimenu.text.toString().trim()
        val hargaMenuString = hargamenu.text.toString().trim()

        if (namaMenu.isEmpty() || deskripsiMenu.isEmpty() || hargaMenuString.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua kolom", Toast.LENGTH_SHORT).show()
            return
        }

        val hargaMenu: Int
        try {
            hargaMenu = hargaMenuString.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Harga menu tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        // Jika gambar belum dipilih
        if (!::selectedImageUri.isInitialized) {
            Toast.makeText(this, "Pilih gambar menu terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        // Melakukan upload gambar dan membuat menu
        uploadMenuImage()
    }

    private fun uploadMenuImage() {
        val user = auth.currentUser
        val userID = user?.uid

        if (userID != null) {
            val menuReference = reference.child("menu").push()

            val photoRef = storageReference.child("/menu_photos/${menuReference.key}")

            photoRef.putFile(selectedImageUri)
                .addOnSuccessListener { uploadTask ->
                    if (uploadTask.task.isSuccessful) {
                        photoRef.downloadUrl.addOnSuccessListener { uri ->
                            val menuImageUrl = uri.toString()
                            tambahMenu(menuReference.key!!, menuImageUrl)
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Gagal mengunggah gambar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun tambahMenu(menuId: String, menuImageUrl: String) {
        val user = auth.currentUser
        val userID = user?.uid

        if (userID != null) {
            val menuReference = reference.child("menu").child(menuId)

            val menuData = HashMap<String, Any>()
            menuData["namaMenu"] = namamenu.text.toString().trim()
            menuData["deskripsiMenu"] = deksripsimenu.text.toString().trim()
            val hargaMenuString = hargamenu.text.toString().trim()
            try {
                val hargaMenuInt = hargaMenuString.toInt()
                menuData["hargaMenu"] = hargaMenuInt
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Harga menu tidak valid", Toast.LENGTH_SHORT).show()
                return
            }
            menuData["imageUrl"] = menuImageUrl

            menuReference.setValue(menuData)
                .addOnCompleteListener { databaseTask ->
                    if (databaseTask.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Menu berhasil ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this, DaftarMenu::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Gagal menambahkan menu",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}