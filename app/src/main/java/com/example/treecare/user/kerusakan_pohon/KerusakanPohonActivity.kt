package com.example.treecare.user.kerusakan_pohon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import com.example.treecare.R
import com.example.treecare.user.kesehatan_pohon.TambahKesehatanPohonActivity
import com.example.treecare.user.kondisi_tapak.TambahKondisiTapakActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity

class KerusakanPohonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kerusakan_pohon)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnSelanjutnya: AppCompatButton = findViewById(R.id.btnSelanjutnya)
        val btnTambahKerusakan: AppCompatButton = findViewById(R.id.btnTambahKerusakan)

        btnBack.setOnClickListener {
            val intent = Intent(this, TambahKesehatanPohonActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSelanjutnya.setOnClickListener {
            val intent = Intent(this, TambahKondisiTapakActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnTambahKerusakan.setOnClickListener {
            val intent = Intent(this, TambahKerusakanPohonActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, TambahKesehatanPohonActivity::class.java)
        startActivity(intent)
        finish()
    }
}