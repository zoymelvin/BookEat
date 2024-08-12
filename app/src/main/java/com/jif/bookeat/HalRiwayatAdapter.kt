package com.jif.bookeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

class HalRiwayatAdapter(private var tampilMenu: MutableList<RiwayatItem>) : RecyclerView.Adapter<HalRiwayatAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDatetime: TextView = itemView.findViewById(R.id.tv_riw_datetime_pesanan)
        val teksJumlah: TextView = itemView.findViewById(R.id.textJumlah)
        val namaItem: TextView = itemView.findViewById(R.id.textNamaItem)
        val noMeja: TextView = itemView.findViewById(R.id.noMeja)
        val tvHargaMenu: TextView = itemView.findViewById(R.id.textHarga)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HalRiwayatAdapter.MenuViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_riwayat, parent, false)
        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HalRiwayatAdapter.MenuViewHolder, position: Int) {
        val riwayatItem = tampilMenu[position]

        // Isi data ke dalam TextView di sini
        holder.tvDatetime.text = riwayatItem.waktu
        holder.teksJumlah.text = "x${riwayatItem.jumlah}"
        holder.namaItem.text = riwayatItem.namaItem
        holder.noMeja.text = "Meja: ${riwayatItem.nomorMeja}"
        holder.tvHargaMenu.text = "Rp${riwayatItem.harga}"

    }

    override fun getItemCount(): Int {
        return tampilMenu.size
    }
    fun setData(newRiwayatItems: MutableList<RiwayatItem>) {
        tampilMenu.clear()
        tampilMenu.addAll(newRiwayatItems)
        notifyDataSetChanged()
    }

}