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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MilihMeja : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mejaAdapter: MilihMejaAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var btnback: Button
    private lateinit var btnPesan: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_milih_meja, container, false)

        recyclerView = view.findViewById(R.id.rv_milihmeja)
        recyclerView.layoutManager = GridLayoutManager(context, 4)

        val dataProses = generateDummyMejaList().toMutableList()
        mejaAdapter = MilihMejaAdapter(dataProses)
        recyclerView.adapter = mejaAdapter

        // Inisialisasi database reference
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().reference.child("resto").child(userID!!)

        // Tambahkan listener untuk mendengarkan perubahan data pada child "meja"
        databaseReference.child("meja").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear data lama sebelum menambahkan data baru
                mejaAdapter.clearData()

                // Iterasi melalui setiap item meja di Firebase dan tambahkan ke adapter
                for (mejaSnapshot in dataSnapshot.children) {
                    val nomorMeja = mejaSnapshot.child("nomor_meja").getValue(String::class.java)
                    val kapasitasMeja = mejaSnapshot.child("kapasitas_meja").getValue(String::class.java)

                    // Tambahkan item meja ke adapter
                    if (nomorMeja != null && kapasitasMeja != null) {
                        mejaAdapter.addItem(KMejaItem(nomorMeja, kapasitasMeja))
                    }
                }

                // Perbarui tampilan RecyclerView
                mejaAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error jika perlu
                Log.e("MilihMeja", "Gagal membaca data meja: ${databaseError.message}")
            }
        })

        btnPesan = view.findViewById(R.id.btn_lanjutpesanan)
        btnback = view.findViewById(R.id.btn_kembalimilihmenu)
        btnback.setOnClickListener {
            val backFrag = MilihMenu()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.tampilfragment, backFrag)
            transaction.commit()
        }
        btnPesan.setOnClickListener {
            // Ambil argumen yang disimpan saat memulai fragment ini
            val detailPesananId = arguments?.getString(ARG_DETAIL_PESANAN_ID) ?: ""
            val selectedMenuList =
                arguments?.getParcelableArrayList<pesananMenuItem>(ARG_SELECTED_MENU_LIST) ?: emptyList()
            // Simpan pilihan meja ke dalam database
            val selectedMejaList = mejaAdapter.getSelectedMeja()

            // Ambil nomorMeja dari salah satu item yang dipilih (misalnya, dari item pertama)
            val nomorMeja = selectedMejaList.firstOrNull()?.nomorMeja ?: ""

            savePesananToDatabase(selectedMenuList, detailPesananId, nomorMeja)

            // Memulai Intent ke Activity yang menampung fragment MilihMeja
            val intent = Intent(activity, StrukDetailPesanan::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun generateDummyMejaList(): List<KMejaItem> {
        val mejaList = mutableListOf<KMejaItem>()
        for (i in 1..20) {
            val mejaItem = KMejaItem(i.toString(), "4")
            mejaList.add(mejaItem)
        }
        return mejaList
    }

    private fun savePesananToDatabase(
        selectedMenuList: List<pesananMenuItem>,
        detailPesananId: String,
        nomorMeja: String
    ) {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedTime = dateFormat.format(currentTime)

        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val pesananReference = FirebaseDatabase.getInstance().reference
            .child("resto").child(userID!!).child("detailpesanan").child(detailPesananId)

        for (selectedMenu in selectedMenuList) {
            val menuData = HashMap<String, Any>()
            menuData["namaMenu"] = selectedMenu.namaMenu
            menuData["harga"] = selectedMenu.harga
            menuData["jumlah"] = 1 // Jumlah otomatis 1
            menuData["logs"] = formattedTime

            // Tambahan turunan nomorMeja
            menuData["nomorMeja"] = nomorMeja

            pesananReference.child(selectedMenu.namaMenu).setValue(menuData)
        }
    }

    companion object {
        private const val ARG_DETAIL_PESANAN_ID = "arg_detail_pesanan_id"
        private const val ARG_SELECTED_MENU_LIST = "arg_selected_menu_list"

        fun newInstance(detailPesananId: String, selectedMenuList: List<pesananMenuItem>): MilihMeja {
            val fragment = MilihMeja()
            val args = Bundle()
            args.putString(ARG_DETAIL_PESANAN_ID, detailPesananId)
            args.putParcelableArrayList(ARG_SELECTED_MENU_LIST, ArrayList(selectedMenuList))
            fragment.arguments = args
            return fragment
        }
    }
}
