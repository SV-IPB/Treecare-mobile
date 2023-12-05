package com.example.treecare.user

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import com.example.treecare.R
import com.example.treecare.user.karakteristik_pohon.DetailKarakteristikPohonActivity
import com.example.treecare.user.kerusakan_pohon.ListKerusakanPohonActivity
import com.example.treecare.user.kesehatan_pohon.DetailKesehatanPohonActivity
import com.example.treecare.user.kondisi_tapak.DetailKondisiTapakActivity
import com.example.treecare.user.kondisi_tapak.TambahKondisiTapakActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity
import com.example.treecare.user.target.DetailTargetActivity

class RiwayatPengamatanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_pengamatan)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnKarakteristik: AppCompatButton = findViewById(R.id.btnKarakteristik)
        val btnKesehatan: AppCompatButton = findViewById(R.id.btnKesehatan)
        val btnKerusakan: AppCompatButton = findViewById(R.id.btnKerusakan)
        val btnKondisi: AppCompatButton = findViewById(R.id.btnKondisi)
        val btnTarget: AppCompatButton = findViewById(R.id.btnTarget)

        btnBack.setOnClickListener {
            val intent = Intent(this, PengamatanVisualActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnKarakteristik.setOnClickListener {
            val intent = Intent(this, DetailKarakteristikPohonActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnKesehatan.setOnClickListener {
            val intent = Intent(this, DetailKesehatanPohonActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnKerusakan.setOnClickListener {
            val intent = Intent(this, ListKerusakanPohonActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnKondisi.setOnClickListener {
            val intent = Intent(this, DetailKondisiTapakActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnTarget.setOnClickListener {
            val intent = Intent(this, DetailTargetActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, PengamatanVisualActivity::class.java)
        startActivity(intent)
        finish()
    }
}