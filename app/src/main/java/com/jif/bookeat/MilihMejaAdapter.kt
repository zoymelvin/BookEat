package com.jif.bookeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MilihMejaAdapter(private var mejaList: MutableList<KMejaItem>) : RecyclerView.Adapter<MilihMejaAdapter.MejaViewHolder>() {

    class MejaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNomorMeja: TextView = itemView.findViewById(R.id.tv_nomormeja)
        val tvJumlahKursi: TextView = itemView.findViewById(R.id.tv_kapasitasmeja)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox_meja)
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
        holder.tvJumlahKursi.text = currentItem.kapasitasMeja
        // Sesuaikan dengan properti lainnya
        holder.checkBox.isChecked = currentItem.isSelected

        // Tambahkan listener untuk CheckBox
        holder.checkBox.setOnClickListener {
            currentItem.isSelected = holder.checkBox.isChecked
        }
    }

    // Tambahkan fungsi untuk membersihkan data meja
    fun clearData() {
        mejaList.clear()
        notifyDataSetChanged()
    }

    // Tambahkan fungsi untuk menambahkan item meja
    fun addItem(mejaItem: KMejaItem) {
        mejaList.add(mejaItem)
        notifyDataSetChanged()
    }
    fun getSelectedMeja(): List<KMejaItem> {
        return mejaList.filter { it.isSelected }
    }
}