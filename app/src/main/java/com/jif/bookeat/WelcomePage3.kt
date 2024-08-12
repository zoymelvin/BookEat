package com.jif.bookeat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class WelcomePage3 : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var btnDaftar: Button
    private lateinit var btnLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // Image Button Slide
        val view = inflater.inflate(R.layout.fragment_welcome_page3, container, false)
        val btnBackfrag = view.findViewById<ImageButton>(R.id.ib_back_welcomepage3)
        btnBackfrag.setOnClickListener {
            val backFrag = WelcomePage2()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.tampilwelcomepage,backFrag)
            transaction.commit()
        }

        // Button Daftar

        btnDaftar = view.findViewById(R.id.btn_register)

        // Menambahkan listener untuk tombol keranjang
        btnDaftar.setOnClickListener {
            val intent = Intent(activity, RegisterPage::class.java)
            startActivity(intent)
        }

        btnLogin = view.findViewById(R.id.tv_punya_akun)

        // Menambahkan listener untuk tombol keranjang
        btnLogin.setOnClickListener {
            val intent = Intent(activity, LoginPage::class.java)
            startActivity(intent)
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WelcomePage3.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WelcomePage3().apply {
                arguments = Bundle().apply {
                }
            }
    }
}