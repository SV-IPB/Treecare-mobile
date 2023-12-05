package com.example.treecare.user.pengamatan_visual

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.treecare.R
import com.example.treecare.user.identitas_pohon.DetailIndentitasPohonActivity
import com.example.treecare.user.karakteristik_pohon.TambahKarakteristikPohonActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdaIdentitasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdaIdentitasFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var nomorPohon: String? = null
    private var codeResponse: String? = null

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
        val view = inflater.inflate(R.layout.fragment_ada_identitas, container, false)
        nomorPohon = requireArguments().getString("nomor")
        codeResponse = requireArguments().getString("responseCode")
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdaIdentitasFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdaIdentitasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clIdentitas: ConstraintLayout = view.findViewById(R.id.clIdentitas)
        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        val tvNoRiwayat: TextView = view.findViewById(R.id.tvNoRiwayat)

        tvNoRiwayat.text = "nomor $nomorPohon code $codeResponse"

        clIdentitas.setOnClickListener {
            val intent = Intent(context, DetailIndentitasPohonActivity::class.java)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivity(intent)
            activity?.finish()
        }

        fab.setOnClickListener {
            val intent = Intent(context, TambahKarakteristikPohonActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}