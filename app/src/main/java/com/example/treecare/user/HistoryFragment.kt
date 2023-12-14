package com.example.treecare.user

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
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
import com.example.treecare.service.api.v1.response.RiwayatPohonsPagingResponse
import com.example.treecare.service.model.IdentitasPohonModel
import com.example.treecare.service.model.RiwayatPohonModel
import com.example.treecare.service.model.UserModel
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
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment(), PengamatanInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var btnFilter: ImageView
    private lateinit var tvNoRiwayat: TextView
    private lateinit var rvHistory: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var searchIcon: ImageView
    private lateinit var pbLoading: ProgressBar
    private lateinit var preferenceManager: PreferenceManager
    private val listRiwayat = ArrayList<RiwayatPohonModel>()

    private var sort: String = "created_at"
    private var sortType: String = "desc"
    private var page: Int = 1
    private var pageSize: Int = 5
    private var totalPage: Int = 1

    private val LOAD_MORE_THRESHOLD = 1

    private var currFilter: Int? = 0


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

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = PreferenceManager(requireContext())
        btnFilter = view.findViewById(R.id.btnFilter)
        tvNoRiwayat = view.findViewById(R.id.tvNoRiwayat)
        searchEditText = view.findViewById(R.id.searchEditText)
        searchIcon = view.findViewById(R.id.searchIcon)
        rvHistory = view.findViewById(R.id.rvHistory)
        pbLoading = view.findViewById(R.id.pbLoading)

        rvHistory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvHistory.adapter = PengamatanAdapter(requireContext(), listRiwayat, this)

        tvNoRiwayat.visibility = View.GONE
        rvHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val percentageScrolled = (lastVisibleItemPosition + 1) / totalItemCount.toDouble()

                if (percentageScrolled >= LOAD_MORE_THRESHOLD && page <= totalPage) {
                    loadNextPage()
                }
            }
        })
        searchEditText.setOnKeyListener(View.OnKeyListener{v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                this.page = 1
                currFilter = 0

                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)

                searchEditText.clearFocus()
                pbLoading.visibility = View.VISIBLE

                listRiwayat.clear()
                rvHistory.adapter?.notifyDataSetChanged()

                getAllRiwayat(searchEditText.text.toString())

                return@OnKeyListener true
            }
            false
        })
        searchIcon.setOnClickListener(View.OnClickListener { v ->

            this.page = 1
            currFilter = 0

            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            searchEditText.clearFocus()
            pbLoading.visibility = View.VISIBLE

            listRiwayat.clear()
            rvHistory.adapter?.notifyDataSetChanged()

            getAllRiwayat(searchEditText.text.toString())
        })
        btnFilter.setOnClickListener {
            showFilterDialog()
        }

        getAllRiwayat()
    }

    @SuppressLint("NotifyDataSetChanged")
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

        when (currFilter) {
            1 -> {
                btnAZ.setTypeface(resources.getFont(R.font.poppins_bold))
            }
            2 -> {
                btnZA.setTypeface(resources.getFont(R.font.poppins_bold))
            }
            3 -> {
                btnTerbaru.setTypeface(resources.getFont(R.font.poppins_bold))
            }
            4 -> {
                btnTerlama.setTypeface(resources.getFont(R.font.poppins_bold))
            }
        }

        btnAZ.setOnClickListener {
            currFilter = 1
            this.sort = "nomor_pohon"
            this.sortType = "asc"
            this.page = 1

            listRiwayat.clear()
            rvHistory.adapter?.notifyDataSetChanged()

            pbLoading.visibility = View.VISIBLE

            if (searchEditText.text.toString() != "") {
                getAllRiwayat(searchEditText.text.toString())
            } else {
                getAllRiwayat()
            }
            filterLogout.dismiss()
        }
        btnZA.setOnClickListener {
            currFilter = 2
            this.sort= "nomor_pohon"
            this.sortType = "desc"
            this.page = 1

            listRiwayat.clear()
            rvHistory.adapter?.notifyDataSetChanged()

            pbLoading.visibility = View.VISIBLE

            if (searchEditText.text.toString() != "") {
                getAllRiwayat(searchEditText.text.toString())
            } else {
                getAllRiwayat()
            }
            filterLogout.dismiss()
        }
        btnTerbaru.setOnClickListener {
            currFilter = 3
            this.sort = "created_at"
            this.sortType = "desc"
            this.page = 1

            listRiwayat.clear()
            rvHistory.adapter?.notifyDataSetChanged()

            pbLoading.visibility = View.VISIBLE

            if (searchEditText.text.toString() != "") {
                getAllRiwayat(searchEditText.text.toString())
            } else {
                getAllRiwayat()
            }
            filterLogout.dismiss()
        }
        btnTerlama.setOnClickListener {
            currFilter = 4
            this.sort = "created_at"
            this.sortType = "asc"
            this.page = 1

            listRiwayat.clear()
            rvHistory.adapter?.notifyDataSetChanged()

            pbLoading.visibility = View.VISIBLE

            if (searchEditText.text.toString() != "") {
                getAllRiwayat(searchEditText.text.toString())
            } else {
                getAllRiwayat()
            }
            filterLogout.dismiss()
        }
    }

    private fun loadNextPage() {
        if (this.page <= this.totalPage) {
            this.page++
            getAllRiwayat()
        }
    }

    private fun getAllRiwayat(keyword: String?=null) {
        tvNoRiwayat.visibility = View.GONE

        val authToken = preferenceManager.getAccessToken()
        val tokenAuthenticator = TokenAuthenticator(preferenceManager)
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .build()
        val retroHelperRiwayatPohon = RetrofitHelperV1()
            .getApiClientAuth(okHttpClient)
            .create(RiwayatPohonService::class.java)

        val callback = object : Callback<RiwayatPohonsPagingResponse> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<RiwayatPohonsPagingResponse>,
                response: Response<RiwayatPohonsPagingResponse>
            ) {

                pbLoading.visibility = View.GONE

                if (!response.isSuccessful) {
                    return
                }

                var body = response.body()
                if (body?.data == null ) {
                    return
                }
                if (body.data?.data == null || body.data?.data?.size == 0) {
                    if (listRiwayat.isEmpty()) {
                        tvNoRiwayat.visibility = View.VISIBLE
                    }

                    return
                } else {
                    tvNoRiwayat.visibility = View.GONE
                }

                totalPage = body.data?.totalPage!!

                for (riwayat in body.data?.data!!) {
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
                    identitasPohon.nomorPohon = riwayat.identitasPohon?.nomorPohon.toString()
                    identitasPohon.namaProjek = riwayat.identitasPohon?.namaProyek.toString()

                    //Initiate new data class in newRiwayat
                    newRiwayat.identitasPohon = identitasPohon
                    newRiwayat.user = user

                    listRiwayat.add(newRiwayat)
                }

                rvHistory.adapter?.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<RiwayatPohonsPagingResponse>, t: Throwable) {
                pbLoading.visibility = View.GONE
                tvNoRiwayat.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    "Gagal mendapatkan data pengamatan",
                    Toast.LENGTH_LONG
                ).show();
            }

        }

        if (keyword != null) {
            // Request with keyword
            retroHelperRiwayatPohon.getAllRiwayatByKeyword(
                keyword,
                this.sort,
                this.sortType,
                this.page,
                this.pageSize,
                authToken
            ).enqueue(callback)

        } else {
            // Request without keyword
            retroHelperRiwayatPohon.getAllRiwayat(
                this.sort,
                this.sortType,
                this.page,
                this.pageSize,
                authToken
            ).enqueue(callback)

        }
    }

    override fun onItemClick(position: Int) {
        val data = listRiwayat[position]

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