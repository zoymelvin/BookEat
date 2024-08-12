//package com.jif.bookeat
//
//import android.os.Bundle
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import com.jif.bookeat.databinding.FragmentItemListDialogListDialogItemBinding
//import com.jif.bookeat.databinding.FragmentItemListDialogListDialogBinding
//
//// TODO: Customize parameter argument names
//const val ARG_ITEM_COUNT = "item_count"
//
///**
// *
// * A fragment that shows a list of items as a modal bottom sheet.
// *
// * You can show this modal bottom sheet from your activity like this:
// * <pre>
// *    ItemListDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
// * </pre>
// */
//class ItemListDialogFragment : BottomSheetDialogFragment() {
//
//    private var _binding: FragmentItemListDialogListDialogBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        _binding = FragmentItemListDialogListDialogBinding.inflate(inflater, container, false)
//        return binding.root
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
//        recyclerView.layoutManager = LinearLayoutManager(context)
//
//        // Tambahkan adapter dengan passing keranjangItems
//        recyclerView.adapter = ItemAdapter(getDataProses(), KeranjangItem)
//    }
//
//
//    private fun getDataProses(): ArrayList<DaftarMenuItem> {
//        return arrayListOf(
////            DaftarMenuItem(
////                "Menu 1",
////                R.drawable.icondish2,
////                harga = 13500,
////                idmenu = "RD-010"
////            ),
////
////            DaftarMenuItem(
////                "Menu 1",
////                R.drawable.icondish2,
////                harga = 8500,
////                idmenu = "RD-011"
////            )
////            // Tambahkan data lainnya sesuai kebutuhan
//        )
//    }
//
//    private inner class ViewHolder internal constructor(binding: FragmentItemListDialogListDialogItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        internal val tvNamaMenu: TextView = binding.tvCartNamamenu
//        internal val tvHargaMenu: TextView = binding.tvCartHargamenu
//        internal val tvIdMenu: TextView = binding.tvCartIdmenu
//    }
//
//    private inner class ItemAdapter internal constructor(
//        private val daftarMenuItems: ArrayList<DaftarMenuItem>,
//        private val keranjangItems: MutableList<KeranjangItem>
//    ) : RecyclerView.Adapter<ViewHolder>() {
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//            return ViewHolder(
//                FragmentItemListDialogListDialogItemBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent, false
//                )
//            )
//        }
//
//        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            val menuItem = daftarMenuItems[position]
//
//            holder.tvNamaMenu.text = menuItem.namaMenu
//            holder.tvHargaMenu.text = "Rp ${menuItem.harga}"
//
//            // Tambahkan listener untuk checkbox
//            holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
//                // Update status isSelected pada item dalam keranjangItems
//                val keranjangItem = KeranjangItem(menuItem, 1)
//                if (isChecked) {
//                    keranjangItems.add(keranjangItem)
//                } else {
//                    keranjangItems.remove(keranjangItem)
//                }
//            }
//        }
//
//        override fun getItemCount(): Int {
//            return daftarMenuItems.size
//        }
//    }
//
//    companion object {
//
//        // TODO: Customize parameters
//        fun newInstance(itemCount: Int): ItemListDialogFragment =
//            ItemListDialogFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(ARG_ITEM_COUNT, itemCount)
//                }
//            }
//
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}