package com.example.treecare.user.karakteristik_pohon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import com.example.treecare.R
import com.example.treecare.service.PreferenceManager
import com.example.treecare.user.kesehatan_pohon.TambahKesehatanPohonActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity

class TambahKarakteristikPohonActivity : AppCompatActivity() {

    private lateinit var etKeliling: EditText
    private lateinit var etTinggi: EditText
    private lateinit var etLebarTajuk: EditText
    private lateinit var sBentuk: Spinner
    private lateinit var etCrownRatio: EditText
    private lateinit var sSejarahPemangkasan: Spinner
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var id_pohon: String
    private lateinit var nomorPohon: String
    private lateinit var codeResponse: String

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferences2: SharedPreferences
    private lateinit var sharedPreferences3: SharedPreferences
    private lateinit var sharedPreferences4: SharedPreferences
    private lateinit var sharedPreferences5: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_karakteristik_pohon)

        preferenceManager = PreferenceManager(this)

        sharedPreferences = getSharedPreferences("KarakteristikPohon", Context.MODE_PRIVATE)
        sharedPreferences2 = getSharedPreferences("KesehatanPohon", Context.MODE_PRIVATE)
        sharedPreferences3 = getSharedPreferences("KerusakanPohon", Context.MODE_PRIVATE)
        sharedPreferences4 = getSharedPreferences("KondisiTapak", Context.MODE_PRIVATE)
        sharedPreferences5 = getSharedPreferences("Target", Context.MODE_PRIVATE)

        id_pohon = intent.getStringExtra("idPohon").toString()
        nomorPohon = intent.getStringExtra("nomor").toString()
        codeResponse = intent.getStringExtra("responseCode").toString()
        Log.e("id pohon : ", id_pohon)
        Log.e("nomor pohon : ", nomorPohon)
        Log.e("code pohon : ", codeResponse)

        etKeliling = findViewById(R.id.etKeliling)
        etTinggi = findViewById(R.id.etTinggi)
        etLebarTajuk = findViewById(R.id.etLebarTajuk)
        etCrownRatio = findViewById(R.id.etCrownRatio)
        sBentuk = findViewById(R.id.sBentuk)
        sSejarahPemangkasan = findViewById(R.id.sSejarahPemangkasan)

        loadValuesFromSharedPreferences()

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnSelanjutnya: AppCompatButton = findViewById(R.id.btnSelanjutnya)

        btnBack.setOnClickListener {
            clearSharedPreferences()
            val intent = Intent(this, PengamatanVisualActivity::class.java)
            intent.putExtra("idPohon",id_pohon)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivity(intent)
            finish()
        }

        btnSelanjutnya.setOnClickListener {
            saveValuesToSharedPreferences()
            val intent = Intent(this, TambahKesehatanPohonActivity::class.java)
            intent.putExtra("idPohon",id_pohon)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivity(intent)
            finish()
        }
    }

    private fun saveValuesToSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.putString("keliling", etKeliling.text.toString())
        editor.putString("tinggi", etTinggi.text.toString())
        editor.putString("lebarTajuk", etLebarTajuk.text.toString())
        editor.putString("crownRatio", etCrownRatio.text.toString())
        editor.putString("bentuk", sBentuk.selectedItem.toString())
        editor.putString("sejarahPemangkasan", sSejarahPemangkasan.selectedItem.toString())
        editor.apply()
    }

    private fun loadValuesFromSharedPreferences() {
        etKeliling.setText(sharedPreferences.getString("keliling", ""))
        etTinggi.setText(sharedPreferences.getString("tinggi", ""))
        etLebarTajuk.setText(sharedPreferences.getString("lebarTajuk", ""))
        etCrownRatio.setText(sharedPreferences.getString("crownRatio", ""))

        val bentukValue = sharedPreferences.getString("bentuk", "")
        val bentukPosition = resources.getStringArray(R.array.Bentuk).indexOf(bentukValue)
        sBentuk.setSelection(if (bentukPosition != -1) bentukPosition else 0)

        val sejarahPemangkasanValue = sharedPreferences.getString("sejarahPemangkasan", "")
        val sejarahPemangkasanPosition = resources.getStringArray(R.array.SejarahPemangkasan).indexOf(sejarahPemangkasanValue)
        sSejarahPemangkasan.setSelection(if (sejarahPemangkasanPosition != -1) sejarahPemangkasanPosition else 0)
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
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        clearSharedPreferences()
        val intent = Intent(this, PengamatanVisualActivity::class.java)
        intent.putExtra("idPohon",id_pohon)
        intent.putExtra("nomor",nomorPohon)
        intent.putExtra("responseCode",codeResponse)
        startActivity(intent)
        finish()
    }
}