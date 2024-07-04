package com.example.treecare.user.pengamatan_visual

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treecare.R
import com.example.treecare.adapter.PengamatanAdapter
import com.example.treecare.interfaces.PengamatanInterface
import com.example.treecare.service.ImagePreferenceManager
import com.example.treecare.service.KerusakanPreferenceManager
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.RetrofitHelperV1
import com.example.treecare.service.api.v1.RiwayatPohonService
import com.example.treecare.service.api.v1.TokenAuthenticator
import com.example.treecare.service.api.v1.response.RiwayatPohonsResponse
import com.example.treecare.service.model.IdentitasPohonModel
import com.example.treecare.service.model.RiwayatPohonModel
import com.example.treecare.service.model.UserModel
import com.example.treecare.user.RiwayatPengamatanActivity
import com.example.treecare.user.identitas_pohon.DetailIndentitasPohonActivity
import com.example.treecare.user.karakteristik_pohon.TambahKarakteristikPohonActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
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
    private lateinit var tvNoRiwayat: TextView
    private lateinit var pbLoading: ProgressBar
    private lateinit var preferenceManager: PreferenceManager
    private val listRiwayat = ArrayList<RiwayatPohonModel>()

    private lateinit var kerusakanPreferenceManager: KerusakanPreferenceManager
    private lateinit var imagePreferenceManager: ImagePreferenceManager
    private lateinit var counterPreferences: SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferences2: SharedPreferences
    private lateinit var sharedPreferences3: SharedPreferences
    private lateinit var sharedPreferences4: SharedPreferences
    private lateinit var sharedPreferences5: SharedPreferences

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
        tvNoRiwayat = view.findViewById(R.id.tvNoRiwayat)
        pbLoading   = view.findViewById(R.id.pbLoading)

        kerusakanPreferenceManager = KerusakanPreferenceManager(requireContext())
        imagePreferenceManager = ImagePreferenceManager(requireContext())
        sharedPreferences = requireActivity().getSharedPreferences("KarakteristikPohon", Context.MODE_PRIVATE)
        sharedPreferences2 = requireActivity().getSharedPreferences("KesehatanPohon", Context.MODE_PRIVATE)
        sharedPreferences3 = requireActivity().getSharedPreferences("KerusakanPohon", Context.MODE_PRIVATE)
        sharedPreferences4 = requireActivity().getSharedPreferences("KondisiTapak", Context.MODE_PRIVATE)
        sharedPreferences5 = requireActivity().getSharedPreferences("Target", Context.MODE_PRIVATE)

        preferenceManager = PreferenceManager(requireContext())
        counterPreferences = requireActivity().getSharedPreferences("kerusakanCounter", Context.MODE_PRIVATE)
        addCounter()
        rvPengamatan = view.findViewById(R.id.rvPengamatan)

        rvPengamatan.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false)
        rvPengamatan.adapter = PengamatanAdapter(requireContext(), listRiwayat, this)
        tvNoRiwayat.visibility = View.GONE

        // Request get data riwayat
        getAllRiwayat()

        clIdentitas.setOnClickListener {
            val intent = Intent(context, DetailIndentitasPohonActivity::class.java)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            intent.putExtra("idPohon", idPohon)
            startActivity(intent)
            activity?.finish()
        }

        fab.setOnClickListener {
            clearSharedPreferences()
            val intent = Intent(context, TambahKarakteristikPohonActivity::class.java)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            intent.putExtra("idPohon",idPohon)
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
        val retroHelperRiwayatPohonService = RetrofitHelperV1()
            .getApiClientAuth(okHttpClient)
            .create(RiwayatPohonService::class.java)

        retroHelperRiwayatPohonService.getAllRiwayatPohonById(idPohon, authToken).enqueue(object : Callback<RiwayatPohonsResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<RiwayatPohonsResponse>, response: Response<RiwayatPohonsResponse>) {
                pbLoading.visibility = View.GONE

                if (!response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Gagal mengambil data",
                        Toast.LENGTH_LONG
                    ).show();

                    return
                }

                var body = response.body()

                if (body?.data == null || body.data?.size == 0 ) {
                    return
                }

                for (riwayat in body.data!!) {
                    var newRiwayat = RiwayatPohonModel()
                    var user = UserModel()
                    var identitasPohon = IdentitasPohonModel()

                    newRiwayat.id = riwayat.id
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

                    user.nama = riwayat.user?.name

                    identitasPohon.id = riwayat.identitasPohon?.id
                    identitasPohon.gambar = riwayat.identitasPohon?.gambar.toString()
                    identitasPohon.nomorPohon = riwayat.identitasPohon?.nomorPohon
                    identitasPohon.namaProjek = riwayat.identitasPohon?.namaProyek.toString()

                    //Initiate new data class in newRiwayat
                    newRiwayat.identitasPohon = identitasPohon
                    newRiwayat.user = user

                    listRiwayat.add(newRiwayat)
                }

                rvPengamatan.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<RiwayatPohonsResponse>, t: Throwable) {
                pbLoading.visibility = View.GONE
                tvNoRiwayat.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    "Gagal mengambil data",
                    Toast.LENGTH_LONG
                ).show();
            }
        })
    }

    override fun onItemClick(position: Int) {

        val data = listRiwayat.get(position)

        val extras = Bundle()
        extras.putString("tanggal", data.tanggal)
        extras.putString("jam", data.jam)
        extras.putString("petugas", data.user?.nama)

        val intent = Intent(requireContext(), RiwayatPengamatanActivity::class.java)
        intent.putExtra("nomor",nomorPohon)
        intent.putExtra("responseCode",codeResponse)
        intent.putExtra("idRiwayat", data.id)
        intent.putExtra("dataRiwayat", extras)
        startActivity(intent)
    }

    private fun addCounter(){
        val editor = counterPreferences.edit()
        editor.putInt("counter",0)
        editor.apply()
        Log.e("Debug counter: ", counterPreferences.getInt("counter",-1).toString())
    }

    private fun clearSharedPreferences() {
        val editor = sharedPreferences.edit()
        val editor2 = sharedPreferences2.edit()
        val editor3= sharedPreferences3.edit()
        val editor4 = sharedPreferences4.edit()
        val editor5 = sharedPreferences5.edit()
        editor.clear().apply()
        editor2.clear().apply()
        editor3.clear().apply()
        editor4.clear().apply()
        editor5.clear().apply()
        kerusakanPreferenceManager.removeData()
        imagePreferenceManager.removeData()
    }
}