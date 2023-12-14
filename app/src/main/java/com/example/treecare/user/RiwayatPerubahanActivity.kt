package com.example.treecare.user

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treecare.R
import com.example.treecare.adapter.RiwayatPerubahanAdapter
import com.example.treecare.interfaces.PerubahanInterface
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.PohonService
import com.example.treecare.service.api.v1.RetrofitHelperV1
import com.example.treecare.service.api.v1.TokenAuthenticator
import com.example.treecare.service.api.v1.response.RiwayatPerubahanResponse
import com.example.treecare.service.model.RiwayatPerubahanModel
import com.example.treecare.service.model.UserModel
import com.example.treecare.user.identitas_pohon.DetailIndentitasPohonActivity
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatPerubahanActivity : AppCompatActivity(), PerubahanInterface {

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var rvPerubahan: RecyclerView
    private lateinit var pbLoading: ProgressBar
    private lateinit var tvNoPerubahan: TextView
    private lateinit var nomor: String
    private lateinit var codeResponse: String
    private lateinit var idPohon: String
    private val listPerubahan = ArrayList<RiwayatPerubahanModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_perubahan)

        preferenceManager = PreferenceManager(this)
        rvPerubahan = findViewById(R.id.rvPerubahan)
        pbLoading = findViewById(R.id.pbLoading)
        tvNoPerubahan = findViewById(R.id.tvNoPerubahan)

        nomor = intent.getStringExtra("nomor").toString()
        codeResponse = intent.getStringExtra("responseCode").toString()
        idPohon = intent.getStringExtra("id").toString()

        val btnBack: ImageView = findViewById(R.id.btnBack)
        rvPerubahan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvPerubahan.adapter = RiwayatPerubahanAdapter(this, listPerubahan, this)

        btnBack.setOnClickListener {
//            val intent = Intent(this, DetailIndentitasPohonActivity::class.java)
//            intent.putExtra("nomor", nomor)
//            intent.putExtra("responseCode", codeResponse)
//            startActivity(intent)
            finish()
        }

        tvNoPerubahan.visibility = View.GONE
        getValue()
    }

    private fun getValue() {
        val token = preferenceManager.getAccessToken()
        val tokenAuthenticator = TokenAuthenticator(preferenceManager)
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .build()
        val retro = RetrofitHelperV1()
            .getApiClientAuth(okHttpClient)
            .create(PohonService::class.java)

        retro.getAudit(idPohon, token).enqueue(object : Callback<RiwayatPerubahanResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<RiwayatPerubahanResponse>,
                response: Response<RiwayatPerubahanResponse>
            ) {

                pbLoading.visibility = View.GONE

                if (!response.isSuccessful) {
                    tvNoPerubahan.visibility = View.VISIBLE
                    Toast.makeText(
                        this@RiwayatPerubahanActivity,
                        "Gagal mendapatkan data",
                        Toast.LENGTH_SHORT
                    ).show()

                    return
                }

                val body = response.body()

                if (body?.data == null) {
                    tvNoPerubahan.visibility = View.VISIBLE
                    return
                }

                for (perubahan in body.data!!) {
                    val newPerubahan = RiwayatPerubahanModel()

                    newPerubahan.id = perubahan.id
                    newPerubahan.fieldName = perubahan.fieldName
                    newPerubahan.tanggal = perubahan.tanggal
                    newPerubahan.jam = perubahan.jam

                    val user = UserModel()
                    user.nama = perubahan.user?.name

                    newPerubahan.user = user

                    listPerubahan.add(newPerubahan)
                }

                rvPerubahan.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<RiwayatPerubahanResponse>, t: Throwable) {
                tvNoPerubahan.visibility = View.VISIBLE
                Toast.makeText(
                    this@RiwayatPerubahanActivity,
                    "Gagal mendapatkan data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
//        val intent = Intent(this, DetailIndentitasPohonActivity::class.java)
//        intent.putExtra("nomor", nomor)
//        intent.putExtra("responseCode", codeResponse)
//        startActivity(intent)
        finish()
    }

    override fun onItemClick(position: Int) {
        return
    }
}