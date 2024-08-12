package com.jif.bookeat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileEdit : AppCompatActivity() {
    private lateinit var etEditNama: EditText
    private lateinit var etEditNomor: EditText
    private lateinit var etEditEmail: EditText
    private lateinit var etEditAlamat: EditText
    private lateinit var etEditJamBuka: EditText
    private lateinit var etEditJamTutup: EditText
    private lateinit var ibEditFotoResto: ImageButton
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentUserID: String

    private val IMAGE_PICK_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        etEditNama = findViewById(R.id.et_editnama)
        etEditNomor = findViewById(R.id.et_editnomor)
        etEditEmail = findViewById(R.id.et_editemail)
        etEditAlamat = findViewById(R.id.et_editalamat)
        etEditJamBuka = findViewById(R.id.et_editjambuka)
        etEditJamTutup = findViewById(R.id.et_editjamtutup)
        ibEditFotoResto = findViewById(R.id.ib_editfoto_resto)

        // Pemanggilan fungsi untuk menampilkan data
        displayProfileData()

        val gantiprofil = findViewById<Button>(R.id.btn_ubahprofil)
        gantiprofil.setOnClickListener(View.OnClickListener {
            // Perbarui data profil saat tombol "btn_ubahprofil" ditekan
            updateProfileData()
        })

        val btnSelesaiEdit = findViewById<Button>(R.id.btn_ubahprofil)
        btnSelesaiEdit.setOnClickListener {
            // Pindah ke halaman profil setelah selesai mengedit
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)

            // Update data profil setelah berpindah ke halaman profil
            updateProfileData()
        }
        val btnback = findViewById<Button>(R.id.btn_backtoprofil)
        btnback.setOnClickListener {
            // Pindah ke halaman profil setelah selesai mengedit
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        ibEditFotoResto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_REQUEST)
        }
    }
    private fun displayProfileData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUserID = currentUser!!.uid
        databaseReference = FirebaseDatabase.getInstance().reference.child("resto").child(currentUserID)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val nama = dataSnapshot.child("nama").getValue(String::class.java) ?: ""
                val nomor = dataSnapshot.child("phone").getValue(String::class.java) ?: ""
                val email = dataSnapshot.child("email").getValue(String::class.java) ?: ""
                val alamat = dataSnapshot.child("alamat").getValue(String::class.java) ?: ""
                val jamBuka = dataSnapshot.child("jam_buka").getValue(String::class.java) ?: ""
                val jamTutup = dataSnapshot.child("jam_tutup").getValue(String::class.java) ?: ""

                etEditNama.setText(nama)
                etEditNomor.setText(nomor)
                etEditEmail.setText(email)
                etEditAlamat.setText(alamat)
                etEditJamBuka.setText(jamBuka)
                etEditJamTutup.setText(jamTutup)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error jika perlu
                Log.e("ProfileEdit", "Gagal membaca data profil: ${databaseError.message}")
            }
        })
    }

    private fun updateProfileData() {
        val namaBaru = etEditNama.text.toString()
        val nomorBaru = etEditNomor.text.toString()
        val emailBaru = etEditEmail.text.toString()
        val alamatBaru = etEditAlamat.text.toString()
        val jamBukaBaru = etEditJamBuka.text.toString()
        val jamTutupBaru = etEditJamTutup.text.toString()

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUserID = currentUser!!.uid
        databaseReference =
            FirebaseDatabase.getInstance().reference.child("resto").child(currentUserID)

        val updateData: MutableMap<String, Any> = HashMap()
        updateData["nama"] = namaBaru
        updateData["phone"] = nomorBaru
        updateData["email"] = emailBaru
        updateData["alamat"] = alamatBaru
        updateData["jam_buka"] = jamBukaBaru
        updateData["jam_tutup"] = jamTutupBaru

        databaseReference.updateChildren(updateData)
            .addOnSuccessListener {
                Toast.makeText(this, "Profil berhasil diubah", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mengubah profil", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_REQUEST && resultCode == RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                if (!::databaseReference.isInitialized) {
                    // Menginisialisasi kembali databaseReference jika belum diinisialisasi
                    currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
                    databaseReference =
                        FirebaseDatabase.getInstance().reference.child("resto").child(currentUserID)
                }

                val storageReference = FirebaseStorage.getInstance().reference
                val imageRef = storageReference.child("images").child("$currentUserID.jpg")

                imageRef.putFile(it)
                    .addOnSuccessListener { taskSnapshot ->
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageURL = uri.toString()

                            val updateData: MutableMap<String, Any> = HashMap()
                            updateData["photo_url"] = imageURL

                            databaseReference.updateChildren(updateData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Foto berhasil diubah", Toast.LENGTH_SHORT)
                                        .show()
                                    // Refresh tampilan setelah berhasil mengubah foto
                                    // Implementasikan fungsi untuk memperbarui tampilan foto di sini
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Gagal mengubah foto", Toast.LENGTH_SHORT)
                                        .show()
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Gagal mengunggah foto", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}

