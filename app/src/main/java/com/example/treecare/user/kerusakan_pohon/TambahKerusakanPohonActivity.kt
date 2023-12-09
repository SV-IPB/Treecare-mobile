package com.example.treecare.user.kerusakan_pohon

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treecare.R
import com.example.treecare.adapter.ImageAdapter
import com.example.treecare.interfaces.ImageInterface
import com.example.treecare.service.PreferenceManager
import com.example.treecare.user.kondisi_tapak.TambahKondisiTapakActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    private val PICK_IMAGE_REQUEST = 1003
    private val CAMERA_REQUEST_CODE = 1002
    private val selectedImages = ArrayList<Uri>()
    val MAX_IMAGES = 5
    private lateinit var rvImage: RecyclerView

    private lateinit var sharedPreferences: SharedPreferences

    private val datetime: String
        get() {
            val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            return sdf.format(Date())
        }
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
            showChooserDialog()
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
                Log.e("Debug", "Photo uri: $selectedImage")
                selectedImages.add(selectedImage)
                updateRecyclerView()
            } else {
                Toast.makeText(this, "Maximum gambar $MAX_IMAGES", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val photo: Bitmap? = data?.extras?.get("data") as? Bitmap

            val photoUri: Uri? = if (photo != null) {
                Log.e("Debug", "Photo is not null")
                saveBitmapToFile(photo)?.let { FileProvider.getUriForFile(this, "com.example.treecare.fileprovider", it) }
            } else {
                Log.e("Debug", "Photo is null, data?.data: ${data?.data}")
                data?.data
            }

            if (photoUri != null && selectedImages.size < MAX_IMAGES) {
                selectedImages.add(photoUri)
                updateRecyclerView()
            } else {
                Log.e("Debug", "Condition not met - photoUri: $photoUri, selectedImages.size: ${selectedImages.size}, MAX_IMAGES: $MAX_IMAGES")
                Toast.makeText(this, "Maximum gambar $MAX_IMAGES", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun saveBitmapToFile(bitmap: Bitmap): File? {
        val directory = File(filesDir, "images")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val fileDirectory = File(directory, "Treecare_${datetime}.png")

        try {
            val stream: OutputStream = FileOutputStream(fileDirectory)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Debug", "Error saving bitmap to file: ${e.message}")
            return null
        }

        return fileDirectory
    }



    fun showChooserDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_chooser)
        val clPhoto: ConstraintLayout = dialog.findViewById(R.id.clPhoto)
        val clGalery: ConstraintLayout = dialog.findViewById(R.id.clGalery)
        dialog.setTitle("Chooser")
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        clPhoto.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra("idPohon",id_pohon)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            dialog.dismiss()
        }

        clGalery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra("idPohon",id_pohon)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
            dialog.dismiss()
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