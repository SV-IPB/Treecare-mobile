package com.example.treecare.user.karakteristik_pohon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.treecare.R
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.RetrofitHelperV1
import com.example.treecare.service.api.v1.RiwayatPohonService
import com.example.treecare.service.api.v1.TokenAuthenticator
import com.example.treecare.service.api.v1.UserService
import com.example.treecare.user.RiwayatPengamatanActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity
import okhttp3.OkHttpClient

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
    }

    fun getValue() {
        val authToken = preferenceManager.getAccessToken()
        val tokenAuthenticator = TokenAuthenticator(preferenceManager)
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .build()
        val retro = RetrofitHelperV1()
            .getApiClientAuth(okHttpClient)
            .create(RiwayatPohonService::class.java)


    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, RiwayatPengamatanActivity::class.java)
        startActivity(intent)
        finish()
    }
}