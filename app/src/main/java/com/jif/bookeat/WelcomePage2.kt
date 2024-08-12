package com.jif.bookeat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.FragmentTransaction

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [WelcomePage2.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomePage2 : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome_page2, container, false)

        val btnBackfrag = view.findViewById<ImageButton>(R.id.ib_back_welcomepage2)
        btnBackfrag.setOnClickListener {
            val backFrag = WelcomePage1()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.tampilwelcomepage,backFrag)
            transaction.commit()
        }
        val btnNextfrag = view.findViewById<ImageButton>(R.id.ib_next_welcomepage2)
        btnNextfrag.setOnClickListener {
            val nextFrag = WelcomePage3()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.tampilwelcomepage,nextFrag)
            transaction.commit()
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WelcomePage2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WelcomePage2().apply {
                arguments = Bundle().apply {
                }
            }
    }
}