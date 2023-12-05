package com.example.treecare.user.kesehatan_pohon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import com.example.treecare.R
import com.example.treecare.user.karakteristik_pohon.TambahKarakteristikPohonActivity
import com.example.treecare.user.kerusakan_pohon.KerusakanPohonActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity

class TambahKesehatanPohonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_kesehatan_pohon)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnSelanjutnya: AppCompatButton = findViewById(R.id.btnSelanjutnya)

        btnBack.setOnClickListener {
            val intent = Intent(this, TambahKarakteristikPohonActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSelanjutnya.setOnClickListener {
            val intent = Intent(this, KerusakanPohonActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, TambahKarakteristikPohonActivity::class.java)
        startActivity(intent)
        finish()
    }
}