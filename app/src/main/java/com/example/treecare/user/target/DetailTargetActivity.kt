package com.example.treecare.user.target

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
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

class DetailTargetActivity : AppCompatActivity() {

    private lateinit var sPemanfaatanArea: TextView
    private lateinit var rbYaDipindahkan: RadioButton
    private lateinit var rbTidakDipindahkan: RadioButton
    private lateinit var rbYaDibatasi: RadioButton
    private lateinit var rbTidakDibatasi: RadioButton
    private lateinit var sHunian: TextView
    private lateinit var pbLoading: ProgressBar
    private lateinit var preferenceManager : PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_target)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        sPemanfaatanArea = findViewById(R.id.sPemanfaatanArea)
        rbYaDipindahkan = findViewById(R.id.rbYaDipindahkan)
        rbTidakDipindahkan = findViewById(R.id.rbTidakDipindahkan)
        rbYaDibatasi = findViewById(R.id.rbYaDibatasi)
        rbTidakDibatasi = findViewById(R.id.rbTidakDibatasi)
        sHunian = findViewById(R.id.sHunian)
        pbLoading   = findViewById(R.id.pbLoading)

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
                pbLoading.visibility = View.GONE

                if (!response.isSuccessful) {
                    Toast.makeText(this@DetailTargetActivity, "Gagal mengambil data",
                        Toast.LENGTH_LONG).show();
                    return
                }

                val body = response.body()
                sPemanfaatanArea.setText(body?.data?.pemanfaatanSekitar)
                sHunian.setText(body?.data?.hunian)

                if (body?.data?.dapatDipindahkan == true) {
                    rbYaDipindahkan.isChecked = true
                    rbTidakDipindahkan.isEnabled = false
                } else {
                    rbYaDipindahkan.isEnabled = false
                    rbTidakDipindahkan.isChecked = true
                }

                if (body?.data?.dapatDibatasi == true) {
                    rbYaDibatasi.isChecked = true
                    rbTidakDibatasi.isEnabled = false
                } else {
                    rbTidakDibatasi.isChecked = true
                    rbYaDibatasi.isEnabled = false
                }

            }

            override fun onFailure(call: Call<RiwayatPohonResponse>, t: Throwable) {
                pbLoading.visibility = View.GONE
                Toast.makeText(this@DetailTargetActivity, "Gagal mengambil data",
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