package com.example.treecare.user.kerusakan_pohon

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treecare.R
import com.example.treecare.adapter.ImageAdapter
import com.example.treecare.interfaces.ImageInterface
import com.example.treecare.user.kesehatan_pohon.TambahKesehatanPohonActivity
import com.example.treecare.user.kondisi_tapak.TambahKondisiTapakActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity

class TambahKerusakanPohonActivity : AppCompatActivity(), ImageInterface {

    val PICK_IMAGE_REQUEST = 1
    private val selectedImages = ArrayList<Uri>()
    val MAX_IMAGES = 5
    private lateinit var rvImage: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_kerusakan_pohon)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnSimpan: AppCompatButton = findViewById(R.id.btnSimpan)
        val btnSelanjutnya: AppCompatButton = findViewById(R.id.btnSelanjutnya)
        val cvFoto: CardView = findViewById(R.id.cvFoto)
        rvImage = findViewById(R.id.rvImage)
        rvImage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvImage.adapter = ImageAdapter(this, selectedImages, this)

        cvFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, KerusakanPohonActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSimpan.setOnClickListener {
            val intent = Intent(this, KerusakanPohonActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSelanjutnya.setOnClickListener {
            val intent = Intent(this, TambahKondisiTapakActivity::class.java)
            startActivity(intent)
            finish()
        }

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
        val intent = Intent(this, KerusakanPohonActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onItemClick(position: Int) {
        selectedImages.removeAt(position)
        updateRecyclerView()
    }
}