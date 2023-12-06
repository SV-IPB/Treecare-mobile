package com.example.treecare.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treecare.R
import com.example.treecare.adapter.PengamatanAdapter
import com.example.treecare.interfaces.PengamatanInterface
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.RetrofitHelperV1
import com.example.treecare.service.api.v1.RiwayatPohonService
import com.example.treecare.service.api.v1.TokenAuthenticator
import com.example.treecare.service.api.v1.response.RiwayatPohonsPagingResponse
import com.example.treecare.service.model.IdentitasPohonModel
import com.example.treecare.service.model.RiwayatPohonModel
import com.example.treecare.service.model.UserModel
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
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), PengamatanInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var rvHome: RecyclerView
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchEditText: EditText = view.findViewById(R.id.searchEditText)
        val searchIcon: ImageView = view.findViewById(R.id.searchIcon)
        val tvPengamatanTerbaru: TextView = view.findViewById(R.id.tvPengamatanTerbaru)
        rvHome = view.findViewById(R.id.rvHome)
        preferenceManager = PreferenceManager(requireContext())

        rvHome.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvHome.adapter = PengamatanAdapter(requireContext(), listRiwayat, this)

        searchIcon.setOnClickListener {
            if (searchEditText.visibility == View.VISIBLE) {
                searchEditText.visibility = View.INVISIBLE
                tvPengamatanTerbaru.visibility = View.VISIBLE
            } else {
                searchEditText.visibility = View.VISIBLE
                tvPengamatanTerbaru.visibility = View.INVISIBLE
            }
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

        retroHelperRiwayatPohon.getAllRiwayat("created_at", "asc", 1, 5, authToken).enqueue(object : Callback<RiwayatPohonsPagingResponse> {

            override fun onResponse(
                call: Call<RiwayatPohonsPagingResponse>,
                response: Response<RiwayatPohonsPagingResponse>
            ) {

                var body = response.body()

                if (body?.data != null || body?.data?.data != null || body?.data?.data?.size == 0) {
                    return
                }

                for (riwayat in body?.data?.data!!) {
                    var newRiwayat = RiwayatPohonModel()
                    var user = UserModel()
                    var identitasPohon = IdentitasPohonModel()

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
                    identitasPohon.nomorPohon = riwayat.identitasPohon?.nomorPohon.toString()
                    identitasPohon.namaProjek = riwayat.identitasPohon?.namaProyek.toString()

                    //Initiate new data class in newRiwayat
                    newRiwayat.identitasPohon = identitasPohon
                    newRiwayat.user = user

                    listRiwayat.add(newRiwayat)
                }

                rvHome.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<RiwayatPohonsPagingResponse>, t: Throwable) {
                TODO("Not yet implemented")
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
        intent.putExtra("nomor", data.identitasPohon?.nomorPohon)
        intent.putExtra("responseCode", "200")
        intent.putExtra("idRiwayat", data.id)
        intent.putExtra("dataRiwayat", extras)
        startActivity(intent)
    }
}