package com.example.treecare.user.kondisi_tapak

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

class DetailKondisiTapakActivity : AppCompatActivity() {

    private lateinit var sKarakteristikTapak: TextView
    private lateinit var rbYaGangguan: RadioButton
    private lateinit var rbTidakGangguan: RadioButton
    private lateinit var sMasalahTanah: TextView
    private lateinit var sGangguanLainnya: TextView
    private lateinit var preferenceManager : PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kondisi_tapak)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        sKarakteristikTapak = findViewById(R.id.sKarakteristikTapak)
        rbYaGangguan = findViewById(R.id.rbYaGangguan)
        rbTidakGangguan = findViewById(R.id.rbTidakGangguan)
        sMasalahTanah = findViewById(R.id.sMasalahTanah)
        sGangguanLainnya = findViewById(R.id.sGangguanLainnya)

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
                    Toast.makeText(this@DetailKondisiTapakActivity, "Gagal mengambil data",
                        Toast.LENGTH_LONG).show();
                    return
                }

                val body = response.body()
                sKarakteristikTapak.setText(body?.data?.karakteristikTapak)
                sMasalahTanah.setText(body?.data?.masalahTanah)
                sGangguanLainnya.setText((body?.data?.gangguanLain))

                if (body?.data?.gangguan == true) {
                    rbYaGangguan.isChecked = true
                    rbTidakGangguan.isEnabled = false
                } else {
                    rbTidakGangguan.isChecked = true
                    rbYaGangguan.isEnabled = false
                }

            }

            override fun onFailure(call: Call<RiwayatPohonResponse>, t: Throwable) {
                Toast.makeText(this@DetailKondisiTapakActivity, "Gagal mengambil data",
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