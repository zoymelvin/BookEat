package com.jif.bookeat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

class HalProses : Fragment() {

//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: HalProsesAdapter
//
//    companion object {
//        fun newInstance() = HalProses()
//    }
//
//    private lateinit var viewModel: HalProsesViewModel
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_hal_proses, container, false)
//
//        // Inisialisasi RecyclerView
//        recyclerView = view.findViewById(R.id.rv_proses)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//        // Inisialisasi Adapter dan terapkan ke RecyclerView
//        adapter = HalProsesAdapter(getDataProses()) // Gantilah dengan fungsi yang memberikan data proses
//        recyclerView.adapter = adapter
//
//        return view
//    }
//
//    // Fungsi untuk mendapatkan data proses
//    private fun getDataProses(): List<DaftarMenuItem> {
//        // Implementasikan logika untuk mendapatkan data proses
//        // Misalnya, ambil data dari sumber daya atau database
//        // Contoh sederhana:
//        return listOf(
//            DaftarMenuItem(
//                "Menu 3",
//                R.drawable.icondish2,
//                harga = 20000, _datetime = SimpleDateFormat("dd-MM-yyyy HH:mm").parse("22-05-2023 19:45")!!,
//                _jmlhterjual = 2,
//                _idmenu = "RD-003",
//                _hargatotal = 15000 * 2
//
//                //...
//            ),
//
//            DaftarMenuItem(
//                "Menu 4",
//                R.drawable.icondish2,
//                3,
//                8000,
//                4.0,
//                40000,
//                SimpleDateFormat("dd-MM-yyyy HH:mm").parse("16-11-2023 20:30")!!,
//                1,
//                _idmenu = "RD-004",
//                _hargatotal = 30000 * 1
//            ),
//
//        )
//
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(HalProsesViewModel::class.java)
//        // TODO: Use the ViewModel
//    }
}