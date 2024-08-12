package com.jif.bookeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LandingPageSubs : AppCompatActivity() {

    private lateinit var btnPaketGratis: Button
    private lateinit var btnPaketBerlangganan: Button
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page_subs)

        val userID = FirebaseAuth.getInstance().currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().reference.child("resto").child(userID!!)
        // Inisialisasi tombol
        btnPaketGratis = findViewById(R.id.btn_paketgratis)
        btnPaketBerlangganan = findViewById(R.id.btn_paketberlangganan)

        // Set listener untuk masing-masing tombol
        btnPaketGratis.setOnClickListener {
            // User mengklik tombol gratis, pindah ke DashboardResto dengan showBasicFeatures
//            navigateToDashboard { showBasicFeatures() }
            val intent = Intent(this, DashboardResto::class.java)
            startActivity(intent)
        }
        btnPaketBerlangganan.setOnClickListener {
            handleSubscriptionButtonClick()
        }

        // Periksa status subscription dan atur tampilan tombol
        checkSubscriptionStatus()
    }

    private fun redirectToPayment() {
        simulatePaymentSuccess()
    }

    private fun simulatePaymentSuccess() {
        // Simulasi pembayaran berhasil
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val databaseReference = FirebaseDatabase.getInstance().reference.child("resto").child(userID!!)

        // Perbarui status subscription menjadi true di Firebase
        databaseReference.child("status_subs").setValue(true)
            .addOnSuccessListener {
                // Proses update berhasil
                Toast.makeText(this, "Status langganan diperbarui", Toast.LENGTH_SHORT).show()

                // Pindah ke DashboardResto setelah pembayaran berhasil
                val intent = Intent(this, DashboardResto::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                // Proses update gagal
                Toast.makeText(this, "Gagal memperbarui status langganan", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkSubscriptionStatus() {
        databaseReference.child("status_subs").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val statusSubscription = dataSnapshot.getValue(Boolean::class.java)

                // Atur tampilan tombol berlangganan berdasarkan status subscription
                updateButtonBasedOnSubscriptionStatus(statusSubscription)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error jika perlu
            }
        })
    }

    private fun updateButtonBasedOnSubscriptionStatus(statusSubscription: Boolean?) {
        if (statusSubscription == true) {
            // Jika user sudah subscribe, ubah teks tombol menjadi "Cancel"
            btnPaketBerlangganan.text = "Cancel"
        } else {
            // Jika user belum subscribe, ubah teks tombol menjadi "Bayar"
            btnPaketBerlangganan.text = "Bayar"
        }
    }

    private fun handleSubscriptionButtonClick() {
        databaseReference.child("status_subs").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val statusSubscription = dataSnapshot.getValue(Boolean::class.java)

                // Jika status subscription adalah true, maka user ingin membatalkan berlangganan
                if (statusSubscription == true) {
                    cancelSubscription()
                } else {
                    // Jika status subscription adalah false, maka user ingin berlangganan atau melakukan pembayaran
                    redirectToPayment()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error jika perlu
            }
        })
    }

    private fun cancelSubscription() {
        // Implementasikan logika pembatalan berlangganan
        // ...

        // Setelah pembatalan berhasil, perbarui status subscription di Firebase menjadi false
        databaseReference.child("status_subs").setValue(false)
            .addOnSuccessListener {
                Toast.makeText(this, "Berlangganan dibatalkan", Toast.LENGTH_SHORT).show()
                updateButtonBasedOnSubscriptionStatus(false)

                // Pindah ke DashboardResto setelah pembayaran berhasil
                val intent = Intent(this, DashboardResto::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal membatalkan berlangganan", Toast.LENGTH_SHORT).show()
            }
    }
}