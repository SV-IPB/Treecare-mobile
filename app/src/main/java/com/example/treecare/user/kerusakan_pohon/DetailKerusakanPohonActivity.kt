package com.example.treecare.user.kerusakan_pohon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treecare.R
import com.example.treecare.adapter.ImageAdapter
import com.example.treecare.adapter.ImageAdapterKerusakanPohon
import com.example.treecare.interfaces.ImageInterface
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.RetrofitHelperV1
import com.example.treecare.service.api.v1.RiwayatPohonService
import com.example.treecare.service.api.v1.TokenAuthenticator
import com.example.treecare.service.api.v1.response.RiwayatKerusakanPohonResponse
import com.example.treecare.service.api.v1.response.RiwayatPohonResponse
import com.example.treecare.service.model.RiwayatKerusakanPohonModel
import com.example.treecare.user.RiwayatPengamatanActivity
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailKerusakanPohonActivity : AppCompatActivity(), ImageInterface {

    private lateinit var preferenceManager : PreferenceManager
    private lateinit var rvImage: RecyclerView
    private lateinit var sBagianPohon: TextView
    private lateinit var etDeskripsiKerusakan: TextView
    private lateinit var sPotensiKegagalan: TextView
    private lateinit var sUkuranBagian: TextView
    private lateinit var sPeringkatTarget: TextView
    private lateinit var tvPBahayaValue: TextView
    private lateinit var sPemangkasan: TextView
    private lateinit var etDeskripsiPemangkasan: TextView
    private lateinit var etSaranLainnya: TextView

    private lateinit var rbYaTindakan: RadioButton
    private lateinit var rbTidakTindakan: RadioButton
    private lateinit var rbYaDipindahkan: RadioButton
    private lateinit var rbTidakDipindahkan: RadioButton
    private lateinit var rbYaTDipindahkan: RadioButton
    private lateinit var rbTidakTDipindahkan: RadioButton

    private var listImage = ArrayList<String> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kerusakan_pohon)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val tvKerusakan: TextView = findViewById(R.id.tvKerusakan)

        tvKerusakan.text = "Kerusakan " + intent.getIntExtra("nomorKerusakan", 1).toString()
        rvImage = findViewById(R.id.rvImage)
        sBagianPohon = findViewById(R.id.sBagianPohon)
        etDeskripsiKerusakan = findViewById(R.id.etDeskripsiKerusakan)
        sPotensiKegagalan = findViewById(R.id.sPotensiKegagalan)
        sUkuranBagian = findViewById(R.id.sUkuranBagian)
        sPeringkatTarget = findViewById(R.id.sPeringkatTarget)
        tvPBahayaValue = findViewById(R.id.tvPBahayaValue)
        sPemangkasan = findViewById(R.id.sPemangkasan)
        etDeskripsiPemangkasan = findViewById(R.id.etDeskripsiPemangkasan)
        etSaranLainnya = findViewById(R.id.etSaranLainnya)

        rbYaTindakan = findViewById(R.id.rbYaTindakan)
        rbTidakTindakan = findViewById(R.id.rbTidakTindakan)
        rbYaDipindahkan = findViewById(R.id.rbYaDipindahkan)
        rbTidakDipindahkan = findViewById(R.id.rbTidakDipindahkan)
        rbYaTDipindahkan = findViewById(R.id.rbYaTDipindahkan)
        rbTidakTDipindahkan = findViewById(R.id.rbTidakTDipindahkan)
        preferenceManager = PreferenceManager(this)

        rvImage.layoutManager = LinearLayoutManager(
            this@DetailKerusakanPohonActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rvImage.adapter = ImageAdapterKerusakanPohon(
            this@DetailKerusakanPohonActivity,
            listImage,
            this
        )
        btnBack.setOnClickListener {
//            val intent = Intent(this, ListKerusakanPohonActivity::class.java)
//            intent.putExtra("idKerusakan", intent.getStringExtra("idKerusakan"))
//            startActivity(intent)
            finish()
        }

        getValue()
    }

    fun getValue() {
        val idKerusakan = intent.getStringExtra("idKerusakan")
        val authToken = preferenceManager.getAccessToken()
        val tokenAuthenticator = TokenAuthenticator(preferenceManager)
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .build()
        val retro = RetrofitHelperV1()
            .getApiClientAuth(okHttpClient)
            .create(RiwayatPohonService::class.java)

        retro.getKerusakanDetail(idKerusakan, authToken).enqueue(object : Callback<RiwayatKerusakanPohonResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<RiwayatKerusakanPohonResponse>, response: Response<RiwayatKerusakanPohonResponse>) {

                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@DetailKerusakanPohonActivity,
                        "Gagal mendapatkan data kerusakan",
                        Toast.LENGTH_LONG
                    ).show();
                    return
                }

                val body = response.body()

                for (item in body?.data?.gambar!!) {
                    listImage.add(item)
                }
                rvImage.adapter?.notifyDataSetChanged()

                sBagianPohon.text = body?.data?.bagian_pohon
                etDeskripsiKerusakan.text = body?.data?.deskripsi
                sPotensiKegagalan.text = body?.data?.potensi_kegagalan.toString()
                sUkuranBagian.text = body?.data?.ukuran_bagian_pohon.toString()
                sPeringkatTarget.text = body?.data?.peringkat_target.toString()
                tvPBahayaValue.text = body?.data?.peringkat_bahaya.toString()
                sPemangkasan.text = body?.data?.pemangkasan
//                etDeskripsiPemangkasan.text = body?.data?.deskripsi
                etSaranLainnya.text = body?.data?.saran

                if (body?.data?.butuh_tindakan!!) {
                    rbYaTindakan.isChecked = true
                    rbTidakTindakan.isEnabled = false
                } else {
                    rbTidakTindakan.isChecked = true
                    rbYaTindakan.isEnabled = false
                }

                if (body.data?.pohon_dipindahkan!!) {
                    rbYaDipindahkan.isChecked = true
                    rbTidakDipindahkan.isEnabled = false
                } else {
                    rbTidakDipindahkan.isChecked = true
                    rbYaDipindahkan.isEnabled = false
                }

                if (body.data?.target_dipindahkan!!) {
                    rbYaTDipindahkan.isChecked = true
                    rbTidakTDipindahkan.isEnabled = false
                } else {
                    rbTidakTDipindahkan.isChecked = true
                    rbYaTDipindahkan.isEnabled = false
                }

            }

            override fun onFailure(call: Call<RiwayatKerusakanPohonResponse>, t: Throwable) {
                Toast.makeText(
                    this@DetailKerusakanPohonActivity,
                    "Gagal mendapatkan data kerusakan",
                    Toast.LENGTH_LONG
                ).show();
            }
        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
//        val intent = Intent(this, ListKerusakanPohonActivity::class.java)
//        startActivity(intent)
        finish()
    }

    override fun onItemClick(position: Int) {
        return
    }
}