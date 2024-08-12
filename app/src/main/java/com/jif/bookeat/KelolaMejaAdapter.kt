package com.jif.bookeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KelolaMejaAdapter(private val mejaList: MutableList<KMejaItem>) : RecyclerView.Adapter<KelolaMejaAdapter.MejaViewHolder>() {

    class MejaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNomorMeja: TextView = itemView.findViewById(R.id.tv_nomormeja)
        val tvKapasitasMeja: TextView = itemView.findViewById(R.id.tv_kapasitasmeja)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MejaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_meja, parent, false)
        return MejaViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mejaList.size
    }

    override fun onBindViewHolder(holder: MejaViewHolder, position: Int) {
        val currentItem = mejaList[position]

        holder.tvNomorMeja.text = currentItem.nomorMeja
        holder.tvKapasitasMeja.text = currentItem.kapasitasMeja
    }

    // Fungsi untuk membersihkan data meja
    fun clearData() {
        mejaList.clear()
        notifyDataSetChanged()
    }

    // Fungsi untuk menambahkan data meja
    fun addItem(mejaItem: KMejaItem) {
        mejaList.add(mejaItem)
        notifyDataSetChanged()
    }
}
