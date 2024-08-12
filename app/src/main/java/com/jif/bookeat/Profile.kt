package com.jif.bookeat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Profile : AppCompatActivity() {
    private lateinit var namaProfilTextView: TextView
    private lateinit var emailProfilTextView: TextView
    private lateinit var fotoProfilImageView: ImageView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentUserID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val editprofil = findViewById<Button>(R.id.btn_editprofil)
        editprofil.setOnClickListener {
            startActivity(Intent(this, ProfileEdit::class.java))
        }
        val back = findViewById<ImageButton>(R.id.btn_kelolaback)
        back.setOnClickListener {
            startActivity(Intent(this, DashboardResto::class.java))
        }

        // Buat instance dari fragment
        val kp = KontenProfil()

        // Menampilkan fragment di dalam container
        supportFragmentManager.beginTransaction()
            .replace(R.id.fr_kontenprofil, kp)
            .commit()

        namaProfilTextView = findViewById(R.id.tv_namauser)
        emailProfilTextView = findViewById(R.id.tv_emailuser)
        fotoProfilImageView = findViewById(R.id.iv_fotouser)

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUserID = currentUser!!.uid
        databaseReference = FirebaseDatabase.getInstance().reference.child("resto").child(currentUserID)

        databaseReference.addValueEventListener(profileValueEventListener)
    }

    private val profileValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (isDestroyed) {
                // Jika aktivitas sudah dihancurkan, hentikan pemrosesan
                return
            }

            if (dataSnapshot.exists()) {
                val nama = dataSnapshot.child("nama").getValue(String::class.java)
                val email = dataSnapshot.child("email").getValue(String::class.java)
                val fotoUrl = dataSnapshot.child("photo_url").getValue(String::class.java)

                namaProfilTextView.text = nama
                emailProfilTextView.text = email

                if (!fotoUrl.isNullOrEmpty()) {
                    Glide.with(this@Profile)
                        .load(fotoUrl)
                        .into(fotoProfilImageView)
                } else {
                    // Glide.with(this@Profile).load(R.drawable.default_photo).into(fotoProfilImageView)
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle error
        }
    }
}
