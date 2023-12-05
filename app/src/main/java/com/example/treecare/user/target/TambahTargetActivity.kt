package com.example.treecare.user.target

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.treecare.LoginActivity
import com.example.treecare.R
import com.example.treecare.user.RiwayatPengamatanActivity
import com.example.treecare.user.kondisi_tapak.TambahKondisiTapakActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity

class TambahTargetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_target)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnSimpan: AppCompatButton = findViewById(R.id.btnSimpan)

        btnBack.setOnClickListener {
            val intent = Intent(this, TambahKondisiTapakActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSimpan.setOnClickListener {
            showConfirmDialog()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, TambahKondisiTapakActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showConfirmDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.setTitle("Confirm")
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val btnSimpan: AppCompatButton = dialog.findViewById(R.id.btnSimpan)
        val btnKembali: TextView = dialog.findViewById(R.id.btnKembali)

        btnSimpan.setOnClickListener {
            val intent = Intent(this, PengamatanVisualActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnKembali.setOnClickListener {
            dialog.dismiss()
        }

    }
}