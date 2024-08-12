package com.jif.bookeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

//class HalProsesAdapter(private val tampilMenu: List<DaftarMenuItem>) : RecyclerView.Adapter<HalProsesAdapter.MenuViewHolder>() {

//    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvDatetime: TextView = itemView.findViewById(R.id.tv_pro_datetime_pesanan)
//        val jmlhterjual: TextView = itemView.findViewById(R.id.tv_pro_jmlhterjual)
//        val idmenu: TextView = itemView.findViewById(R.id.tv_pro_idmenu)
//        val hargatotal: TextView = itemView.findViewById(R.id.tv_pro_hargatotal)
//        val ivFotoMenu: ImageView = itemView.findViewById(R.id.iv_pro_fotomenu)
//        val tvNamaMenu: TextView = itemView.findViewById(R.id.tv_pro_namamenu)
//        val tvHargaMenu: TextView = itemView.findViewById(R.id.tv_pro_hargamenu)
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HalProsesAdapter.MenuViewHolder {
//        val itemView = LayoutInflater.from(parent.context)
//            .inflate(R.layout.list_item_proses, parent, false)
//        return MenuViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: HalProsesAdapter.MenuViewHolder, position: Int) {
//        val currentItem = tampilMenu[position]
//        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm") // Sesuaikan dengan format yang diinginkan
//        val formattedDate = dateFormat.format(currentItem._datetime)
//        // Set data ke tampilan
//        // Set data ke view holder
//        holder.tvNamaMenu.text = currentItem.namaMenu
//        holder.ivFotoMenu.setImageResource(currentItem.fotoMenu)
//        holder.tvHargaMenu.text = "Rp${currentItem.harga}"
//        holder.tvDatetime.text = formattedDate
//        holder.jmlhterjual.text = "x${currentItem._jmlhterjual}"
//        holder.idmenu.text = currentItem._idmenu
//        // Hitung harga total
//        val hargaTotal = currentItem.harga * currentItem._jmlhterjual
//        holder.hargatotal.text = "Rp${hargaTotal}"
//
//
//
//        // Lanjutkan dengan mengisi data ke TextView lainnya
//
//        // Contoh set gambar menggunakan Glide:
//        // Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.ivFotoMenu)
//    }
//
//    override fun getItemCount(): Int {
//        return tampilMenu.size
//    }

//}