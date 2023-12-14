package com.example.treecare.user.kondisi_tapak

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.treecare.R
import com.example.treecare.service.PreferenceManager
import com.example.treecare.user.kerusakan_pohon.KerusakanPohonActivity
import com.example.treecare.user.target.TambahTargetActivity

class TambahKondisiTapakActivity : AppCompatActivity() {

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var id_pohon: String
    private lateinit var nomorPohon: String
    private lateinit var codeResponse: String

    private lateinit var sKarakteristikTapak: Spinner
    private lateinit var etKarakteristikLainnya: EditText
    private lateinit var rgGangguan: RadioGroup
    private lateinit var rbYaGangguan: RadioButton
    private lateinit var rbTidakGangguan: RadioButton
    private lateinit var sMasalahTanah: Spinner
    private lateinit var etMasalahTanahLainnya: EditText
    private lateinit var sGangguanLainnya: Spinner
    private lateinit var etGangguanLainnya: EditText

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_kondisi_tapak)

        preferenceManager = PreferenceManager(this)

        sharedPreferences = getSharedPreferences("KondisiTapak", Context.MODE_PRIVATE)

        id_pohon = intent.getStringExtra("idPohon").toString()
        nomorPohon = intent.getStringExtra("nomor").toString()
        codeResponse = intent.getStringExtra("responseCode").toString()
        Log.e("id pohon : ", id_pohon)
        Log.e("nomor pohon : ", nomorPohon)
        Log.e("code pohon : ", codeResponse)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnSelanjutnya: AppCompatButton = findViewById(R.id.btnSelanjutnya)
        sKarakteristikTapak = findViewById(R.id.sKarakteristikTapak)
        etKarakteristikLainnya = findViewById(R.id.etKarakteristikLainnya)
        rgGangguan = findViewById(R.id.rgGangguan)
        rbYaGangguan = findViewById(R.id.rbYaGangguan)
        rbTidakGangguan = findViewById(R.id.rbTidakGangguan)
        sMasalahTanah = findViewById(R.id.sMasalahTanah)
        etMasalahTanahLainnya = findViewById(R.id.etMasalahTanahLainnya)
        sGangguanLainnya = findViewById(R.id.sGangguanLainnya)
        etGangguanLainnya = findViewById(R.id.etGangguanLainnya)

        loadValuesFromSharedPreferences()

        sKarakteristikTapak.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = sKarakteristikTapak.selectedItem.toString()
                if(selectedItem == "Lainnya"){
                    etKarakteristikLainnya.visibility = View.VISIBLE
                    etKarakteristikLainnya.setText(sharedPreferences.getString("karakteristikLainnya", ""))
                }else{
                    etKarakteristikLainnya.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        sMasalahTanah.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = sMasalahTanah.selectedItem.toString()
                if(selectedItem == "Lainnya"){
                    etMasalahTanahLainnya.visibility = View.VISIBLE
                    etMasalahTanahLainnya.setText(sharedPreferences.getString("masalahTanahLainnya", ""))
                }else{
                    etMasalahTanahLainnya.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        sGangguanLainnya.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = sGangguanLainnya.selectedItem.toString()
                if(selectedItem == "Lainnya"){
                    etGangguanLainnya.visibility = View.VISIBLE
                    etGangguanLainnya.setText(sharedPreferences.getString("gangguanLainnyaText", ""))
                }else{
                    etGangguanLainnya.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        btnBack.setOnClickListener {
            saveValuesToSharedPreferences()
            val intent = Intent(this, KerusakanPohonActivity::class.java)
            intent.putExtra("idPohon",id_pohon)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivity(intent)
            finish()
        }

        btnSelanjutnya.setOnClickListener {
            saveValuesToSharedPreferences()
            val intent = Intent(this, TambahTargetActivity::class.java)
            intent.putExtra("idPohon",id_pohon)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivity(intent)
            finish()
        }
    }

    private fun saveValuesToSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.putInt("gangguan", if (rgGangguan.checkedRadioButtonId != -1) rgGangguan.checkedRadioButtonId else -1)
        editor.putString("karakteristikTapak", sKarakteristikTapak.selectedItem.toString())
        editor.putString("karakteristikLainnya", etKarakteristikLainnya.text.toString())
        editor.putString("masalahTanah", sMasalahTanah.selectedItem.toString())
        editor.putString("masalahTanahLainnya", etMasalahTanahLainnya.text.toString())
        editor.putString("gangguanLainnya", sGangguanLainnya.selectedItem.toString())
        editor.putString("gangguanLainnyaText", etGangguanLainnya.text.toString())
        editor.apply()
    }

    private fun loadValuesFromSharedPreferences() {
        val gangguanCheckedId = sharedPreferences.getInt("gangguan", -1)
        if (gangguanCheckedId != -1) {
            rgGangguan.check(gangguanCheckedId)
        }

        val karakteristikTapakValue = sharedPreferences.getString("karakteristikTapak", "")
        val karakteristikTapakPosition = resources.getStringArray(R.array.KarakteristikTapak).indexOf(karakteristikTapakValue)
        sKarakteristikTapak.setSelection(if (karakteristikTapakPosition != -1) karakteristikTapakPosition else 0)

        val masalahTanahValue = sharedPreferences.getString("masalahTanah", "")
        val masalahTanahPosition = resources.getStringArray(R.array.MasalahTanah).indexOf(masalahTanahValue)
        sMasalahTanah.setSelection(if (masalahTanahPosition != -1) masalahTanahPosition else 0)

        val gangguanLainnyaValue = sharedPreferences.getString("gangguanLainnya", "")
        val gangguanLainnyaPosition = resources.getStringArray(R.array.GangguanLainnya).indexOf(gangguanLainnyaValue)
        sGangguanLainnya.setSelection(if (gangguanLainnyaPosition != -1) gangguanLainnyaPosition else 0)
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        saveValuesToSharedPreferences()
        val intent = Intent(this, KerusakanPohonActivity::class.java)
        intent.putExtra("idPohon",id_pohon)
        intent.putExtra("nomor",nomorPohon)
        intent.putExtra("responseCode",codeResponse)
        startActivity(intent)
        finish()
    }
}