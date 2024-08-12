package com.jif.bookeat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class KontenProfil : Fragment() {
    private lateinit var nameTextView: TextView
    private lateinit var tlpTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var alamatTextView: TextView
    private lateinit var jambukaTextView: TextView
    private lateinit var jamtutupTextView: TextView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentUserID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_konten_profil, container, false)

        nameTextView = view.findViewById(R.id.nameTextView)
        tlpTextView = view.findViewById(R.id.tlpTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        alamatTextView = view.findViewById(R.id.tv_alamatTextView)
        jambukaTextView = view.findViewById(R.id.jam_buka)
        jamtutupTextView = view.findViewById(R.id.jam_tutup)

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUserID = currentUser!!.uid
        databaseReference = FirebaseDatabase.getInstance().reference.child("resto").child(currentUserID)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val nama = dataSnapshot.child("nama").getValue(String::class.java)
                    val email = dataSnapshot.child("email").getValue(String::class.java)
                    val notlp = dataSnapshot.child("phone").getValue(String::class.java)
                    val alamat = dataSnapshot.child("alamat").getValue(String::class.java)
                    val jambuka = dataSnapshot.child("jam_buka").getValue(String::class.java)
                    val jamtutup = dataSnapshot.child("jam_tutup").getValue(String::class.java)

                    nameTextView.text = nama
                    emailTextView.text = email
                    tlpTextView.text = notlp
                    alamatTextView.text = alamat
                    jambukaTextView.text = jambuka
                    jamtutupTextView.text = jamtutup
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

        return view
    }
}
