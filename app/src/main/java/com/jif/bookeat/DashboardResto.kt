package com.jif.bookeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.bumptech.glide.Glide

class DashboardResto : AppCompatActivity() {
    private lateinit var restoNameTextView: TextView
    private lateinit var restoAddressTextView: TextView
    private lateinit var restoImageView: ImageView
    private lateinit var restoJamBuka: TextView
    private lateinit var restoJamTutup: TextView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var cv_lpanalisis: CardView
    private lateinit var cv_lpuang: CardView
    private lateinit var tv_paket: TextView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var navHeaderRestoName: TextView
    private lateinit var navHeaderEmail: TextView
    private lateinit var navHeaderImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_resto)

        restoNameTextView = findViewById(R.id.tv_namaresto)
        restoAddressTextView = findViewById(R.id.tv_alamatresto)
        restoImageView = findViewById(R.id.iv_fotoresto)
        restoJamBuka = findViewById(R.id.tv_openingresto)
        restoJamTutup = findViewById(R.id.tv_closingresto)
        cv_lpanalisis = findViewById(R.id.cv_lpanalisis)
        cv_lpuang = findViewById(R.id.cv_lpuang)
        tv_paket = findViewById(R.id.tv_paket)

        val userID = FirebaseAuth.getInstance().currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().reference.child("resto").child(userID!!)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val restoName = dataSnapshot.child("nama").getValue(String::class.java)
                    val restoAddress = dataSnapshot.child("alamat").getValue(String::class.java)
                    val restoImageUrl = dataSnapshot.child("photo_url").getValue(String::class.java)
                    val statusSubscription = dataSnapshot.child("status_subs").getValue(Boolean::class.java)
                    val restojamBuka = dataSnapshot.child("jam_buka").getValue(String::class.java)
                    val restojamTutup = dataSnapshot.child("jam_tutup").getValue(String::class.java)
                    // Update UI based on subscription status
                    handleSubscriptionStatus(statusSubscription)

                    restoNameTextView.text = restoName
                    restoAddressTextView.text = restoAddress
                    restoJamBuka.text = restojamBuka
                    restoJamTutup.text = restojamTutup

                    try {
                        if (!restoImageUrl.isNullOrEmpty()) {
                            Glide.with(this@DashboardResto)
                                .load(restoImageUrl)
                                .into(restoImageView)
                        }
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navigationView : NavigationView = findViewById(R.id.nav_view)

        val navHeaderView = navigationView.getHeaderView(0)

        navHeaderRestoName = navHeaderView.findViewById(R.id.nav_header_name)
        navHeaderEmail = navHeaderView.findViewById(R.id.nav_header_email)
        navHeaderImageView = navHeaderView.findViewById(R.id.nav_header_image)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val restoName = dataSnapshot.child("nama").getValue(String::class.java)
                    val restoEmail = dataSnapshot.child("email").getValue(String::class.java)
                    val restoImageUrl = dataSnapshot.child("photo_url").getValue(String::class.java)

                    navHeaderRestoName.text = restoName
                    navHeaderEmail.text = restoEmail

                    try {
                        if (!restoImageUrl.isNullOrEmpty()) {
                            Glide.with(this@DashboardResto)
                                .load(restoImageUrl)
                                .into(navHeaderImageView)
                        }
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        val btndaftarmenu = findViewById<CardView>(R.id.cv_daftarmenu)
        btndaftarmenu.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DaftarMenu::class.java)
            startActivity(intent)
        })

        val btnkelolameja = findViewById<CardView>(R.id.cv_kelolameja)
        btnkelolameja.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, KelolaMeja::class.java)
            startActivity(intent)
        })

        val btnriwayatpesanan = findViewById<CardView>(R.id.cv_riwayatpesanan)
        btnriwayatpesanan.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RiwayatPesanan::class.java)
            startActivity(intent)
        })

        val btnbuatpesanan = findViewById<CardView>(R.id.cv_buatpesanan)
        btnbuatpesanan.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, BuatPesanan::class.java)
            startActivity(intent)
        })

        val ibNavigasiMenu = findViewById<ImageButton>(R.id.ib_navigasimenu)
        ibNavigasiMenu.setOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> Toast.makeText(applicationContext, "Halaman Home", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> {
                    Toast.makeText(applicationContext, "Halaman Profil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)
                }
                R.id.nav_logout -> {
                    signOut()
                }
                R.id.nav_upgrade -> {
                    Toast.makeText(applicationContext, "Halaman berlangganan", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LandingPageSubs::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    //Tempat Handle Subscription
    private fun handleSubscriptionStatus(statusSubscription: Boolean?) {
        if (statusSubscription == true) {
            // Subscription is active, show features accordingly
            activateSubscriptionFeature()
        } else {
            // Subscription is not active, handle as needed
            showSubscriptionPrompt()
        }
    }

    private fun activateSubscriptionFeature() {
        // Implement logic to activate subscription feature
        cv_lpanalisis.visibility = View.VISIBLE
        cv_lpuang.visibility = View.VISIBLE
        tv_paket.visibility = View.VISIBLE
    }

    private fun showSubscriptionPrompt() {
        // Implement logic to show a prompt for subscription
        cv_lpanalisis.visibility = View.GONE
        cv_lpuang.visibility = View.GONE
        tv_paket.visibility = View.INVISIBLE
    }
    private fun signOut() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()

        // Setelah keluar, arahkan pengguna ke halaman login atau halaman lain yang sesuai
        Toast.makeText(applicationContext, "Logout berhasil", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginPage::class.java)
        startActivity(intent)
        finish() // Tutup halaman saat ini
    }
}
