package com.jif.bookeat

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class LoginPage : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        auth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.loginuser)
        passwordEditText = findViewById(R.id.loginpass)

        val loginButton: Button = findViewById(R.id.btn_restolog)
        loginButton.setOnClickListener {
            loginUser()
        }
        val resetpass: TextView = findViewById(R.id.lupa_password)
        resetpass.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_forgot, null)
            val userEmail = view.findViewById<EditText>(R.id.editBox)
            builder.setView(view)
            val dialog = builder.create()
            view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                compareEmail(userEmail)
                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }
            if (dialog.window != null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }
    }
    private fun compareEmail(email: EditText){
        if (email.text.toString().isEmpty()){
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }
        auth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login berhasil
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                    updateLoginLog()
                    checkSubscriptionStatusAndNavigate()
                    // Selesaikan Activity saat ini agar tidak bisa kembali ke halaman login
                } else {
                    // Login gagal, tampilkan pesan kesalahan
                    Toast.makeText(this, "Login gagal", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun checkSubscriptionStatusAndNavigate() {
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val databaseReference = FirebaseDatabase.getInstance().reference.child("resto").child(userID!!)
        Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val statusSubscription = dataSnapshot.child("status_subs").getValue(Boolean::class.java)



                    if (statusSubscription == true) {
                        // Pengguna sudah subscribe, navigasi ke LandingPageSubs atau DashboardResto
                        navigateToLandingPageOrDashboard()
                    } else {
                        // Pengguna belum subscribe, navigasi ke halaman pilih paket subscribe
                        navigateToChoosePackage()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle onCancelled
            }
        })
    }
    private fun navigateToLandingPageOrDashboard() {
        // Pilih antara LandingPageSubs atau DashboardResto berdasarkan kebutuhan
        val intent = Intent(this, DashboardResto::class.java)
        startActivity(intent)
        finish() // Selesaikan Activity saat ini agar tidak bisa kembali ke halaman login
    }

    private fun navigateToChoosePackage() {
        val intent = Intent(this, LandingPageSubs::class.java)
        startActivity(intent)
        finish() // Selesaikan Activity saat ini agar tidak bisa kembali ke halaman login
    }

    private fun updateLoginLog() {
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val databaseReference = FirebaseDatabase.getInstance().reference.child("resto").child(userID!!)
        val loginLogValue = getCurrentDateTime()
        databaseReference.child("logs-login").setValue(loginLogValue)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Berhasil mengupdate logs-login
                    Log.d("LoginPage", "Login log berhasil diupdate")
                } else {
                    // Gagal mengupdate login-log
                    Log.e("LoginPage", "Gagal mengupdate login log")
                }
            }
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}
