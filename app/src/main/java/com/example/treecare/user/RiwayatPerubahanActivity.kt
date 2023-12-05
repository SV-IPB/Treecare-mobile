package com.example.treecare.user

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.treecare.R
import com.example.treecare.user.identitas_pohon.DetailIndentitasPohonActivity
import com.example.treecare.user.kondisi_tapak.TambahKondisiTapakActivity

class RiwayatPerubahanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_perubahan)

        val btnBack: ImageView = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, DetailIndentitasPohonActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, DetailIndentitasPohonActivity::class.java)
        startActivity(intent)
        finish()
    }
}