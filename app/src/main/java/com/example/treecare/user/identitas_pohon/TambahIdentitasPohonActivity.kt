package com.example.treecare.user.identitas_pohon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import com.example.treecare.R
import com.example.treecare.user.MainActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class TambahIdentitasPohonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_identitas_pohon)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val mapView: MapView = findViewById(R.id.map)
        val sPemilik: Spinner = findViewById(R.id.sPemilik)
        val etPemilikLainnya: EditText = findViewById(R.id.etPemilikLainnya)

        sPemilik.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = sPemilik.selectedItem.toString()
                if(selectedItem == "Lainnya"){
                    etPemilikLainnya.visibility = View.VISIBLE
                }else{
                    etPemilikLainnya.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.controller.setZoom(17.0)
        mapView.controller.setCenter(GeoPoint(-6.5971,106.8060))

        btnBack.setOnClickListener {
            val intent = Intent(this, PengamatanVisualActivity::class.java)
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