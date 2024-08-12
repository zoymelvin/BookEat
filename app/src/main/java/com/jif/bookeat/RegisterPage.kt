package com.jif.bookeat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterPage : AppCompatActivity() {
    private lateinit var namaresto: EditText
    private lateinit var phoneresto: EditText
    private lateinit var emailresto: EditText
    private lateinit var pwresto: EditText
    private lateinit var alamatresto: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    private lateinit var selectedImageUri: Uri
    private lateinit var storageReference: StorageReference
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        reference = database.reference.child("resto")

        val storage = FirebaseStorage.getInstance()
        storageReference = storage.reference.child("resto_photos")

        namaresto = findViewById(R.id.et_namaresto)
        phoneresto = findViewById(R.id.et_notelp)
        emailresto = findViewById(R.id.et_emailresto)
        pwresto = findViewById(R.id.et_passwordresto)
        alamatresto = findViewById(R.id.et_alamatresto)

        val btnDaftar = findViewById<Button>(R.id.btn_register)
        btnDaftar.setOnClickListener {
            registerResto()
        }

        val uploadresto: ImageButton = findViewById(R.id.ib_upload_resto)
        uploadresto.setOnClickListener {
            pickImage()
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
            val viewimg = findViewById<ImageView>(R.id.iv_upload_resto)
            viewimg.setImageURI(selectedImageUri)
            // imgPreview.setImageURI(selectedImageUri)
        }
    }

    private fun uploadRestoImage() {
        val user = auth.currentUser
        val userID = user?.uid

        if (userID != null) {
            val photoRef = storageReference.child("$userID/resto_photo")

            photoRef.putFile(selectedImageUri)
                .addOnSuccessListener { uploadTask ->
                    if (uploadTask.task.isSuccessful) {
                        photoRef.downloadUrl.addOnSuccessListener { uri ->
                            val restoImageUrl = uri.toString()
                            saveRestoDataToDatabase(restoImageUrl)
                            Toast.makeText(
                                this,
                                "berhasil mengunggah gambar",
                                Toast.LENGTH_SHORT
                            ).show()
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

    private fun saveRestoDataToDatabase(restoImageUrl: String) {
        val user = auth.currentUser
        val userID = user?.uid

        if (userID != null) {
            val restoData = HashMap<String, Any>()
            restoData["nama"] = namaresto.text.toString().trim()
            restoData["phone"] = phoneresto.text.toString().trim()
            restoData["email"] = emailresto.text.toString().trim()
            restoData["alamat"] = alamatresto.text.toString().trim()
            restoData["photo_url"] = restoImageUrl
            restoData["jam_buka"] = "10:00" // Jam buka restoran
            restoData["jam_tutup"] = "21:00" // Jam tutup restoran
            restoData["status_subs"] = false
            restoData["logs-registration"] ="yyyy-MM-dd HH:mm:ss"
            restoData["logs-login"] ="yyyy-MM-dd HH:mm:ss"


            reference.child(userID).setValue(restoData)
                .addOnCompleteListener { databaseTask ->
                    if (databaseTask.isSuccessful) {
                        // Tambahkan kode berikut di sini untuk membuat tabel menu secara default setelah pengguna berhasil mendaftar
                        val mejaReference = reference.child(userID).child("meja")
                        for (i in 1..20) {
                            val meja = HashMap<String, Any>()
                            meja["nomor_meja"] = i.toString()
                            meja["kapasitas_meja"] = "4" // Sesuaikan dengan kapasitas meja yang diinginkan
                            meja["status"] = false

                            mejaReference.push().setValue(meja)
                                .addOnSuccessListener {
                                    // Berhasil menambahkan meja secara default
                                }
                                .addOnFailureListener {
                                    // Gagal menambahkan meja secara default
                                    // Tambahkan penanganan kesalahan yang diperlukan
                                }
                        }
                        // Tambahkan orderByChild untuk mengurutkan berdasarkan nomor_meja
                        mejaReference.orderByChild("nomor_meja").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // Data meja telah diurutkan berdasarkan nomor_meja
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle error jika perlu
                            }
                        })
                        // Akhir kode untuk membuat tabel menu secara default

                        Toast.makeText(
                            this,
                            "Registrasi berhasil",
                            Toast.LENGTH_SHORT
                        ).show()

                        logRegistrationTime(userID)

                        val intent = Intent(this, LoginPage::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Gagal Register",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun logRegistrationTime(userID: String) {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedTime = dateFormat.format(currentTime)

        // Simpan waktu registrasi ke dalam Realtime Database
        val logRef = reference.child(userID).child("logs-registration")
        logRef.setValue(formattedTime)
            .addOnSuccessListener {
                // Log waktu registrasi berhasil disimpan
            }
            .addOnFailureListener {
                // Log waktu registrasi gagal disimpan, handle kesalahan jika perlu
            }
    }

    private fun registerResto() {
        // Validasi tambahan atau tindakan sebelum mendaftar akun
        if (isValidRegistration()) {
            val emailResto = emailresto.text.toString().trim()
            val passwordResto = pwresto.text.toString().trim()

            auth.createUserWithEmailAndPassword(emailResto, passwordResto)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Proses daftar akun berhasil
                        uploadRestoImage()
                    } else {
                        // Proses daftar akun gagal
                        Toast.makeText(
                            this,
                            "Registrasi gagal. Pastikan email dan password valid.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            // Tampilkan pesan atau ambil tindakan jika validasi tambahan tidak berhasil
            Toast.makeText(
                this,
                "Mohon lengkapi semua kolom dengan benar.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isValidRegistration(): Boolean {
        // Implementasikan validasi tambahan jika diperlukan
        // Misalnya, periksa apakah semua kolom telah diisi dengan benar
        val nama = namaresto.text.toString().trim()
        val phone = phoneresto.text.toString().trim()
        val email = emailresto.text.toString().trim()
        val password = pwresto.text.toString().trim()
        val alamat = alamatresto.text.toString().trim()

        return !nama.isEmpty() && !phone.isEmpty() && !email.isEmpty() && !password.isEmpty() && !alamat.isEmpty()
    }
}