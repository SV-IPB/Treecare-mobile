package com.example.treecare.user.pengamatan_visual

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.treecare.R
import com.example.treecare.user.MainActivity

class PengamatanVisualActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengamatan_visual)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val tvPengamatanVisual: TextView = findViewById(R.id.tvPengamatanVisual)

        val nomorPohon = intent.getStringExtra("nomor")
        val codeResponse = intent.getStringExtra("responseCode")
        val bundle = Bundle().apply {
            putString("nomor", nomorPohon)
            putString("responseCode", codeResponse)
        }

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        if (codeResponse == "404"){
            val fragment = TidakAdaIdentitasFragment()
            fragment.arguments = bundle
            fragmentTransaction.replace(R.id.flContainer, fragment)
            fragmentTransaction.commit()
        }
        if (codeResponse == "200"){
            val fragment = AdaIdentitasFragment()
            fragment.arguments = bundle
            fragmentTransaction.replace(R.id.flContainer, fragment)
            fragmentTransaction.commit()
        }


        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}