package com.jif.bookeat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MilihMenu.newInstance] factory method to
 * create an instance of this fragment.
 */
class MilihMenu : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var menuAdapter: MilihMenuAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var btnKembaliKeHome: Button
    private lateinit var btnLanjutMilihMeja: Button
    private var detailPesananId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_milih_menu, container, false)

        recyclerView = view.findViewById(R.id.rv_milihmenu)
        recyclerView.layoutManager = GridLayoutManager(context, 3) // Set layout manager

        // Inisialisasi adapter
        menuAdapter = MilihMenuAdapter(mutableListOf()) // Inisialisasi dengan daftar kosong
        recyclerView.adapter = menuAdapter

        // Inisialisasi database reference
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().reference.child("resto").child(userID!!).child("menu")

        // Tambahkan orderByChild untuk mengurutkan berdasarkan menuID
        databaseReference.orderByChild("menuID").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear data lama sebelum menambahkan data baru
                menuAdapter.clearData()

                // Iterasi melalui setiap item menu di Firebase dan tambahkan ke adapter
                for (menuSnapshot in dataSnapshot.children) {
                    val namaMenu = menuSnapshot.child("namaMenu").getValue(String::class.java) ?: ""
                    val imageUrl = menuSnapshot.child("imageUrl").getValue(String::class.java) ?: ""
                    val hargaInt = menuSnapshot.child("hargaMenu").getValue(Int::class.java) ?: 0

                    val menuItem = pesananMenuItem(namaMenu, imageUrl, hargaInt)
                    menuAdapter.addMenu(menuItem)
                }

                // Perbarui tampilan RecyclerView dengan memanggil notifyDataSetChanged pada objek adapter
                menuAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error jika perlu
                Log.e("DaftarMenu", "Gagal membaca data menu: ${databaseError.message}")
            }
        })

        btnKembaliKeHome = view.findViewById(R.id.btn_kembalikehome)
        btnLanjutMilihMeja = view.findViewById(R.id.btn_lanjutmilihmeja)
        btnKembaliKeHome.setOnClickListener {
            // Memulai Intent ke Activity yang menampung fragment MilihMeja
            val intent = Intent(activity, DashboardResto::class.java)
            startActivity(intent)
        }
        btnLanjutMilihMeja.setOnClickListener {
            // Simpan pilihan menu ke dalam database
            val selectedMenuList = menuAdapter.getSelectedMenu()

            // ID unik untuk setiap detail pesanan
            val detailPesananId = FirebaseDatabase.getInstance().reference
                .child("resto").child(userID!!).child("detailpesanan").push().key!!

            // Simpan pilihan menu dan ID detail pesanan ke argument fragment berikutnya
            val nextFrag = MilihMeja.newInstance(detailPesananId, selectedMenuList)
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.tampilfragment, nextFrag)
            transaction.commit()
        }
        return view
    }
}