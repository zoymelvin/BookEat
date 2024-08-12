package com.jif.bookeat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StrukDetailAdapter(private val itemList: MutableList<StrukItem>) :
    RecyclerView.Adapter<StrukDetailAdapter.StrukViewHolder>() {

    class StrukViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textJumlah: TextView = itemView.findViewById(R.id.textJumlah)
        val textNamaItem: TextView = itemView.findViewById(R.id.textNamaItem)
        val textHarga: TextView = itemView.findViewById(R.id.textHarga)
        val textMeja: TextView = itemView.findViewById(R.id.noMeja)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StrukViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return StrukViewHolder(view)
    }

    override fun onBindViewHolder(holder: StrukViewHolder, position: Int) {
        val item = itemList[position]

        holder.textJumlah.text = item.jumlah.toString()
        holder.textNamaItem.text = item.namaItem
        holder.textHarga.text = "Rp ${item.harga}"
        holder.textMeja.text = item.nomorMeja
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun clearData() {
        itemList.clear()
        notifyDataSetChanged()
    }

    // Fungsi untuk menambahkan data menu
    fun addMenu(strukItem: StrukItem) {
        itemList.add(strukItem)
        notifyDataSetChanged()
    }
}