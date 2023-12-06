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

        sWarnaDaun.setSelection(sharedPreferences.getInt("warnaDaun", 0))
        rgEpicormic.check(sharedPreferences.getInt("epicormic", R.id.rbTidakEpicormic))
        sKerapatanDaun.setSelection(sharedPreferences.getInt("kerapatanDaun", 0))
        rgUkuranDaun.check(sharedPreferences.getInt("ukuranDaun", R.id.rbNormal))
        sWoundWood.setSelection(sharedPreferences.getInt("woundWood", 0))
        rgTwig.check(sharedPreferences.getInt("twig", R.id.rbTidakTwig))
        sVigor.setSelection(sharedPreferences.getInt("vigor", 0))
        etHama.setText(sharedPreferences.getString("hama", ""))

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
        editor.putInt("warnaDaun", sWarnaDaun.selectedItemPosition)
        editor.putInt("epicormic", rgEpicormic.checkedRadioButtonId)
        editor.putInt("kerapatanDaun", sKerapatanDaun.selectedItemPosition)
        editor.putInt("ukuranDaun", rgUkuranDaun.checkedRadioButtonId)
        editor.putInt("woundWood", sWoundWood.selectedItemPosition)
        editor.putInt("twig", rgTwig.checkedRadioButtonId)
        editor.putInt("vigor", sVigor.selectedItemPosition)
        editor.putString("hama", etHama.text.toString())
        editor.apply()
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