package com.example.treecare.user.kesehatan_pohon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioButton
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

class DetailKesehatanPohonActivity : AppCompatActivity() {

    private lateinit var preferenceManager : PreferenceManager
    private lateinit var sWarnaDaun: TextView
    private lateinit var rbYaEpicormic: RadioButton
    private lateinit var rbTidakEpicormic: RadioButton
    private lateinit var sKerapatanDaun: TextView
    private lateinit var rbNormal: RadioButton
    private lateinit var rbKecil: RadioButton
    private lateinit var sWoundWood: TextView
    private lateinit var rbYaTwig: RadioButton
    private lateinit var rbTidakTwig: RadioButton
    private lateinit var sVigor: TextView
    private lateinit var etHama: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kesehatan_pohon)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        sWarnaDaun = findViewById(R.id.sWarnaDaun)
        rbYaEpicormic = findViewById(R.id.rbYaEpicormic)
        rbTidakEpicormic = findViewById(R.id.rbTidakEpicormic)
        sKerapatanDaun = findViewById(R.id.sKerapatanDaun)
        rbNormal = findViewById(R.id.rbNormal)
        rbKecil = findViewById(R.id.rbKecil)
        sWoundWood = findViewById(R.id.sWoundWood)
        rbYaTwig = findViewById(R.id.rbYaTwig)
        rbTidakTwig = findViewById(R.id.rbTidakTwig)
        sVigor = findViewById(R.id.sVigor)
        etHama = findViewById(R.id.etHama)


        preferenceManager = PreferenceManager(this)

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
            override fun onResponse(call: Call<RiwayatPohonResponse>, response: Response<RiwayatPohonResponse>) {

                if (!response.isSuccessful) {
                    Toast.makeText(this@DetailKesehatanPohonActivity, "Gagal mengambil data",
                        Toast.LENGTH_LONG).show();
                    return
                }

                val body = response.body()

                sWarnaDaun.setText(body?.data?.warnaDaun)
                sKerapatanDaun.setText(body?.data?.kerapatanDaun)
                sWoundWood.setText((body?.data?.woundWood))
                sVigor.setText(body?.data?.vigor)
                etHama.setText(body?.data?.hama)

                if (body?.data?.twigDieback == true) {
                    rbYaTwig.isChecked = true
                    rbTidakTwig.isEnabled = false
                } else {
                    rbTidakTwig.isChecked = true
                    rbYaTwig.isEnabled = false
                }

                if (body?.data?.ukuranDaun == "Normal") {
                    rbNormal.isChecked = true
                    rbKecil.isEnabled = false
                } else {
                    rbKecil.isChecked = true
                    rbNormal.isEnabled = false
                }

                if (body?.data?.epicormic == true) {
                    rbYaEpicormic.isChecked = true
                    rbTidakEpicormic.isEnabled = false
                } else {
                    rbTidakEpicormic.isChecked = true
                    rbYaEpicormic.isEnabled = false
                }

            }

            override fun onFailure(call: Call<RiwayatPohonResponse>, t: Throwable) {
                Toast.makeText(this@DetailKesehatanPohonActivity, "Gagal mengambil data",
                    Toast.LENGTH_LONG).show();
            }
        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
//        val intent = Intent(this, RiwayatPengamatanActivity::class.java)
//        startActivity(intent)
        finish()
    }
}