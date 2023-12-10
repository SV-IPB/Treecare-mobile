package com.example.treecare.user.kerusakan_pohon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treecare.R
import com.example.treecare.adapter.KerusakanAdapter
import com.example.treecare.interfaces.KerusakanInterface
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.RetrofitHelperV1
import com.example.treecare.service.api.v1.RiwayatPohonService
import com.example.treecare.service.api.v1.TokenAuthenticator
import com.example.treecare.service.api.v1.response.RiwayatPohonResponse
import com.example.treecare.service.model.RiwayatKerusakanPohonModel
import com.example.treecare.user.RiwayatPengamatanActivity
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListKerusakanPohonActivity : AppCompatActivity(), KerusakanInterface {

    private lateinit var rvKerusakanPohon: RecyclerView
    private lateinit var pbLoading: ProgressBar
    private lateinit var preferenceManager : PreferenceManager
    private var listKerusakan = ArrayList<RiwayatKerusakanPohonModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_kerusakan_pohon)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        rvKerusakanPohon = findViewById(R.id.rvKerusakanPohon)
        pbLoading       = findViewById(R.id.pbLoading)
        preferenceManager = PreferenceManager(this)

        rvKerusakanPohon.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        rvKerusakanPohon.adapter = KerusakanAdapter(
            this@ListKerusakanPohonActivity,
            listKerusakan,
            this
        )

        btnBack.setOnClickListener {
//            val intent = Intent(this, RiwayatPengamatanActivity::class.java)
//            startActivity(intent)
            finish()
        }

        getValue()
    }

    fun getValue() {
        val idRiwayat = intent.getStringExtra("idRiwayat")
        val authToken = preferenceManager.getAccessToken()
        val tokenAuthenticator = TokenAuthenticator(preferenceManager)
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .build()
        val retro = RetrofitHelperV1()
            .getApiClientAuth(okHttpClient)
            .create(RiwayatPohonService::class.java)

        retro.getRiwayatDetailPohonById(idRiwayat, authToken).enqueue(object : Callback<RiwayatPohonResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<RiwayatPohonResponse>, response: Response<RiwayatPohonResponse>) {

                pbLoading.visibility = View.GONE

                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@ListKerusakanPohonActivity,
                        "Gagal mendapatkan data kerusakan",
                        Toast.LENGTH_LONG
                    ).show();
                    return
                }


                val body = response.body()
                for (kerusakan in body?.data?.riwayatKerusakanPohon!!) {
                    val newKerusakan = RiwayatKerusakanPohonModel()
                    newKerusakan.id = kerusakan.id

                    listKerusakan.add(newKerusakan)
                }

                rvKerusakanPohon.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<RiwayatPohonResponse>, t: Throwable) {
                pbLoading.visibility = View.GONE
                Toast.makeText(
                    this@ListKerusakanPohonActivity,
                    "Gagal mendapatkan data kerusakan",
                    Toast.LENGTH_LONG
                ).show();
            }
        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
//        val intent = Intent(this, RiwayatPengamatanActivity::class.java)
//        intent.putExtra("dataRiwayat", getIntent().getBundleExtra("dataRiwayat"))
//        startActivity(intent)
        finish()
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, DetailKerusakanPohonActivity::class.java)
        intent.putExtra("idKerusakan", listKerusakan[position].id)
        intent.putExtra("nomorKerusakan", position+1)
        startActivity(intent)
    }
}