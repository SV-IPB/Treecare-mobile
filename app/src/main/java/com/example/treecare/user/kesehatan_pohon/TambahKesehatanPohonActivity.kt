package com.example.treecare.user.kesehatan_pohon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import com.example.treecare.R
import com.example.treecare.service.PreferenceManager
import com.example.treecare.user.karakteristik_pohon.TambahKarakteristikPohonActivity
import com.example.treecare.user.kerusakan_pohon.KerusakanPohonActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity

class TambahKesehatanPohonActivity : AppCompatActivity() {

    private lateinit var sWarnaDaun: Spinner
    private lateinit var rgEpicormic: RadioGroup
    private lateinit var rbYaEpicormic: RadioButton
    private lateinit var rbTidakEpicormic: RadioButton
    private lateinit var sKerapatanDaun: Spinner
    private lateinit var rgUkuranDaun: RadioGroup
    private lateinit var rbNormal: RadioButton
    private lateinit var rbKecil: RadioButton
    private lateinit var sWoundWood: Spinner
    private lateinit var rgTwig: RadioGroup
    private lateinit var rbYaTwig: RadioButton
    private lateinit var rbTidakTwig: RadioButton
    private lateinit var sVigor: Spinner
    private lateinit var etHama: EditText

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var id_pohon: String
    private lateinit var nomorPohon: String
    private lateinit var codeResponse: String

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_kesehatan_pohon)

        preferenceManager = PreferenceManager(this)

        sharedPreferences = getSharedPreferences("KesehatanPohon", Context.MODE_PRIVATE)

        sWarnaDaun = findViewById(R.id.sWarnaDaun)
        rgEpicormic = findViewById(R.id.rgEpicormic)
        rbYaEpicormic = findViewById(R.id.rbYaEpicormic)
        rbTidakEpicormic = findViewById(R.id.rbTidakEpicormic)
        sKerapatanDaun = findViewById(R.id.sKerapatanDaun)
        rgUkuranDaun = findViewById(R.id.rgUkuranDaun)
        rbNormal = findViewById(R.id.rbNormal)
        rbKecil = findViewById(R.id.rbKecil)
        sWoundWood = findViewById(R.id.sWoundWood)
        rgTwig = findViewById(R.id.rgTwig)
        rbYaTwig = findViewById(R.id.rbYaTwig)
        rbTidakTwig = findViewById(R.id.rbTidakTwig)
        sVigor = findViewById(R.id.sVigor)
        etHama = findViewById(R.id.etHama)

        id_pohon = intent.getStringExtra("idPohon").toString()
        nomorPohon = intent.getStringExtra("nomor").toString()
        codeResponse = intent.getStringExtra("responseCode").toString()
        Log.e("id pohon : ", id_pohon)
        Log.e("nomor pohon : ", nomorPohon)
        Log.e("code pohon : ", codeResponse)

        loadValuesFromSharedPreferences()

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnSelanjutnya: AppCompatButton = findViewById(R.id.btnSelanjutnya)

        btnBack.setOnClickListener {
            saveValuesToSharedPreferences()
            val intent = Intent(this, TambahKarakteristikPohonActivity::class.java)
            intent.putExtra("idPohon",id_pohon)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivity(intent)
            finish()
        }

        btnSelanjutnya.setOnClickListener {
            saveValuesToSharedPreferences()
            val intent = Intent(this, KerusakanPohonActivity::class.java)
            intent.putExtra("idPohon",id_pohon)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivity(intent)
            finish()
        }
    }

    private fun saveValuesToSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.putInt("epicormic", if (rgEpicormic.checkedRadioButtonId != -1) rgEpicormic.checkedRadioButtonId else -1)
        editor.putInt("ukuranDaun", if (rgUkuranDaun.checkedRadioButtonId != -1) rgUkuranDaun.checkedRadioButtonId else -1)
        editor.putInt("twig", if (rgTwig.checkedRadioButtonId != -1) rgTwig.checkedRadioButtonId else -1)
        editor.putString("warnaDaun", sWarnaDaun.selectedItem.toString())
        editor.putString("kerapatanDaun", sKerapatanDaun.selectedItem.toString())
        editor.putString("woundWood", sWoundWood.selectedItem.toString())
        editor.putString("vigor", sVigor.selectedItem.toString())
        editor.putString("hama", etHama.text.toString())
        editor.apply()
    }

    private fun loadValuesFromSharedPreferences(){
        val epicormicCheckedId = sharedPreferences.getInt("epicormic", -1)
        if (epicormicCheckedId != -1) {
            rgEpicormic.check(epicormicCheckedId)
        }

        val ukuranDaunCheckedId = sharedPreferences.getInt("ukuranDaun", -1)
        if (ukuranDaunCheckedId != -1) {
            rgUkuranDaun.check(ukuranDaunCheckedId)
        }

        val twigCheckedId = sharedPreferences.getInt("twig", -1)
        if (twigCheckedId != -1) {
            rgTwig.check(twigCheckedId)
        }

        etHama.setText(sharedPreferences.getString("hama", ""))

        val warnaDaunValue = sharedPreferences.getString("warnaDaun", "")
        val warnaDaunPosition = resources.getStringArray(R.array.WarnaDaun).indexOf(warnaDaunValue)
        sWarnaDaun.setSelection(if (warnaDaunPosition != -1) warnaDaunPosition else 0)

        val kerapatanDaunValue = sharedPreferences.getString("kerapatanDaun", "")
        val kerapatanDaunPosition = resources.getStringArray(R.array.KerapatanDaun).indexOf(kerapatanDaunValue)
        sKerapatanDaun.setSelection(if (kerapatanDaunPosition != -1) kerapatanDaunPosition else 0)

        val woundWoodValue = sharedPreferences.getString("woundWood", "")
        val woundWoodPosition = resources.getStringArray(R.array.WoundWood).indexOf(woundWoodValue)
        sWoundWood.setSelection(if (woundWoodPosition != -1) woundWoodPosition else 0)

        val vigorValue = sharedPreferences.getString("vigor", "")
        val vigorPosition = resources.getStringArray(R.array.Vigor).indexOf(vigorValue)
        sVigor.setSelection(if (vigorPosition != -1) vigorPosition else 0)
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        saveValuesToSharedPreferences()
        val intent = Intent(this, TambahKarakteristikPohonActivity::class.java)
        intent.putExtra("idPohon",id_pohon)
        intent.putExtra("nomor",nomorPohon)
        intent.putExtra("responseCode",codeResponse)
        startActivity(intent)
        finish()
    }
}