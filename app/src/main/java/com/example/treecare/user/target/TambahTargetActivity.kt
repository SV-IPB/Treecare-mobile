package com.example.treecare.user.target

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.treecare.R
import com.example.treecare.service.KerusakanPreferenceManager
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.PohonService
import com.example.treecare.service.api.v1.RetrofitHelperV1
import com.example.treecare.service.api.v1.RiwayatPohonService
import com.example.treecare.service.api.v1.TokenAuthenticator
import com.example.treecare.service.api.v1.request.RiwayatKerusakanPohon
import com.example.treecare.service.api.v1.request.RiwayatPohonRequest
import com.example.treecare.service.api.v1.response.IdentitasPohonResponse
import com.example.treecare.service.api.v1.response.RiwayatPohonsResponse
import com.example.treecare.service.model.RiwayatKerusakanPohonModel
import com.example.treecare.user.kondisi_tapak.TambahKondisiTapakActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class TambahTargetActivity : AppCompatActivity() {

    private lateinit var sPemanfaatanArea: Spinner
    private lateinit var rgDipindahkan: RadioGroup
    private lateinit var rbYaDipindahkan: RadioButton
    private lateinit var rbTidakDipindahkan: RadioButton
    private lateinit var rgDibatasi: RadioGroup
    private lateinit var rbYaDibatasi: RadioButton
    private lateinit var rbTidakDibatasi: RadioButton
    private lateinit var sHunian: Spinner
    private lateinit var pbLoading: ProgressBar

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var id_pohon: String
    private lateinit var nomorPohon: String
    private lateinit var codeResponse: String

    private lateinit var kerusakanPreferenceManager: KerusakanPreferenceManager
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferences2: SharedPreferences
    private lateinit var sharedPreferences3: SharedPreferences
    private lateinit var sharedPreferences4: SharedPreferences
    private lateinit var sharedPreferences5: SharedPreferences

    private lateinit var counterPreferences: SharedPreferences
    private var counter:Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_target)

        preferenceManager = PreferenceManager(this)
        counterPreferences = getSharedPreferences("kerusakanCounter", Context.MODE_PRIVATE)

        kerusakanPreferenceManager = KerusakanPreferenceManager(this)
        sharedPreferences = getSharedPreferences("Target", Context.MODE_PRIVATE)
        sharedPreferences2 = getSharedPreferences("KarakteristikPohon", Context.MODE_PRIVATE)
        sharedPreferences3 = getSharedPreferences("KesehatanPohon", Context.MODE_PRIVATE)
        sharedPreferences4 = getSharedPreferences("KerusakanPohon", Context.MODE_PRIVATE)
        sharedPreferences5 = getSharedPreferences("KondisiTapak", Context.MODE_PRIVATE)

        counter = counterPreferences.getInt("counter",-1)

        id_pohon = intent.getStringExtra("idPohon").toString()
        nomorPohon = intent.getStringExtra("nomor").toString()
        codeResponse = intent.getStringExtra("responseCode").toString()
        Log.e("id pohon : ", id_pohon)
        Log.e("nomor pohon : ", nomorPohon)
        Log.e("code pohon : ", codeResponse)

        sPemanfaatanArea = findViewById(R.id.sPemanfaatanArea)
        rgDipindahkan = findViewById(R.id.rgDipindahkan)
        rbYaDipindahkan = findViewById(R.id.rbYaDipindahkan)
        rbTidakDipindahkan = findViewById(R.id.rbTidakDipindahkan)
        rgDibatasi = findViewById(R.id.rgDibatasi)
        rbYaDibatasi = findViewById(R.id.rbYaDibatasi)
        rbTidakDibatasi = findViewById(R.id.rbTidakDibatasi)
        sHunian = findViewById(R.id.sHunian)
        pbLoading = findViewById(R.id.pbLoading)

        pbLoading.visibility = View.INVISIBLE

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnSimpan: AppCompatButton = findViewById(R.id.btnSimpan)

        loadValuesFromSharedPreferences()

        btnBack.setOnClickListener {
            saveValuesToSharedPreferences()
            val intent = Intent(this, TambahKondisiTapakActivity::class.java)
            intent.putExtra("idPohon",id_pohon)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivity(intent)
            finish()
        }

        btnSimpan.setOnClickListener {
            saveValuesToSharedPreferences()
            showConfirmDialog()
        }
    }

    private fun saveValuesToSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.putInt("dipindahkan", if (rgDipindahkan.checkedRadioButtonId != -1) rgDipindahkan.checkedRadioButtonId else -1)
        editor.putInt("dibatasi", if (rgDibatasi.checkedRadioButtonId != -1) rgDibatasi.checkedRadioButtonId else -1)
        editor.putString("pemanfaatanArea", sPemanfaatanArea.selectedItem.toString())
        editor.putString("hunian", sHunian.selectedItem.toString())
        editor.apply()
    }

    private fun loadValuesFromSharedPreferences(){
        val dipindahkanCheckedId = sharedPreferences.getInt("dipindahkan", -1)
        if (dipindahkanCheckedId != -1) {
            rgDipindahkan.check(dipindahkanCheckedId)
        }

        val dibatasiCheckedId = sharedPreferences.getInt("dibatasi", -1)
        if (dibatasiCheckedId != -1) {
            rgDibatasi.check(dibatasiCheckedId)
        }

        val pemanfaatanAreaValue = sharedPreferences.getString("pemanfaatanArea", "")
        val pemanfaatanAreaPosition = resources.getStringArray(R.array.PemanfaatanArea).indexOf(pemanfaatanAreaValue)
        sPemanfaatanArea.setSelection(if (pemanfaatanAreaPosition != -1) pemanfaatanAreaPosition else 0)

        val hunianValue = sharedPreferences.getString("hunian", "")
        val hunianPosition = resources.getStringArray(R.array.Hunian).indexOf(hunianValue)
        sHunian.setSelection(if (hunianPosition != -1) hunianPosition else 0)
    }

    private fun createHistory(){
        val request = RiwayatPohonRequest()
        //karakteristik
        request.keliling = sharedPreferences2.getString("keliling", "")?.toFloat()
        request.tinggi = sharedPreferences2.getString("tinggi", "")?.toFloat()
        request.lebarTajuk = sharedPreferences2.getString("lebarTajuk", "")?.toFloat()
        request.bentuk = sharedPreferences2.getString("bentuk", "")
        request.liveCrownRatio = sharedPreferences2.getString("crownRatio", "")?.toInt()
        request.sejarahPemangkasan = sharedPreferences2.getString("sejarahPemangkasan", "")
        //kesehatan
        request.warnaDaun = sharedPreferences3.getString("warnaDaun", "")
        request.epicormic = sharedPreferences3.getInt("epicormic", -1) == R.id.rbYaEpicormic
        request.kerapatanDaun = sharedPreferences3.getString("kerapatanDaun", "")
        val ukuranDaun = sharedPreferences3.getInt("ukuranDaun", -1)
        val ukuranDaunString = when (ukuranDaun) {
            0 -> "Normal"
            1 -> "Kecil"
            else -> "Normal"
        }
        request.ukuranDaun = ukuranDaunString
        request.woundWood = sharedPreferences3.getString("woundWood", "")
        request.twigDieback = sharedPreferences3.getInt("twig", -1) == R.id.rbYaTwig
        request.vigor = sharedPreferences3.getString("vigor", "")
        request.hama = sharedPreferences3.getString("hama", "")
        //kerusakan
        val allLists = kerusakanPreferenceManager.getAllLists()
        val riwayatKerusakanList: MutableList<RiwayatKerusakanPohon> = mutableListOf()

        if (counter > 0) {
            for ((key, list) in allLists) {
                if (list != null) {
                    for (riwayatKerusakanPohonModel in list) {
                        if (riwayatKerusakanPohonModel != null) {
                            val gambarKerusakanWithPrefix = riwayatKerusakanPohonModel.gambarKerusakan?.map {
                                "data:image/png;base64,$it"
                            }

                            val riwayatKerusakanPohon = RiwayatKerusakanPohon(
                                gambar = gambarKerusakanWithPrefix,
                                bagian_pohon = riwayatKerusakanPohonModel.bagianPohon,
                                deskripsi = riwayatKerusakanPohonModel.deksripsi,
                                potensi_kegagalan = riwayatKerusakanPohonModel.potensiKegagalan,
                                ukuran_bagian_pohon = riwayatKerusakanPohonModel.ukuranBagianPohon,
                                peringkat_target = riwayatKerusakanPohonModel.peringkatTarget,
                                peringkat_bahaya = riwayatKerusakanPohonModel.peringkatBahaya?.toInt(),
                                butuh_tindakan = riwayatKerusakanPohonModel.butuhTindakan,
                                pemangkasan = riwayatKerusakanPohonModel.pemangkasan,
                                pohon_dipindahkan = riwayatKerusakanPohonModel.pohonDipindahkan,
                                target_dipindahkan = riwayatKerusakanPohonModel.targetDipindahkan,
                                saran = riwayatKerusakanPohonModel.saran
                            )
                            riwayatKerusakanList.add(riwayatKerusakanPohon)
                            //Log.e("Request", "Riwayat Kerusakan Pohon: $riwayatKerusakanPohon")
                        }
                    }
                }
            }
        }

        request.riwayatKerusakanPohon = riwayatKerusakanList
        //kondisi tapak
        val karakteristikTapakValue = sharedPreferences5.getString("karakteristikTapak", "")
        if ("Lainnya" == karakteristikTapakValue) {
            request.karakteristikTapak = sharedPreferences5.getString("karakteristikLainnya", "")
        } else {
            request.karakteristikTapak = karakteristikTapakValue
        }
        request.gangguan = sharedPreferences5.getInt("gangguan", -1) == R.id.rbYaGangguan
        val masalahTanahValue = sharedPreferences5.getString("masalahTanah", "")
        if ("Lainnya" == masalahTanahValue) {
            request.masalahTanah = sharedPreferences5.getString("masalahTanahLainnya", "")
        } else {
            request.masalahTanah = masalahTanahValue
        }
        val gangguanLainnyaValue = sharedPreferences5.getString("gangguanLainnya", "")
        if ("Lainnya" == gangguanLainnyaValue) {
            request.gangguanLain = sharedPreferences5.getString("gangguanLainnyaText", "")
        } else {
            request.gangguanLain = gangguanLainnyaValue
        }
        //target
        request.pemanfaatanSekitar = sharedPreferences.getString("pemanfaatanArea", "")
        request.dapatDipindahkan = sharedPreferences.getInt("dipindahkan", -1) == R.id.rbYaDipindahkan
        request.dapatDibatasi = sharedPreferences.getInt("dibatasi", -1) == R.id.rbYaDibatasi
        request.hunian = sharedPreferences.getString("hunian", "")

//        Log.e("Request", "Keliling: ${request.keliling}")
//        Log.e("Request", "Tinggi: ${request.tinggi}")
//        Log.e("Request", "Lebar Tajuk: ${request.lebarTajuk}")
//        Log.e("Request", "Bentuk: ${request.bentuk}")
//        Log.e("Request", "Live Crown Ratio: ${request.liveCrownRatio}")
//        Log.e("Request", "Sejarah Pemangkasan: ${request.sejarahPemangkasan}")
//        Log.e("Request", "Warna Daun: ${request.warnaDaun}")
//        Log.e("Request", "Epicormic: ${request.epicormic}")
//        Log.e("Request", "Kerapatan Daun: ${request.kerapatanDaun}")
//        Log.e("Request", "Ukuran Daun: ${request.ukuranDaun}")
//        Log.e("Request", "Wound Wood: ${request.woundWood}")
//        Log.e("Request", "Twig Dieback: ${request.twigDieback}")
//        Log.e("Request", "Vigor: ${request.vigor}")
//        Log.e("Request", "Hama: ${request.hama}")
//        Log.e("Request", "Karakteristik Tapak: ${request.karakteristikTapak}")
//        Log.e("Request", "Gangguan: ${request.gangguan}")
//        Log.e("Request", "Masalah Tanah: ${request.masalahTanah}")
//        Log.e("Request", "Gangguan lain: ${request.gangguanLain}")
//        Log.e("Request", "Pemanfaatan Sekitar: ${request.pemanfaatanSekitar}")
//        Log.e("Request", "Dapat Dipindahkan: ${request.dapatDipindahkan}")
//        Log.e("Request", "Dapat Dibatasi: ${request.dapatDibatasi}")
//        Log.e("Request", "Hunian: ${request.hunian}")
        Log.e("Request", "Full Request: ${Gson().toJson(request)}")

        val token = preferenceManager.getAccessToken()
        val tokenAuthenticator = TokenAuthenticator(preferenceManager)
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            //.connectTimeout(60, TimeUnit.SECONDS)
            //.readTimeout(60, TimeUnit.SECONDS)
            //.writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val retro = RetrofitHelperV1().getApiClientAuth(okHttpClient).create(RiwayatPohonService::class.java)
        retro.createHistory(request,id_pohon,token).enqueue(object : Callback<RiwayatPohonsResponse> {
            override fun onResponse(call: Call<RiwayatPohonsResponse>, response: Response<RiwayatPohonsResponse>) {
                Log.e("Raw Response: ", response.raw().toString())
                Log.e("Raw Response message: ", response.message())
                Log.e("Raw Response status: ", response.body()?.status.toString())
                val gson = GsonBuilder().setPrettyPrinting().create()
                val responseBody = gson.toJson(response.body())
                Log.e("Body: ", responseBody)
                pbLoading.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<RiwayatPohonsResponse>, t: Throwable) {
                Log.e("Network API Error: ", t.message.toString())
                Log.e("Error: ","network or API call failure")
            }
        })
    }

    private fun showConfirmDialog(){
        if (isFinishing) {
            return
        }

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.setTitle("Confirm")
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnSimpan: AppCompatButton = dialog.findViewById(R.id.btnSimpan)
        val btnKembali: TextView = dialog.findViewById(R.id.btnKembali)

        btnSimpan.setOnClickListener {
            pbLoading.visibility = View.VISIBLE
            dialog.dismiss()
            if (!isFinishing) {
                createHistory()
                val intent = Intent(this, PengamatanVisualActivity::class.java)
                intent.putExtra("idPohon", id_pohon)
                intent.putExtra("nomor", nomorPohon)
                intent.putExtra("responseCode", codeResponse)
                clearSharedPreferences()
                startActivity(intent)
                finish()
            }
        }

        btnKembali.setOnClickListener {
            dialog.dismiss()
            if (!isFinishing) {
                finish()
            }
        }

        if (!isFinishing) {
            dialog.show()
        }
    }


    private fun clearSharedPreferences() {
        val editor = sharedPreferences.edit()
        val editor2 = sharedPreferences2.edit()
        val editor3= sharedPreferences3.edit()
        val editor4 = sharedPreferences4.edit()
        val editor5 = sharedPreferences5.edit()
        editor.clear().apply()
        editor2.clear().apply()
        editor3.clear().apply()
        editor4.clear().apply()
        editor5.clear().apply()
        kerusakanPreferenceManager.removeData()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        saveValuesToSharedPreferences()
        val intent = Intent(this, TambahKondisiTapakActivity::class.java)
        intent.putExtra("idPohon",id_pohon)
        intent.putExtra("nomor",nomorPohon)
        intent.putExtra("responseCode",codeResponse)
        startActivity(intent)
        finish()
    }
}