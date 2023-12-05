package com.example.treecare.user.identitas_pohon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import com.example.treecare.R
import com.example.treecare.user.RiwayatPerubahanActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class DetailIndentitasPohonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_indentitas_pohon)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnUbah: AppCompatButton = findViewById(R.id.btnUbah)
        val btnHistory: ImageView = findViewById(R.id.btnHistory)
        val mapView: MapView = findViewById(R.id.map)

        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.controller.setZoom(20.0)
        mapView.controller.setCenter(GeoPoint(-6.5971,106.8060))

        btnBack.setOnClickListener {
            val intent = Intent(this, PengamatanVisualActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnUbah.setOnClickListener {
            val intent = Intent(this, EditIdentitasPohonActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnHistory.setOnClickListener {
            val intent = Intent(this, RiwayatPerubahanActivity::class.java)
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