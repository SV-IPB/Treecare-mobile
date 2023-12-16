package com.example.treecare.user

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.example.treecare.R
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.PohonService
import com.example.treecare.service.api.v1.RetrofitHelperV1
import com.example.treecare.service.api.v1.TokenAuthenticator
import com.example.treecare.service.api.v1.response.AllIdentitasPohonResponse
import com.example.treecare.service.api.v1.response.IdentitasPohonResponse
import com.example.treecare.service.model.IdentitasPohonModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class MapPohonActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var mapView: MapView
    private lateinit var fab: FloatingActionButton
    private lateinit var cvItem: CardView
    private lateinit var ivImage: ImageView
    private lateinit var tvNoPohon: TextView
    private lateinit var tvProyek: TextView
    private lateinit var tvLocation: TextView
    private lateinit var searchEditText: EditText
    private lateinit var searchIcon: ImageView

    private lateinit var preferenceManager: PreferenceManager
    private val listPohon = ArrayList<IdentitasPohonModel>()

    private var Latitude by Delegates.notNull<Double>()
    private var Longitude by Delegates.notNull<Double>()
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private var marker: Marker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_pohon)

        preferenceManager = PreferenceManager(this)

        mapView = findViewById(R.id.mapView)
        btnBack = findViewById(R.id.btnBack)
        fab = findViewById(R.id.fab)
        cvItem = findViewById(R.id.cvItem)
        ivImage = findViewById(R.id.ivImage)
        tvNoPohon = findViewById(R.id.tvNoPohon)
        tvProyek = findViewById(R.id.tvProyek)
        tvLocation = findViewById(R.id.tvLocation)
        searchEditText = findViewById(R.id.searchEditText)
        searchIcon = findViewById(R.id.searchIcon)

        searchEditText.imeOptions = EditorInfo.IME_ACTION_DONE
        searchEditText.setRawInputType(InputType.TYPE_CLASS_TEXT)

        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.controller.setZoom(17.0)
        mapView.controller.setCenter(GeoPoint(-6.5971,106.8060))

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {}
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        getAllPohon()
        getCurrentLocation()

        fab.setOnClickListener {
            getCurrentLocation()
        }

        btnBack.setOnClickListener {
            finish()
        }

        mapView.setOnClickListener {
            cvItem.visibility = View.GONE
        }
        searchIcon.setOnClickListener {
            searchPohon()
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchPohon()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun getAllPohon(){
        val token = preferenceManager.getAccessToken()
        val tokenAuthenticator = TokenAuthenticator(preferenceManager)
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .build()
        val Retro = RetrofitHelperV1()
            .getApiClientAuth(okHttpClient)
            .create(PohonService::class.java)

        Retro.getAllPohon(token).enqueue(object : Callback<AllIdentitasPohonResponse> {
            override fun onResponse(call: Call<AllIdentitasPohonResponse>, response: Response<AllIdentitasPohonResponse>) {
                Log.e("Raw Response: ", response.raw().toString())
                val gson = GsonBuilder().setPrettyPrinting().create()
                val body = response.body()
                val responseBody = gson.toJson(body)
                Log.e("Body: ", responseBody)

                if (response.isSuccessful) {
                    for (pohon in body?.data?.datas!!){
                        val newPohon = IdentitasPohonModel()
                        newPohon.id = pohon.id
                        newPohon.nomorPohon = pohon.nomorPohon
                        newPohon.gambar = pohon.gambar
                        newPohon.namaProjek = pohon.namaProyek
                        newPohon.latitude = pohon.latitude
                        newPohon.longitude = pohon.longitude

                        listPohon.add(newPohon)
                        addMarkerForPohon(newPohon)
                    }
                } else {
                    Log.e("Error: ","network or API call failure")
                }

            }
            override fun onFailure(call: Call<AllIdentitasPohonResponse>, t: Throwable) {
                Log.e("Network API Error: ", t.message.toString())
                Log.e("Error: ","network or API call failure")
            }
        })
    }

    private fun getCurrentLocation(){
        if (checkLocationPermissions()) {
            requestLocationUpdates()
            getLastKnownLocation()
        } else {

        }
        currentLocation()
    }

    private fun checkLocationPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
            return false
        }
        return true
    }

    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIME_BETWEEN_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES,
            locationListener
        )
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            lastKnownLocation?.let {
                updateLocation(it)
            }
        }
    }

    fun currentLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                updateLocation(location)
            }
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
    }

    private fun updateLocation(location: Location) {
        Longitude = location.longitude
        Latitude = location.latitude

        marker?.let {
            mapView.overlays.remove(it)
        }

        marker = Marker(mapView)
        val markerPosition = GeoPoint(Latitude, Longitude)
        marker?.position = markerPosition
        mapView.overlays.add(marker)

        mapView.controller.animateTo(markerPosition)
    }

    private fun addMarkerForPohon(pohon: IdentitasPohonModel) {
        val marker = Marker(mapView)
        val markerPosition = pohon.latitude?.toDouble()
            ?.let { pohon.longitude?.toDouble()?.let { it1 -> GeoPoint(it, it1) } }
        marker.position = markerPosition

        val drawable = resources.getDrawable(R.drawable.ic_map_marker)
        marker.icon = drawable
        marker.setAnchor(0.5f, 1.0f)

        marker.setOnMarkerClickListener { marker, mapView ->
            showPohonDetails(pohon)
            true
        }

        mapView.overlays.add(marker)
    }

    private fun showPohonDetails(pohon: IdentitasPohonModel) {
        cvItem.visibility = View.VISIBLE

        Picasso.get().load(pohon.gambar).into(ivImage)

        tvNoPohon.text = pohon.nomorPohon
        tvProyek.text = pohon.namaProjek
        tvLocation.text = "${pohon.latitude}, ${pohon.longitude}"
    }

    private fun searchPohon() {
        val searchQuery = searchEditText.text.toString().trim()
        for (pohon in listPohon) {
            if (pohon.nomorPohon.equals(searchQuery, ignoreCase = true)) {
                cvItem.visibility = View.VISIBLE
                animateToPohonPosition(pohon)
                hideKeyboard()
                return
            }
        }
        Toast.makeText(this, "Pohon not found", Toast.LENGTH_SHORT).show()
    }

    private fun animateToPohonPosition(pohon: IdentitasPohonModel) {
        val markerPosition = GeoPoint(pohon.latitude!!.toDouble(), pohon.longitude!!.toDouble())
        mapView.controller.animateTo(markerPosition)
        showPohonDetails(pohon)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
        private const val MIN_TIME_BETWEEN_UPDATES = 1000L
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 50.0f
    }
}