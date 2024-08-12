package com.jif.bookeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLog = findViewById<Button>(R.id.btn_log)
        btnLog.setOnClickListener(View.OnClickListener {
            // Handle button click event
            when {
                // Check which radio button is selected
                (findViewById<View>(R.id.radio_button_resto) as RadioButton).isChecked -> {
                    // If radio_button_resto is selected, navigate to LandingPage1Resto
                    val intent = Intent(this, LandingPage1::class.java)
                    startActivity(intent)
                }
                (findViewById<View>(R.id.radio_button_user) as RadioButton).isChecked -> {
                    // If radio_button_user is selected, navigate to LandingPage1User
                    val intent = Intent(this, UserLogin::class.java)
                    startActivity(intent)
                }
                else -> {
                    // Handle the case when neither radio button is selected
                    Toast.makeText(this, "Pilih tipe masuk (Resto/User)", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}