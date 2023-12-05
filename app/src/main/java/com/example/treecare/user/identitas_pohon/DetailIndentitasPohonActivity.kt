package com.example.treecare.user.identitas_pohon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.treecare.R
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.PohonService
import com.example.treecare.service.api.v1.RetrofitHelperV1
import com.example.treecare.service.api.v1.TokenAuthenticator
import com.example.treecare.service.api.v1.response.IdentitasPohonResponse
import com.example.treecare.user.RiwayatPerubahanActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailIndentitasPohonActivity : AppCompatActivity() {

    private lateinit var ivFoto: ImageView
    private lateinit var etNomorPohon: TextView
    private lateinit var etAlamat: TextView
    private lateinit var tvCoordinate: TextView
    private lateinit var etNamaProyek: TextView
    private lateinit var sPemilik: TextView
    private lateinit var etJenisPohon: TextView
    private lateinit var sNilaiSpesial: TextView
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var nomor: String
    private lateinit var codeResponse: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_indentitas_pohon)

        preferenceManager = PreferenceManager(this)

        nomor = intent.getStringExtra("nomor").toString()
        codeResponse = intent.getStringExtra("responseCode").toString()

        ivFoto = findViewById(R.id.ivFoto)
        etNomorPohon = findViewById(R.id.etNomorPohon)
        etAlamat = findViewById(R.id.etAlamat)
        tvCoordinate = findViewById(R.id.tvCoordinate)
        etNamaProyek = findViewById(R.id.etNamaProyek)
        sPemilik = findViewById(R.id.sPemilik)
        etJenisPohon = findViewById(R.id.etJenisPohon)
        sNilaiSpesial = findViewById(R.id.sNilaiSpesial)

        getByNomorPohon()

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnUbah: AppCompatButton = findViewById(R.id.btnUbah)
        val btnHistory: ImageView = findViewById(R.id.btnHistory)
        val mapView: MapView = findViewById(R.id.map)

        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.controller.setZoom(17.0)
        mapView.controller.setCenter(GeoPoint(-6.5971,106.8060))

        btnBack.setOnClickListener {
            val intent = Intent(this, PengamatanVisualActivity::class.java)
            intent.putExtra("nomor", nomor)
            intent.putExtra("responseCode", codeResponse)
            startActivity(intent)
            finish()
        }

        btnUbah.setOnClickListener {
            val intent = Intent(this, EditIdentitasPohonActivity::class.java)
            intent.putExtra("nomor", nomor)
            intent.putExtra("responseCode", codeResponse)
            startActivity(intent)
            finish()
        }

        btnHistory.setOnClickListener {
            val intent = Intent(this, RiwayatPerubahanActivity::class.java)
            intent.putExtra("nomor", nomor)
            intent.putExtra("responseCode", codeResponse)
            startActivity(intent)
            finish()
        }
    }

    private fun getByNomorPohon(){
        var nomorPohon = intent.getStringExtra("nomor")
        val token = preferenceManager.getAccessToken()
        val tokenAuthenticator = TokenAuthenticator(preferenceManager)
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .build()
        val Retro = RetrofitHelperV1().getApiClientAuth(okHttpClient).create(PohonService::class.java)
        Retro.getByNomorPohon(nomorPohon,token).enqueue(object : Callback<IdentitasPohonResponse> {
            override fun onResponse(call: Call<IdentitasPohonResponse>, response: Response<IdentitasPohonResponse>) {
                val gson = GsonBuilder().setPrettyPrinting().create()
                val responseBody = gson.toJson(response.body())
                val responseCode = response.code()
                Log.e("Body: ", responseBody)

                val latitude = response.body()?.data?.latitude
                val longitude = response.body()?.data?.longitude

                val imgUri = response.body()?.data?.gambar
                Picasso.get().invalidate(imgUri)
                Picasso.get().load(imgUri).into(ivFoto)

                etNomorPohon.text = response.body()?.data?.nomorPohon
                etAlamat.text = response.body()?.data?.alamat
                tvCoordinate.text = "$latitude, $longitude"
                etNamaProyek.text = response.body()?.data?.namaProyek
                sPemilik.text = response.body()?.data?.namaPemilik
                etJenisPohon.text =  response.body()?.data?.jenisPohon
                sNilaiSpesial.text = response.body()?.data?.nilaiSpesial


            }
            override fun onFailure(call: Call<IdentitasPohonResponse>, t: Throwable) {
                Log.e("Network API Error: ", t.message.toString())
                Log.e("Error: ","network or API call failure")
            }
        })
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, PengamatanVisualActivity::class.java)
        intent.putExtra("nomor", nomor)
        intent.putExtra("responseCode", codeResponse)
        startActivity(intent)
        finish()
    }
}