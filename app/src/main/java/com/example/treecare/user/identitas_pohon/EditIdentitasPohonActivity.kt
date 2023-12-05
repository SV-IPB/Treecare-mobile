package com.example.treecare.user.identitas_pohon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.treecare.R
import com.example.treecare.service.PreferenceManager
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class EditIdentitasPohonActivity : AppCompatActivity() {


    private lateinit var preferenceManager: PreferenceManager
    private lateinit var nomor: String
    private lateinit var codeResponse: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_identitas_pohon)

        preferenceManager = PreferenceManager(this)

        nomor = intent.getStringExtra("nomor").toString()
        codeResponse = intent.getStringExtra("responseCode").toString()

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val mapView: MapView = findViewById(R.id.map)

        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.controller.setZoom(17.0)
        mapView.controller.setCenter(GeoPoint(-6.5971,106.8060))

        btnBack.setOnClickListener {
            val intent = Intent(this, DetailIndentitasPohonActivity::class.java)
            intent.putExtra("nomor", nomor)
            intent.putExtra("responseCode", codeResponse)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, DetailIndentitasPohonActivity::class.java)
        intent.putExtra("nomor", nomor)
        intent.putExtra("responseCode", codeResponse)
        startActivity(intent)
        finish()
    }
}