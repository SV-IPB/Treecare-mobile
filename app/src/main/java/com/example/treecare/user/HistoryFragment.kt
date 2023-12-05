package com.example.treecare.user

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.treecare.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var btnFilter: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnFilter = view.findViewById(R.id.btnFilter)

        btnFilter.setOnClickListener {
            showFilterDialog()
        }
    }

    fun showFilterDialog() {
        val filterLogout = Dialog(requireActivity(), R.style.MaterialDialogSheet)
        filterLogout.setContentView(R.layout.dialog_filter)
        filterLogout.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        filterLogout.window!!.setGravity(Gravity.BOTTOM)
        filterLogout.show()

        val btnAZ:TextView = filterLogout.findViewById(R.id.btnAZ)
        val btnZA:TextView = filterLogout.findViewById(R.id.btnZA)
        val btnTerbaru:TextView = filterLogout.findViewById(R.id.btnTerbaru)
        val btnTerlama:TextView = filterLogout.findViewById(R.id.btnTerlama)

        btnAZ.setOnClickListener {

        }
        btnZA.setOnClickListener {

        }
        btnTerbaru.setOnClickListener {

        }
        btnTerlama.setOnClickListener {

        }
    }
}