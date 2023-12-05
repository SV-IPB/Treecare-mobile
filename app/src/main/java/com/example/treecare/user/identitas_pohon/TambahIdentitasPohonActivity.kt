package com.example.treecare.user.identitas_pohon

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.treecare.R
import com.example.treecare.service.PreferenceManager
import com.example.treecare.user.MainActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem

class TambahIdentitasPohonActivity : AppCompatActivity() {

    private lateinit var cvFoto: CardView
    private lateinit var ivFoto: ImageView
    private lateinit var etAlamat: EditText
    private lateinit var mapView: MapView
    private lateinit var tvCoordinate: TextView
    private lateinit var btnPilihLokasi: AppCompatButton
    private lateinit var etNamaProyek: EditText
    private lateinit var sPemilik: Spinner
    private lateinit var etPemilikLainnya: EditText
    private lateinit var etJenisPohon: EditText
    private lateinit var sNilaiSpesial: Spinner
    private lateinit var btnSimpan: AppCompatButton
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var nomor: String
    private lateinit var codeResponse: String

    private val LOCATION_PERMISSION_CODE = 1001
    private lateinit var overlay: ItemizedIconOverlay<OverlayItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_identitas_pohon)

        preferenceManager = PreferenceManager(this)

        nomor = intent.getStringExtra("nomor").toString()
        codeResponse = intent.getStringExtra("responseCode").toString()

        cvFoto = findViewById(R.id.cvFoto)
        ivFoto = findViewById(R.id.ivFoto)
        etAlamat = findViewById(R.id.etAlamat)
        mapView = findViewById(R.id.map)
        tvCoordinate = findViewById(R.id.tvCoordinate)
        btnPilihLokasi = findViewById(R.id.btnPilihLokasi)
        etNamaProyek = findViewById(R.id.etNamaProyek)
        sPemilik = findViewById(R.id.sPemilik)
        etPemilikLainnya = findViewById(R.id.etPemilikLainnya)
        etJenisPohon = findViewById(R.id.etJenisPohon)
        sNilaiSpesial = findViewById(R.id.sNilaiSpesial)
        btnSimpan = findViewById(R.id.btnSimpan)

        val btnBack: ImageView = findViewById(R.id.btnBack)

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

        btnPilihLokasi.setOnClickListener {
            requestLocationUpdates()
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, PengamatanVisualActivity::class.java)
            intent.putExtra("nomor", nomor)
            intent.putExtra("responseCode", codeResponse)
            startActivity(intent)
            finish()
        }
    }

    private fun requestLocationUpdates() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0f,
                    locationListener
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_CODE
                )
            }
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val latitude = location.latitude
            val longitude = location.longitude
            tvCoordinate.text = "Latitude: $latitude, Longitude: $longitude"

            val geoPoint = GeoPoint(latitude, longitude)
            mapView.controller.setCenter(geoPoint)

            val marker = OverlayItem("Your Location", "Description", geoPoint)
            val markerIcon = ContextCompat.getDrawable(this@TambahIdentitasPohonActivity, R.drawable.marker_icon)
            marker.setMarker(markerIcon)

            if (!::overlay.isInitialized) {
                val overlayItems = ArrayList<OverlayItem>()
                overlay = ItemizedIconOverlay(overlayItems, markerIcon, null, this@TambahIdentitasPohonActivity)
                mapView.overlays.add(overlay)
            }

            overlay.removeAllItems()
            overlay.addItem(marker)

            val locationManager =
                getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.removeUpdates(this)
        }

        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, PengamatanVisualActivity::class.java)
        intent.putExtra("nomor", nomor)
        intent.putExtra("responseCode", codeResponse)
        startActivity(intent)
        finish()
    }
}