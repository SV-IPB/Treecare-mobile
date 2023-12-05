package com.example.treecare.user.identitas_pohon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.treecare.R
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class EditIdentitasPohonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_identitas_pohon)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val mapView: MapView = findViewById(R.id.map)

        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.controller.setZoom(20.0)
        mapView.controller.setCenter(GeoPoint(-6.5971,106.8060))

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