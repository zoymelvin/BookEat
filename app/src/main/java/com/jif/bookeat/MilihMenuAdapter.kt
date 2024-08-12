package com.jif.bookeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat

class MilihMenuAdapter(private val menuList: MutableList<pesananMenuItem>) : RecyclerView.Adapter<MilihMenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFotoMenu: ImageView = itemView.findViewById(R.id.iv_tampilfoto)
        val tvNamaMenu: TextView = itemView.findViewById(R.id.tv_tampilnamamenu)
        val tvHargaMenu: TextView = itemView.findViewById(R.id.tv_tampilhargamenu)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox_menu)
        // Inisialisasi properti isSelected ke false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_milih_menu, parent, false)
        return MenuViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val currentItem = menuList[position]

        // Set data ke tampilan
        Glide.with(holder.itemView.context)
            .load(currentItem.fotoMenu)
            .into(holder.ivFotoMenu)

        holder.tvNamaMenu.text = currentItem.namaMenu
        holder.tvHargaMenu.text = currentItem.harga.toString()
        // Sesuaikan dengan properti lainnya
        holder.checkBox.isChecked = currentItem.isSelected

        // Tambahkan listener untuk CheckBox
        holder.checkBox.setOnClickListener {
            currentItem.isSelected = holder.checkBox.isChecked
        }
    }
    /// Fungsi untuk membersihkan data menu
    fun clearData() {
        menuList.clear()
        notifyDataSetChanged()
    }

    // Fungsi untuk menambahkan data menu
    fun addMenu(MenuItem: pesananMenuItem) {
        menuList.add(MenuItem)
        notifyDataSetChanged()
    }
    fun getSelectedMenu(): List<pesananMenuItem> {
        return menuList.filter { it.isSelected }
    }
}