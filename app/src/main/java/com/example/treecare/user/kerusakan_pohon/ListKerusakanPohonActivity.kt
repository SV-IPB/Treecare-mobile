package com.example.treecare.user.kerusakan_pohon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.treecare.R
import com.example.treecare.user.RiwayatPengamatanActivity

class ListKerusakanPohonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_kerusakan_pohon)

        val btnBack: ImageView = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, RiwayatPengamatanActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, RiwayatPengamatanActivity::class.java)
        startActivity(intent)
        finish()
    }
}