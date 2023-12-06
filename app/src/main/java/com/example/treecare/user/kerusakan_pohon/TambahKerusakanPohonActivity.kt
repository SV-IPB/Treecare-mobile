package com.example.treecare.user.kerusakan_pohon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treecare.R
import com.example.treecare.adapter.ImageAdapter
import com.example.treecare.interfaces.ImageInterface
import com.example.treecare.service.PreferenceManager
import com.example.treecare.user.kesehatan_pohon.TambahKesehatanPohonActivity
import com.example.treecare.user.kondisi_tapak.TambahKondisiTapakActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity

class TambahKerusakanPohonActivity : AppCompatActivity(), ImageInterface {

    private lateinit var sBagianPohon: Spinner
    private lateinit var sPotensiKegagalan: Spinner
    private lateinit var sUkuranBagian: Spinner
    private lateinit var sPeringkatTarget: Spinner
    private lateinit var sPemangkasan: Spinner
    private lateinit var etDeskripsiKerusakan: EditText
    private lateinit var etDeskripsiPemangkasan: EditText
    private lateinit var etSaranLainnya: EditText
    private lateinit var rgKeperluanTindakan: RadioGroup
    private lateinit var rbYaTindakan: RadioButton
    private lateinit var rbTidakTindakan: RadioButton
    private lateinit var rgDipindahkan: RadioGroup
    private lateinit var rbYaDipindahkan: RadioButton
    private lateinit var rbTidakDipindahkan: RadioButton
    private lateinit var rgTDipindahkan: RadioGroup
    private lateinit var rbYaTDipindahkan: RadioButton
    private lateinit var rbTidakTDipindahkan: RadioButton

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var id_pohon: String
    private lateinit var nomorPohon: String
    private lateinit var codeResponse: String

    val PICK_IMAGE_REQUEST = 1
    private val selectedImages = ArrayList<Uri>()
    val MAX_IMAGES = 5
    private lateinit var rvImage: RecyclerView

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_kerusakan_pohon)

        preferenceManager = PreferenceManager(this)

        sharedPreferences = getSharedPreferences("KerusakanPohon", Context.MODE_PRIVATE)

        id_pohon = intent.getStringExtra("idPohon").toString()
        nomorPohon = intent.getStringExtra("nomor").toString()
        codeResponse = intent.getStringExtra("responseCode").toString()
        Log.e("id pohon : ", id_pohon)
        Log.e("nomor pohon : ", nomorPohon)
        Log.e("code pohon : ", codeResponse)

        sBagianPohon = findViewById(R.id.sBagianPohon)
        sPotensiKegagalan = findViewById(R.id.sPotensiKegagalan)
        sUkuranBagian = findViewById(R.id.sUkuranBagian)
        sPeringkatTarget = findViewById(R.id.sPeringkatTarget)
        sPemangkasan = findViewById(R.id.sPemangkasan)
        etDeskripsiKerusakan = findViewById(R.id.etDeskripsiKerusakan)
        etDeskripsiPemangkasan = findViewById(R.id.etDeskripsiPemangkasan)
        etSaranLainnya = findViewById(R.id.etSaranLainnya)
        rgKeperluanTindakan = findViewById(R.id.rgKeperluanTindakan)
        rbYaTindakan = findViewById(R.id.rbYaTindakan)
        rbTidakTindakan = findViewById(R.id.rbTidakTindakan)
        rgDipindahkan = findViewById(R.id.rgDipindahkan)
        rbYaDipindahkan = findViewById(R.id.rbYaDipindahkan)
        rbTidakDipindahkan = findViewById(R.id.rbTidakDipindahkan)
        rgTDipindahkan = findViewById(R.id.rgTDipindahkan)
        rbYaTDipindahkan = findViewById(R.id.rbYaTDipindahkan)
        rbTidakTDipindahkan = findViewById(R.id.rbTidakTDipindahkan)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnSimpan: AppCompatButton = findViewById(R.id.btnSimpan)
        val btnSelanjutnya: AppCompatButton = findViewById(R.id.btnSelanjutnya)
        val cvFoto: CardView = findViewById(R.id.cvFoto)
        rvImage = findViewById(R.id.rvImage)
        rvImage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvImage.adapter = ImageAdapter(this, selectedImages, this)

        sBagianPohon.setSelection(sharedPreferences.getInt("bagianPohon", 0))
        sPotensiKegagalan.setSelection(sharedPreferences.getInt("potensiKegagalan", 0))
        sUkuranBagian.setSelection(sharedPreferences.getInt("ukuranBagian", 0))
        sPeringkatTarget.setSelection(sharedPreferences.getInt("peringkatTarget", 0))
        sPemangkasan.setSelection(sharedPreferences.getInt("pemangkasan", 0))
        rgKeperluanTindakan.check(sharedPreferences.getInt("keperluanTindakan", R.id.rbTidakTindakan))
        rgDipindahkan.check(sharedPreferences.getInt("dipindahkan", R.id.rbTidakDipindahkan))
        rgTDipindahkan.check(sharedPreferences.getInt("tDipindahkan", R.id.rbTidakTDipindahkan))
        etDeskripsiKerusakan.setText(sharedPreferences.getString("deskripsiKerusakan", ""))
        etDeskripsiPemangkasan.setText(sharedPreferences.getString("deskripsiPemangkasan", ""))
        etSaranLainnya.setText(sharedPreferences.getString("saranLainnya", ""))

        cvFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
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

        btnSimpan.setOnClickListener {
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
            val intent = Intent(this, TambahKondisiTapakActivity::class.java)
            intent.putExtra("idPohon",id_pohon)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivity(intent)
            finish()
        }

    }

    private fun saveValuesToSharedPreferences() {
        val editor = sharedPreferences.edit()

        editor.putInt("bagianPohon", sBagianPohon.selectedItemPosition)
        editor.putInt("potensiKegagalan", sPotensiKegagalan.selectedItemPosition)
        editor.putInt("ukuranBagian", sUkuranBagian.selectedItemPosition)
        editor.putInt("peringkatTarget", sPeringkatTarget.selectedItemPosition)
        editor.putInt("pemangkasan", sPemangkasan.selectedItemPosition)
        editor.putString("deskripsiKerusakan", etDeskripsiKerusakan.text.toString())
        editor.putString("deskripsiPemangkasan", etDeskripsiPemangkasan.text.toString())
        editor.putString("saranLainnya", etSaranLainnya.text.toString())
        editor.putInt("keperluanTindakan", rgKeperluanTindakan.checkedRadioButtonId)
        editor.putInt("dipindahkan", rgDipindahkan.checkedRadioButtonId)
        editor.putInt("tDipindahkan", rgTDipindahkan.checkedRadioButtonId)

        editor.apply()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImage: Uri? = data.data
            if (selectedImage != null && selectedImages.size < MAX_IMAGES) {
                selectedImages.add(selectedImage)
                updateRecyclerView()
            } else {
                Toast.makeText(this, "Maximum gambar $MAX_IMAGES", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecyclerView() {
        rvImage.adapter?.notifyDataSetChanged()
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

    override fun onItemClick(position: Int) {
        selectedImages.removeAt(position)
        updateRecyclerView()
    }
}