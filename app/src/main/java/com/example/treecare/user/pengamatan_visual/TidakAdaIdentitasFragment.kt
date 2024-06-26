package com.example.treecare.user.pengamatan_visual

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.treecare.R
import com.example.treecare.user.identitas_pohon.TambahIdentitasPohonActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var nomorPohon: String? = null
private var codeResponse: String? = null
private var idPohon: String? = null

/**
 * A simple [Fragment] subclass.
 * Use the [TidakAdaIdentitasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TidakAdaIdentitasFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_tidak_ada_identitas, container, false)
        nomorPohon = requireArguments().getString("nomor")
        codeResponse = requireArguments().getString("responseCode")
        idPohon = requireArguments().getString("idPohon")
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TidakAdaIdentitasFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TidakAdaIdentitasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnTambah: AppCompatButton = view.findViewById(R.id.btnTambah)
        val tvKet: TextView = view.findViewById(R.id.tvKet)


        btnTambah.setOnClickListener {
            val intent = Intent(context, TambahIdentitasPohonActivity::class.java)
            intent.putExtra("nomor", nomorPohon)
            intent.putExtra("responseCode", codeResponse)
            startActivity(intent)
            activity?.finish()
        }
    }
}