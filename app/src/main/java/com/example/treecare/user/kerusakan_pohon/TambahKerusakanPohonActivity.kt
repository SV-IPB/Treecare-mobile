package com.example.treecare.user.kerusakan_pohon

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import android.util.Base64
import android.widget.TextView
import com.example.treecare.service.KerusakanPreferenceManager
import com.example.treecare.service.model.RiwayatKerusakanPohonModel


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
    private lateinit var kerusakanPreferenceManager: KerusakanPreferenceManager
    private val listKerusakan: ArrayList<RiwayatKerusakanPohonModel> = ArrayList()
    private var base64Image: String? = null
    private var listBase64Image: List<String>? = null

    private lateinit var counterPreferences: SharedPreferences
    private var counter:Int = -1
    private var rvCounter:Int = -1

    private lateinit var BagianPohon: String
    private lateinit var PotensiKegagalan: String
    private lateinit var UkuranBagian: String
    private lateinit var PeringkatTarget: String
    private lateinit var Pemangkasan: String
    private lateinit var DeskripsiKerusakan: String
    private lateinit var DeskripsiPemangkasan: String
    private lateinit var SaranLainnya: String
    private var KeperluanTindakan: Boolean = true
    private var Dipindahkan: Boolean = true
    private var TDipindahkan: Boolean = true
    private var PotensiKegagalanInt: Int = 1
    private var UkuranBagianInt: Int = 1
    private var PeringkatTargetInt: Int = 1

    private val datetime: String
        get() {
            val sdf = SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.getDefault())
            return sdf.format(Date())
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_kerusakan_pohon)

        preferenceManager = PreferenceManager(this)
        kerusakanPreferenceManager = KerusakanPreferenceManager(this)
        counterPreferences = getSharedPreferences("kerusakanCounter", Context.MODE_PRIVATE)

        counter = counterPreferences.getInt("counter",-1)
        Log.e("Debug counter awal: ", counter.toString())

        rvCounter = intent.getIntExtra("rvCounter", -1)
        Log.e("Debug rvCounter awal: ", rvCounter.toString())

        sharedPreferences = getSharedPreferences("KerusakanPohon", Context.MODE_PRIVATE)

        id_pohon = intent.getStringExtra("idPohon").toString()
        nomorPohon = intent.getStringExtra("nomor").toString()
        codeResponse = intent.getStringExtra("responseCode").toString()

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

        val tvKerusakan: TextView = findViewById(R.id.tvKerusakan)
        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnSimpan: AppCompatButton = findViewById(R.id.btnSimpan)
        val btnSelanjutnya: AppCompatButton = findViewById(R.id.btnSelanjutnya)
        val cvFoto: CardView = findViewById(R.id.cvFoto)
        rvImage = findViewById(R.id.rvImage)

        if (rvCounter > 0){
            tvKerusakan.text = "Kerusakan-$rvCounter"
            Log.e("Debug rvCounter tambah","rvCounter : $rvCounter")
            loadValuesFromListKerusakanPohon(rvCounter)
        }else{
            if (counter >= 0){
                addCounter()
                counter = counterPreferences.getInt("counter",-1)
                tvKerusakan.text = "Kerusakan-$counter"
                Log.e("Debug counter tambah",counter.toString())
                loadValuesFromSharedPreferences()
            }else{
                Log.e("Debug counter tambah","counter < 0 : $counter")
            }
        }

        rvImage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvImage.adapter = ImageAdapter(this, selectedImages, this)

        cvFoto.setOnClickListener {
            showChooserDialog()
        }

        btnBack.setOnClickListener {
            if (rvCounter > 0){
                clearSharedPreferences()
                saveValuesToListKerusakan(rvCounter)
                val intent = Intent(this, KerusakanPohonActivity::class.java)
                intent.putExtra("idPohon",id_pohon)
                intent.putExtra("nomor",nomorPohon)
                intent.putExtra("responseCode",codeResponse)
                startActivity(intent)
                finish()
                Log.e("Debug rvCounter back","rvCounter : $rvCounter")
            }else{
                if (counter >= 0){
                    decreaseCounter()
                    saveValuesToSharedPreferences()
                    val intent = Intent(this, KerusakanPohonActivity::class.java)
                    intent.putExtra("idPohon",id_pohon)
                    intent.putExtra("nomor",nomorPohon)
                    intent.putExtra("responseCode",codeResponse)
                    startActivity(intent)
                    finish()
                }else{
                    Log.e("Debug counter back","counter < 0 : $counter")
                }
            }
        }

        btnSimpan.setOnClickListener {
            if (rvCounter > 0){
                clearSharedPreferences()
                saveValuesToListKerusakan(rvCounter)
                Log.e("Debug rvCounter simpan","rvCounter : $rvCounter")
            }else{
                if (counter >= 0){
                    clearSharedPreferences()
                    saveValuesToListKerusakan(counter)
                    val intent = Intent(this, KerusakanPohonActivity::class.java)
                    intent.putExtra("idPohon",id_pohon)
                    intent.putExtra("nomor",nomorPohon)
                    intent.putExtra("responseCode",codeResponse)
                    startActivity(intent)
                    finish()
                }else{
                    Log.e("Debug counter simpan","counter < 0 : $counter")
                }
            }
        }

        btnSelanjutnya.setOnClickListener {
            if (rvCounter > 0){
                clearSharedPreferences()
                saveValuesToListKerusakan(rvCounter)
                Log.e("Debug rvCounter selanjutnya","rvCounter : $rvCounter")
            }else{
                if (counter >= 0){
                    clearSharedPreferences()
                    saveValuesToListKerusakan(counter)
                    val intent = Intent(this, TambahKondisiTapakActivity::class.java)
                    intent.putExtra("idPohon",id_pohon)
                    intent.putExtra("nomor",nomorPohon)
                    intent.putExtra("responseCode",codeResponse)
                    startActivity(intent)
                    finish()
                }else{
                    Log.e("Debug counter selanjutnya","counter < 0 : $counter")
                }
            }
        }
    }

    private fun clearSharedPreferences(){
        val editor = sharedPreferences.edit()
        editor.clear().apply()
    }

    private fun addCounter(){
        val editor = counterPreferences.edit()
        editor.putInt("counter",counter+1)
        editor.apply()
        Log.e("Debug counter simpan add: ", counterPreferences.getInt("counter",-1).toString())
    }

    private fun decreaseCounter(){
        val editor = counterPreferences.edit()
        editor.putInt("counter",counter-1)
        editor.apply()
        Log.e("Debug counter simpan decrease: ", counterPreferences.getInt("counter",-1).toString())
    }

    private fun saveValues(){
        BagianPohon = sBagianPohon.selectedItem.toString()
        PotensiKegagalan = sPotensiKegagalan.selectedItem.toString()
        UkuranBagian = sUkuranBagian.selectedItem.toString()
        PeringkatTarget = sPeringkatTarget.selectedItem.toString()
        Pemangkasan = sPemangkasan.selectedItem.toString()
        DeskripsiKerusakan = etDeskripsiKerusakan.text.toString()
        DeskripsiPemangkasan = etDeskripsiPemangkasan.text.toString()
        SaranLainnya = etSaranLainnya.text.toString()

        PotensiKegagalanInt = sPotensiKegagalan.selectedItemPosition + 1
        UkuranBagianInt = sUkuranBagian.selectedItemPosition + 1
        PeringkatTargetInt = sPeringkatTarget.selectedItemPosition + 1

        KeperluanTindakan = when (rgKeperluanTindakan.checkedRadioButtonId) {
            R.id.rbYaTindakan -> true
            R.id.rbTidakTindakan -> false
            else -> {
                false
            }
        }
        Dipindahkan = when (rgDipindahkan.checkedRadioButtonId) {
            R.id.rbYaDipindahkan -> true
            R.id.rbTidakDipindahkan -> false
            else -> {
                false
            }
        }
        TDipindahkan = when (rgTDipindahkan.checkedRadioButtonId) {
            R.id.rbYaTDipindahkan -> true
            R.id.rbTidakTDipindahkan -> false
            else -> {
                false
            }
        }
    }

    private fun saveValuesToListKerusakan(newCounter: Int){
        saveValues()

        for ((index, imageUri) in selectedImages.withIndex()) {
            base64Image = uriToBase64(this, imageUri)
            listBase64Image = (listBase64Image ?: emptyList()) + listOf(base64Image.toString())
            Log.e("Debug img save: ", "image_$index")
        }

        listKerusakan.add(RiwayatKerusakanPohonModel(
            null,
            null,
            listBase64Image,
            BagianPohon,
            DeskripsiKerusakan,
            PotensiKegagalanInt,
            UkuranBagianInt,
            PeringkatTargetInt,
            null,
            KeperluanTindakan,
            Pemangkasan,
            Dipindahkan,
            TDipindahkan,
            SaranLainnya
        ))

//        listBase64Image?.let {
//            for ((index, image) in it.withIndex()) {
//                Log.e("listBase64Image $index", " $image ")
//            }
//        }

        for (item in listKerusakan) {
            Log.e("KerusakanValues", "BagianPohon: ${item.bagianPohon}")
            Log.e("KerusakanValues", "DeskripsiKerusakan: ${item.deksripsi}")
            Log.e("KerusakanValues", "PotensiKegagalanInt: ${item.potensiKegagalan}")
            Log.e("KerusakanValues", "UkuranBagianInt: ${item.ukuranBagianPohon}")
            Log.e("KerusakanValues", "PeringkatTargetInt: ${item.peringkatTarget}")
            Log.e("KerusakanValues", "KeperluanTindakan: ${item.butuhTindakan}")
            Log.e("KerusakanValues", "Pemangkasan: ${item.pemangkasan}")
            Log.e("KerusakanValues", "Dipindahkan: ${item.pohonDipindahkan}")
            Log.e("KerusakanValues", "TDipindahkan: ${item.targetDipindahkan}")
            Log.e("KerusakanValues", "SaranLainnya: ${item.saran}")
        }
        kerusakanPreferenceManager.setList("kerusakan-$newCounter",listKerusakan)

        val riwayatKerusakanList = kerusakanPreferenceManager.getList("kerusakan-$newCounter")

        if (riwayatKerusakanList != null) {
            for (riwayatKerusakan in riwayatKerusakanList) {
                Log.e("Debug KerusakanData", "RiwayatKerusakan-$newCounter: $riwayatKerusakan")
            }
        } else {
            Log.e("Debug KerusakanData", "RiwayatKerusakan list is null")
        }
    }

    private fun saveValuesToSharedPreferences() {
        saveValues()
        val editor = sharedPreferences.edit()
        editor.putString("bagianPohon", BagianPohon)
        editor.putString("potensiKegagalan", PotensiKegagalan)
        editor.putString("ukuranBagian", UkuranBagian)
        editor.putString("peringkatTarget", PeringkatTarget)
        editor.putString("pemangkasan", Pemangkasan)
        editor.putString("deskripsiKerusakan", DeskripsiKerusakan)
        editor.putString("deskripsiPemangkasan", DeskripsiPemangkasan)
        editor.putString("saranLainnya", SaranLainnya)
        editor.putInt("keperluanTindakan", if (rgKeperluanTindakan.checkedRadioButtonId != -1) rgKeperluanTindakan.checkedRadioButtonId else -1)
        editor.putInt("dipindahkan", if (rgDipindahkan.checkedRadioButtonId != -1) rgDipindahkan.checkedRadioButtonId else -1)
        editor.putInt("tDipindahkan", if (rgTDipindahkan.checkedRadioButtonId != -1) rgTDipindahkan.checkedRadioButtonId else -1)

        for ((index, imageUri) in selectedImages.withIndex()) {
            base64Image = uriToBase64(this, imageUri)
            editor.putString("image_$index", base64Image)
            listBase64Image = (listBase64Image ?: emptyList()) + listOf(base64Image.toString())
            Log.e("Debug img save: ", "image_$index")
        }

//        listBase64Image?.let { list ->
//            Log.e("Debug listBase64Image: ", "List size: ${list.size}")
//            for ((index, base64Image) in list.withIndex()) {
//                Log.e("Debug listBase64Image: ", "image_$index: $base64Image")
//            }
//        } ?: Log.e("Debug listBase64Image: ", "List is null or empty")

        editor.putInt("image_count", selectedImages.size)

        editor.apply()
    }

    private fun loadValuesFromListKerusakanPohon(newCounter: Int){
        val riwayatKerusakanList = kerusakanPreferenceManager.getList("kerusakan-$newCounter")

        if (riwayatKerusakanList != null) {
            for (riwayatKerusakan in riwayatKerusakanList) {
                Log.e("Debug load KerusakanData", "RiwayatKerusakan-$newCounter: $riwayatKerusakan")

                val bagianPohonValue = riwayatKerusakan?.bagianPohon
                val bagianPohonPosition = resources.getStringArray(R.array.BagianPohon).indexOf(bagianPohonValue)
                sBagianPohon.setSelection(if (bagianPohonPosition != -1) bagianPohonPosition else 0)

                val potensiKegagalanValue = riwayatKerusakan?.potensiKegagalan?.minus(1)
                sPotensiKegagalan.setSelection(potensiKegagalanValue ?: 0)

                val ukuranBagianValue = riwayatKerusakan?.ukuranBagianPohon?.minus(1)
                sUkuranBagian.setSelection(ukuranBagianValue ?: 0)

                val peringkatTargetValue = riwayatKerusakan?.peringkatTarget?.minus(1)
                sPeringkatTarget.setSelection(peringkatTargetValue ?: 0)

                val pemangkasanValue = riwayatKerusakan?.pemangkasan
                val pemangkasanPosition = resources.getStringArray(R.array.Pemangkasan).indexOf(pemangkasanValue)
                sPemangkasan.setSelection(if (pemangkasanPosition != -1) pemangkasanPosition else 0)

                if (riwayatKerusakan?.butuhTindakan == true) {
                    rbYaTindakan.isChecked = true
                } else {
                    rbTidakTindakan.isChecked = true
                }

                if (riwayatKerusakan?.pohonDipindahkan == true) {
                    rbYaDipindahkan.isChecked = true
                } else {
                    rbTidakDipindahkan.isChecked = true
                }

                if (riwayatKerusakan?.targetDipindahkan == true) {
                    rbYaTDipindahkan.isChecked = true
                } else {
                    rbTidakTDipindahkan.isChecked = true
                }

                etDeskripsiKerusakan.setText(riwayatKerusakan?.deksripsi)
                //etDeskripsiPemangkasan.setText(riwayatKerusakan?.deskripsiPemangkasan)
                etSaranLainnya.setText(riwayatKerusakan?.saran)

                selectedImages.clear()
                val gambarKerusakan = riwayatKerusakan?.gambarKerusakan
                gambarKerusakan?.let {
                    for ((index, image) in it.withIndex()) {
                        val imageUri = base64ToUri(image)
                        if (imageUri != null) {
                            selectedImages.add(imageUri)
                            Log.e("Debug gambarKerusakan $index", "$imageUri success add to recyclerview")
                        } else {
                            Log.e("Debug gambarKerusakan", "imageUri kerusakan is null or empty")
                        }
                    }
                }

            }
        } else {
            Log.e("Debug load KerusakanData", "RiwayatKerusakan list is null")
        }
    }

    private fun loadValuesFromSharedPreferences(){
        val bagianPohonValue = sharedPreferences.getString("bagianPohon", "")
        val bagianPohonPosition = resources.getStringArray(R.array.BagianPohon).indexOf(bagianPohonValue)
        sBagianPohon.setSelection(if (bagianPohonPosition != -1) bagianPohonPosition else 0)

        val potensiKegagalanValue = sharedPreferences.getString("potensiKegagalan", "")
        val potensiKegagalanPosition = resources.getStringArray(R.array.PotensiKegagalan).indexOf(potensiKegagalanValue)
        sPotensiKegagalan.setSelection(if (potensiKegagalanPosition != -1) potensiKegagalanPosition else 0)

        val ukuranBagianValue = sharedPreferences.getString("ukuranBagian", "")
        val ukuranBagianPosition = resources.getStringArray(R.array.UkuranBagian).indexOf(ukuranBagianValue)
        sUkuranBagian.setSelection(if (ukuranBagianPosition != -1) ukuranBagianPosition else 0)

        val peringkatTargetValue = sharedPreferences.getString("peringkatTarget", "")
        val peringkatTargetPosition = resources.getStringArray(R.array.PeringkatTarget).indexOf(peringkatTargetValue)
        sPeringkatTarget.setSelection(if (peringkatTargetPosition != -1) peringkatTargetPosition else 0)

        val pemangkasanValue = sharedPreferences.getString("pemangkasan", "")
        val pemangkasanPosition = resources.getStringArray(R.array.Pemangkasan).indexOf(pemangkasanValue)
        sPemangkasan.setSelection(if (pemangkasanPosition != -1) pemangkasanPosition else 0)

        val keperluanTindakanCheckedId = sharedPreferences.getInt("keperluanTindakan", -1)
        if (keperluanTindakanCheckedId != -1) {
            rgKeperluanTindakan.check(keperluanTindakanCheckedId)
        }

        val dipindahkanCheckedId = sharedPreferences.getInt("dipindahkan", -1)
        if (dipindahkanCheckedId != -1) {
            rgDipindahkan.check(dipindahkanCheckedId)
        }

        val tDipindahkanCheckedId = sharedPreferences.getInt("tDipindahkan", -1)
        if (tDipindahkanCheckedId != -1) {
            rgTDipindahkan.check(tDipindahkanCheckedId)
        }
        etDeskripsiKerusakan.setText(sharedPreferences.getString("deskripsiKerusakan", ""))
        etDeskripsiPemangkasan.setText(sharedPreferences.getString("deskripsiPemangkasan", ""))
        etSaranLainnya.setText(sharedPreferences.getString("saranLainnya", ""))

        selectedImages.clear()
        val imageCount = sharedPreferences.getInt("image_count", 0)
        for (index in 0 until imageCount) {
            val base64Image = sharedPreferences.getString("image_$index", null)
            if (base64Image.isNullOrBlank()) {
                break
            }
            Log.e("Debug img load: ", "image_$index")
            val imageUri = base64ToUri(base64Image)
            Log.e("Debug img load uri: ", imageUri.toString())
            if (imageUri != null) {
                selectedImages.add(imageUri)
            }
        }

        updateRecyclerView()

    }

    fun uriToBase64(context: Context, uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private fun base64ToUri(base64: String): Uri? {
        val bytes = Base64.decode(base64, Base64.DEFAULT)
        val directory = File(filesDir, "images")
        val file = File(directory, "Treecare_${datetime}.png")
        FileOutputStream(file).use { stream -> stream.write(bytes) }
        return FileProvider.getUriForFile(this, "com.example.treecare.fileprovider", file)
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
            cameraIntent.putExtra("idPohon",id_pohon)
            cameraIntent.putExtra("nomor",nomorPohon)
            cameraIntent.putExtra("responseCode",codeResponse)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            dialog.dismiss()
        }

        clGalery.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.putExtra("idPohon",id_pohon)
            galleryIntent.putExtra("nomor",nomorPohon)
            galleryIntent.putExtra("responseCode",codeResponse)
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
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