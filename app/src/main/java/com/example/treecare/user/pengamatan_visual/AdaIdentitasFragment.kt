package com.example.treecare.user.pengamatan_visual

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treecare.R
import com.example.treecare.adapter.PengamatanAdapter
import com.example.treecare.interfaces.PengamatanInterface
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.RetrofitHelperV1
import com.example.treecare.service.api.v1.RiwayatPohonService
import com.example.treecare.service.api.v1.TokenAuthenticator
import com.example.treecare.service.api.v1.response.RiwayatPohonsResponse
import com.example.treecare.service.model.RiwayatPohonModel
import com.example.treecare.user.RiwayatPengamatanActivity
import com.example.treecare.user.identitas_pohon.DetailIndentitasPohonActivity
import com.example.treecare.user.karakteristik_pohon.TambahKarakteristikPohonActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdaIdentitasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdaIdentitasFragment : Fragment(), PengamatanInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var nomorPohon: String = ""
    private var codeResponse: String? = null
    private var idPohon: String? = null
    private lateinit var rvPengamatan: RecyclerView
    private lateinit var preferenceManager: PreferenceManager
    private val listRiwayat = ArrayList<RiwayatPohonModel>()

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
        nomorPohon = requireArguments().getString("nomor").toString()
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

        preferenceManager = PreferenceManager(requireContext())
        tvNoRiwayat.text = "nomor $nomorPohon code $codeResponse"
        rvPengamatan = view.findViewById(R.id.rvPengamatan)

        rvPengamatan.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false)
        rvPengamatan.adapter = PengamatanAdapter(requireContext(), listRiwayat, this, nomorPohon)

        // Request get data riwayat
        getAllRiwayat()

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

    fun getAllRiwayat() {
        val authToken = preferenceManager.getAccessToken()
        val tokenAuthenticator = TokenAuthenticator(preferenceManager)
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .build()
        val retroHelperRiwayatPohon = RetrofitHelperV1()
            .getApiClientAuth(okHttpClient)
            .create(RiwayatPohonService::class.java)

        retroHelperRiwayatPohon.getAllRiwayatPohonById(idPohon, authToken).enqueue(object : Callback<RiwayatPohonsResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<RiwayatPohonsResponse>, response: Response<RiwayatPohonsResponse>) {

                var body = response.body()

                for (riwayat in body?.data!!) {
                    var newRiwayat = RiwayatPohonModel()

                    newRiwayat.keliling = riwayat.keliling
                    newRiwayat.tinggi = riwayat.tinggi
                    newRiwayat.lebarTajuk = riwayat.lebarTajuk
                    newRiwayat.bentuk = riwayat.bentuk
                    newRiwayat.liveCrownRatio = riwayat.liveCrownRatio
                    newRiwayat.sejarahPemangkasan = riwayat.sejarahPemangkasan
                    newRiwayat.warnaDaun = riwayat.warnaDaun
                    newRiwayat.epicormic = riwayat.epicormic
                    newRiwayat.kerapatanDaun = riwayat.kerapatanDaun
                    newRiwayat.ukuranDaun = riwayat.ukuranDaun
                    newRiwayat.woundWood = riwayat.woundWood
                    newRiwayat.twigDieback = riwayat.twigDieback
                    newRiwayat.vigor = riwayat.vigor
                    newRiwayat.hama = riwayat.hama
                    newRiwayat.karakteristikTapak = riwayat.karakteristikTapak
                    newRiwayat.gangguan = riwayat.gangguan
                    newRiwayat.masalahTanah = riwayat.masalahTanah
                    newRiwayat.gangguanLain = riwayat.gangguanLain
                    newRiwayat.pemanfaatanSekitar = riwayat.pemanfaatanSekitar
                    newRiwayat.dapatDipindahkan = riwayat.dapatDipindahkan
                    newRiwayat.dapatDibatasi = riwayat.dapatDibatasi
                    newRiwayat.hunian = riwayat.hunian
                    newRiwayat.jam = riwayat.jam
                    newRiwayat.tanggal = riwayat.tanggal

                    newRiwayat.user?.nama = riwayat.user?.name

                    newRiwayat.identitasPohon?.id = riwayat.identitasPohon?.id
                    newRiwayat.identitasPohon?.gambar = riwayat.identitasPohon?.gambar
                    newRiwayat.identitasPohon?.nomorPohon = riwayat.identitasPohon?.nomorPohon

                    listRiwayat.add(newRiwayat)
                }

                rvPengamatan.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<RiwayatPohonsResponse>, t: Throwable) {
                Log.e("Failure Request ", "Failed to request getAllRiwayat ")
            }
        })
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(requireContext(), RiwayatPengamatanActivity::class.java)
        intent.putExtra("nomor",nomorPohon)
        intent.putExtra("responseCode",codeResponse)
        startActivity(intent)
    }
}