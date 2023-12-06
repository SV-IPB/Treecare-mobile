package com.example.treecare.user.target

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.treecare.LoginActivity
import com.example.treecare.R
import com.example.treecare.service.PreferenceManager
import com.example.treecare.user.RiwayatPengamatanActivity
import com.example.treecare.user.kondisi_tapak.TambahKondisiTapakActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity

class TambahTargetActivity : AppCompatActivity() {

    private lateinit var sPemanfaatanArea: Spinner
    private lateinit var rgDipindahkan: RadioGroup
    private lateinit var rbYaDipindahkan: RadioButton
    private lateinit var rbTidakDipindahkan: RadioButton
    private lateinit var rgDibatasi: RadioGroup
    private lateinit var rbYaDibatasi: RadioButton
    private lateinit var rbTidakDibatasi: RadioButton
    private lateinit var sHunian: Spinner

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
        setContentView(R.layout.activity_tambah_target)

        preferenceManager = PreferenceManager(this)

        sharedPreferences = getSharedPreferences("Target", Context.MODE_PRIVATE)
        sharedPreferences2 = getSharedPreferences("KarakteristikPohon", Context.MODE_PRIVATE)
        sharedPreferences3 = getSharedPreferences("KesehatanPohon", Context.MODE_PRIVATE)
        sharedPreferences4 = getSharedPreferences("KerusakanPohon", Context.MODE_PRIVATE)
        sharedPreferences5 = getSharedPreferences("KondisiTapak", Context.MODE_PRIVATE)

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

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnSimpan: AppCompatButton = findViewById(R.id.btnSimpan)

        sPemanfaatanArea.setSelection(sharedPreferences.getInt("pemanfaatanArea", 0))
        rgDipindahkan.check(sharedPreferences.getInt("dipindahkan", R.id.rbTidakDipindahkan))
        rgDibatasi.check(sharedPreferences.getInt("dibatasi", R.id.rbTidakDibatasi))
        sHunian.setSelection(sharedPreferences.getInt("hunian", 0))

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

    private fun saveValuesToSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.putInt("pemanfaatanArea", sPemanfaatanArea.selectedItemPosition)
        editor.putInt("dipindahkan", rgDipindahkan.checkedRadioButtonId)
        editor.putInt("dibatasi", rgDibatasi.checkedRadioButtonId)
        editor.putInt("hunian", sHunian.selectedItemPosition)
        editor.apply()
    }

    fun showConfirmDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.setTitle("Confirm")
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val btnSimpan: AppCompatButton = dialog.findViewById(R.id.btnSimpan)
        val btnKembali: TextView = dialog.findViewById(R.id.btnKembali)

        btnSimpan.setOnClickListener {
            clearSharedPreferences()
            val intent = Intent(this, PengamatanVisualActivity::class.java)
            intent.putExtra("idPohon",id_pohon)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivity(intent)
            finish()
        }

        btnKembali.setOnClickListener {
            dialog.dismiss()
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
    }
}