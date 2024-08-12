package com.jif.bookeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton

class LandingPage1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page1)

        val wcpage = WelcomePage1()

        // Menampilkan fragment di dalam container
        supportFragmentManager.beginTransaction()
            .replace(R.id.tampilwelcomepage,wcpage)
            .commit()
    }
}