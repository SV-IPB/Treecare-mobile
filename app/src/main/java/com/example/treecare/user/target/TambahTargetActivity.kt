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
import com.example.treecare.service.ImagePreferenceManager
import com.example.treecare.service.KerusakanPreferenceManager
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.RetrofitHelperV1
import com.example.treecare.service.api.v1.RiwayatPohonService
import com.example.treecare.service.api.v1.TokenAuthenticator
import com.example.treecare.service.api.v1.request.RiwayatPohonRequest
import com.example.treecare.service.api.v1.request.riwayatKerusakanPohon
import com.example.treecare.service.api.v1.response.RiwayatPohonResponse
import com.example.treecare.user.kondisi_tapak.TambahKondisiTapakActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.activation.MimetypesFileTypeMap

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
    private lateinit var imagePreferenceManager: ImagePreferenceManager
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
        imagePreferenceManager = ImagePreferenceManager(this)
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
        //images
        fun getFileMimeType(file: File): String {
            val mimeTypesMap = MimetypesFileTypeMap()
            return mimeTypesMap.getContentType(file)
        }

        fun convertToPNG(file: File): File {
            return file
        }
        val imageUrls = imagePreferenceManager.getList("image_urls_list")
        Log.e("Debug Request Image Path", "list image: $imageUrls")
        val imageParts = mutableListOf<MultipartBody.Part>()

        imageUrls?.forEach { imageUrl ->
            val file = File(imageUrl)
            val contentType = getFileMimeType(file)
            Log.e("Debug Content Type", "Content Type: $contentType")

            if (!contentType.startsWith("image/png")) {
                val pngFile = convertToPNG(file)
                val requestFile = pngFile.asRequestBody("image/png".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("image", pngFile.name, requestFile)
                imageParts.add(part)
            } else {
                val requestFile = file.asRequestBody(contentType.toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("image", file.name, requestFile)
                imageParts.add(part)
            }
        }

        //karakteristik
        val keliling = sharedPreferences2.getString("keliling", "")?.toFloat()
        val tinggi = sharedPreferences2.getString("tinggi", "")?.toFloat()
        val lebarTajuk = sharedPreferences2.getString("lebarTajuk", "")?.toFloat()
        val bentuk = sharedPreferences2.getString("bentuk", "")
        val liveCrownRatio = sharedPreferences2.getString("crownRatio", "")?.toInt()
        val sejarahPemangkasan = sharedPreferences2.getString("sejarahPemangkasan", "")

        //kesehatan
        val warnaDaun = sharedPreferences3.getString("warnaDaun", "")
        val epicormic = sharedPreferences3.getInt("epicormic", -1) == R.id.rbYaEpicormic
        val kerapatanDaun = sharedPreferences3.getString("kerapatanDaun", "")
        val ukuranDaunString = when (sharedPreferences3.getInt("ukuranDaun", -1)) {
            0 -> "Normal"
            1 -> "Kecil"
            else -> "Normal"
        }
        val woundWood = sharedPreferences3.getString("woundWood", "")
        val twigDieback = sharedPreferences3.getInt("twig", -1) == R.id.rbYaTwig
        val vigor = sharedPreferences3.getString("vigor", "")
        val hama = sharedPreferences3.getString("hama", "")

        //kerusakan
        val allLists = kerusakanPreferenceManager.getAllLists()

        val riwayatKerusakanPohonList: MutableList<riwayatKerusakanPohon> = mutableListOf()
        val riwayatKerusakanPohonJsonStrings = mutableListOf<String>()

        allLists.forEach { (_, list) ->
            list?.forEach { riwayatKerusakanPohonModel ->
                val riwayatKerusakanPohon = riwayatKerusakanPohon(
                    gambar = riwayatKerusakanPohonModel?.gambar ?: listOf(),
                    deskripsi = riwayatKerusakanPohonModel?.deskripsi ?: "",
                    bagian_pohon = riwayatKerusakanPohonModel?.bagian_pohon ?: "",
                    potensi_kegagalan = riwayatKerusakanPohonModel?.potensi_kegagalan ?: 0,
                    ukuran_bagian_pohon = riwayatKerusakanPohonModel?.ukuran_bagian_pohon ?: 0,
                    peringkat_target = riwayatKerusakanPohonModel?.peringkat_target ?: 0,
                    butuh_tindakan = riwayatKerusakanPohonModel?.butuh_tindakan ?: false,
                    pemangkasan = riwayatKerusakanPohonModel?.pemangkasan ?: "",
                    detail_pemangkasan = riwayatKerusakanPohonModel?.detail_pemangkasan ?: "",
                    pohon_dipindahkan = riwayatKerusakanPohonModel?.pohon_dipindahkan ?: false,
                    target_dipindahkan = riwayatKerusakanPohonModel?.target_dipindahkan ?: false,
                    saran = riwayatKerusakanPohonModel?.saran ?: ""
                )
                riwayatKerusakanPohonList.add(riwayatKerusakanPohon)
                val riwayatKerusakanPohonJSON = Gson().toJson(riwayatKerusakanPohon)
                riwayatKerusakanPohonJsonStrings.add(riwayatKerusakanPohonJSON)
            }
        }

        //kondisi tapak
        val karakteristikTapakValue = sharedPreferences5.getString("karakteristikTapak", "")
        var karakteristikTapak = ""
        if ("Lainnya" == karakteristikTapakValue) {
            karakteristikTapak = sharedPreferences5.getString("karakteristikLainnya", "").toString()
        } else {
            if (karakteristikTapakValue != null) {
                karakteristikTapak = karakteristikTapakValue
            }
        }
        val gangguan = sharedPreferences5.getInt("gangguan", -1) == R.id.rbYaGangguan
        val masalahTanahValue = sharedPreferences5.getString("masalahTanah", "")
        var masalahTanah = ""
        if ("Lainnya" == masalahTanahValue) {
            masalahTanah = sharedPreferences5.getString("masalahTanahLainnya", "").toString()
        } else {
            if (masalahTanahValue != null) {
                masalahTanah = masalahTanahValue
            }
        }
        val gangguanLainnyaValue = sharedPreferences5.getString("gangguanLainnya", "")
        var gangguanLain = ""
        if ("Lainnya" == gangguanLainnyaValue) {
            gangguanLain = sharedPreferences5.getString("gangguanLainnyaText", "").toString()
        } else {
            if (gangguanLainnyaValue != null) {
                gangguanLain = gangguanLainnyaValue
            }
        }

        //target
        val pemanfaatanSekitar = sharedPreferences.getString("pemanfaatanArea", "")
        val dapatDipindahkan = sharedPreferences.getInt("dipindahkan", -1) == R.id.rbYaDipindahkan
        val dapatDibatasi = sharedPreferences.getInt("dibatasi", -1) == R.id.rbYaDibatasi
        val hunian = sharedPreferences.getString("hunian", "")

        val pohonRiwayatRequest = RiwayatPohonRequest(
            imageParts, riwayatKerusakanPohonList,
            keliling, tinggi, lebarTajuk,
            bentuk, liveCrownRatio, sejarahPemangkasan,
            warnaDaun, epicormic, kerapatanDaun,
            ukuranDaunString, woundWood, twigDieback,
            vigor, hama, karakteristikTapak,
            gangguan, masalahTanah, gangguanLain,
            pemanfaatanSekitar, dapatDipindahkan, dapatDibatasi, hunian
        )

        Log.e("Debug Request", "Full Request: $pohonRiwayatRequest")

        val token = preferenceManager.getAccessToken()
        val tokenAuthenticator = TokenAuthenticator(preferenceManager)
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .build()

        val retro = RetrofitHelperV1().getApiClientAuthV2(okHttpClient).create(RiwayatPohonService::class.java)
        val call = retro.createHistoryV2(id_pohon, token,
            pohonRiwayatRequest.image,
            pohonRiwayatRequest.riwayatKerusakanPohon,
            pohonRiwayatRequest.keliling!!,
            pohonRiwayatRequest.tinggi!!,
            pohonRiwayatRequest.lebarTajuk!!,
            pohonRiwayatRequest.bentuk!!,
            pohonRiwayatRequest.liveCrownRatio!!,
            pohonRiwayatRequest.sejarahPemangkasan!!,
            pohonRiwayatRequest.warnaDaun!!,
            pohonRiwayatRequest.epicormic!!,
            pohonRiwayatRequest.kerapatanDaun!!,
            pohonRiwayatRequest.ukuranDaun!!,
            pohonRiwayatRequest.woundWood!!,
            pohonRiwayatRequest.twigDieback!!,
            pohonRiwayatRequest.vigor!!,
            pohonRiwayatRequest.hama!!,
            pohonRiwayatRequest.karakteristikTapak!!,
            pohonRiwayatRequest.gangguan!!,
            pohonRiwayatRequest.masalahTanah!!,
            pohonRiwayatRequest.gangguanLain!!,
            pohonRiwayatRequest.pemanfaatanSekitar!!,
            pohonRiwayatRequest.dapatDipindahkan!!,
            pohonRiwayatRequest.dapatDibatasi!!,
            pohonRiwayatRequest.hunian!!,
            )

        call.enqueue(object : Callback<RiwayatPohonResponse> {
            override fun onResponse(call: Call<RiwayatPohonResponse>, response: Response<RiwayatPohonResponse>) {
                Log.e("Debug Raw Response: ", response.raw().toString())
                Log.e("Debug Raw Response message: ", response.message())
                Log.e("Debug Raw Response status: ", response.body()?.status.toString())
                val gson = GsonBuilder().setPrettyPrinting().create()
                val responseBody = gson.toJson(response.body())
                Log.e("Debug Body: ", responseBody)
                pbLoading.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<RiwayatPohonResponse>, t: Throwable) {
                Log.e("Debug Network API Error: ", t.message.toString())
                Log.e("Debug Error: ","network or API call failure")
                Log.e("Debug Error Details: ", t.stackTraceToString())
                Log.e("Debug Call: ", call.request().toString())
            }
        })
    }

    fun createMultipartBodyParts(imageUris: List<String>): List<MultipartBody.Part> {
        val parts = mutableListOf<MultipartBody.Part>()
        for (uri in imageUris) {
            val file = File(uri)
            if (file.exists()) {
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("image", file.name, requestFile)
                parts.add(part)
            }
        }
        return parts
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
        imagePreferenceManager.removeData()
    }

    private fun extractFilenameFromMultipartPart(part: MultipartBody.Part): String {
        val headers = part.headers
        if (headers != null) {
            for (i in 0 until headers.size) {
                if (headers.name(i).equals("Content-Disposition", ignoreCase = true)) {
                    val headerValue = headers.value(i)
                    val startIndex = headerValue.indexOf("filename=")
                    if (startIndex != -1) {
                        return headerValue.substring(startIndex + 9)
                    }
                }
            }
        }
        return "UnknownFilename"
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