package com.jif.bookeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DaftarMenuAdapter(
    private val daftarMenu: MutableList<DaftarMenuItem>,
    private val onClickEdit: (DaftarMenuItem) -> Unit,
    private val onClickDelete: (DaftarMenuItem) -> Unit
) : RecyclerView.Adapter<DaftarMenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFotoMenu: ImageView = itemView.findViewById(R.id.iv_fotomenu)
        val tvNamaMenu: TextView = itemView.findViewById(R.id.tv_namamenu)
        val tvHargaMenu: TextView = itemView.findViewById(R.id.tv_hargamenu)
        val editButton: ImageView = itemView.findViewById(R.id.iv_editmenu)
        val deleteButton: ImageView = itemView.findViewById(R.id.iv_deletemenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_menu, parent, false)
        return MenuViewHolder(itemView)
    }

    override fun getItemCount(): Int = daftarMenu.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val currentItem = daftarMenu[position]

        // Set data ke tampilan menggunakan Glide untuk pengelolaan gambar
        Glide.with(holder.itemView.context)
            .load(currentItem.fotoMenu)
            .into(holder.ivFotoMenu)

        holder.tvNamaMenu.text = currentItem.namaMenu
        holder.tvHargaMenu.text = currentItem.harga

        // Terapkan listener pada tombol edit dan delete
        holder.editButton.setOnClickListener { onClickEdit(currentItem) }
        holder.deleteButton.setOnClickListener { onClickDelete(currentItem) }
    }

    /// Fungsi untuk membersihkan semua data di daftar menu
    fun clearData() {
        daftarMenu.clear()
        notifyDataSetChanged()
    }

    // Fungsi untuk menambahkan semua data menu ke daftar
    fun addAll(menuItems: List<DaftarMenuItem>) {
        daftarMenu.addAll(menuItems)
        notifyDataSetChanged()
    }
}
