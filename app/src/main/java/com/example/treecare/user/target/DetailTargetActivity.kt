package com.example.treecare.user.target

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.treecare.R
import com.example.treecare.user.RiwayatPengamatanActivity

class DetailTargetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_target)

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