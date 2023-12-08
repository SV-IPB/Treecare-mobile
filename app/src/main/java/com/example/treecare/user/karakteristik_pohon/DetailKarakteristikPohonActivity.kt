package com.example.treecare.user.karakteristik_pohon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.treecare.R
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.RetrofitHelperV1
import com.example.treecare.service.api.v1.RiwayatPohonService
import com.example.treecare.service.api.v1.TokenAuthenticator
import com.example.treecare.service.api.v1.response.RiwayatPohonResponse
import com.example.treecare.user.RiwayatPengamatanActivity
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailKarakteristikPohonActivity : AppCompatActivity() {
    private lateinit var etKeliling: TextView
    private lateinit var etTinggi:   TextView
    private lateinit var etLebarTajuk: TextView
    private lateinit var sBentuk:    TextView
    private lateinit var etCrownRatio: TextView
    private lateinit var sSejarahPemangkasan: TextView
    private lateinit var preferenceManager : PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_karakteristik_pohon)

        val btnBack:    ImageView = findViewById(R.id.btnBack)
        etKeliling      = findViewById(R.id.etKeliling)
        etTinggi        = findViewById(R.id.etTinggi)
        etLebarTajuk    = findViewById(R.id.etLebarTajuk)
        sBentuk         = findViewById(R.id.sBentuk)
        etCrownRatio    = findViewById(R.id.etCrownRatio)
        sSejarahPemangkasan = findViewById(R.id.sSejarahPemangkasan)
        preferenceManager = PreferenceManager(this)

        btnBack.setOnClickListener {
            val intent = Intent(this, RiwayatPengamatanActivity::class.java)
            startActivity(intent)
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
            override fun onResponse(call: Call<RiwayatPohonResponse>, response: Response<RiwayatPohonResponse>) {

                if (!response.isSuccessful) {
                    Toast.makeText(this@DetailKarakteristikPohonActivity, "Gagal mengambil data",
                        Toast.LENGTH_LONG).show();
                    return
                }

                val body = response.body()

                etKeliling.setText(body?.data?.keliling.toString())
                etTinggi.setText(body?.data?.tinggi.toString())
                etLebarTajuk.setText(body?.data?.lebarTajuk.toString())
                etCrownRatio.setText(body?.data?.liveCrownRatio.toString())
                sBentuk.setText(body?.data?.bentuk)
                sSejarahPemangkasan.setText((body?.data?.sejarahPemangkasan))

            }

            override fun onFailure(call: Call<RiwayatPohonResponse>, t: Throwable) {
                Toast.makeText(this@DetailKarakteristikPohonActivity, "Gagal mengambil data",
                    Toast.LENGTH_LONG).show();
            }
        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, RiwayatPengamatanActivity::class.java)
        intent.putExtra("dataRiwayat", getIntent().getBundleExtra("dataRiwayat"))
        startActivity(intent)
        finish()
    }
}