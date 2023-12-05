package com.example.treecare.user.identitas_pohon

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.example.treecare.R
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.api.v1.PohonService
import com.example.treecare.service.api.v1.RetrofitHelperV1
import com.example.treecare.service.api.v1.TokenAuthenticator
import com.example.treecare.service.api.v1.request.IdentitasPohonRequest
import com.example.treecare.service.api.v1.response.IdentitasPohonResponse
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity
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
import java.io.ByteArrayOutputStream
import kotlin.properties.Delegates

class EditIdentitasPohonActivity : AppCompatActivity() {


    private lateinit var cvFoto: CardView
    private lateinit var ivFoto: ImageView
    private lateinit var etNomorPohon: TextView
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
    private lateinit var btnUbahFoto: AppCompatButton
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var nomor: String
    private lateinit var codeResponse: String
    private lateinit var id_pohon: String

    private var Latitude by Delegates.notNull<Double>()
    private var Longitude by Delegates.notNull<Double>()
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private var marker: Marker? = null

    private lateinit var base64String: String
    private lateinit var pemilik: String
    private lateinit var nilai_spesial: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_identitas_pohon)

        preferenceManager = PreferenceManager(this)

        nomor = intent.getStringExtra("nomor").toString()
        codeResponse = intent.getStringExtra("responseCode").toString()
        id_pohon = intent.getStringExtra("id").toString()
        //Toast.makeText(this,"id: $id_pohon",Toast.LENGTH_SHORT).show()
        //Log.e("Nomor 1:",nomor)

        getByNomorPohon()

        cvFoto = findViewById(R.id.cvFoto)
        ivFoto = findViewById(R.id.ivFoto)
        etNomorPohon = findViewById(R.id.etNomorPohon)
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
        btnUbahFoto = findViewById(R.id.btnUbahFoto)

        val btnBack: ImageView = findViewById(R.id.btnBack)

        sPemilik.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = sPemilik.selectedItem.toString()
                if(selectedItem == "Lainnya"){
                    etPemilikLainnya.visibility = View.VISIBLE
                    pemilik = etPemilikLainnya.text.toString()
                    //Toast.makeText(this@EditIdentitasPohonActivity,pemilik, Toast.LENGTH_SHORT).show()
                }else{
                    etPemilikLainnya.visibility = View.GONE
                    pemilik = selectedItem
                    //Toast.makeText(this@EditIdentitasPohonActivity,pemilik, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        sNilaiSpesial.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = sNilaiSpesial.selectedItem.toString()
                nilai_spesial = selectedItem
                //Toast.makeText(this@EditIdentitasPohonActivity,nilai_spesial, Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.controller.setZoom(17.0)
        mapView.controller.setCenter(GeoPoint(-6.5971,106.8060))

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {

            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }


        btnUbahFoto.setOnClickListener {
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            nomor = intent.getStringExtra("nomor").toString()
            codeResponse = intent.getStringExtra("responseCode").toString()
            cameraIntent.putExtra("nomor", nomor)
            cameraIntent.putExtra("responseCode", codeResponse)
            //Log.e("Nomor 2:", nomor)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        }

        btnPilihLokasi.setOnClickListener {
            if (checkLocationPermissions()) {
                requestLocationUpdates()
                getLastKnownLocation()
            } else {

            }
            currentLocation()
        }

        btnSimpan.setOnClickListener {
            updatePohon()
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, DetailIndentitasPohonActivity::class.java)
            intent.putExtra("nomor", nomor)
            intent.putExtra("responseCode", codeResponse)
            startActivity(intent)
            finish()
        }
    }

    fun updatePohon(){
        if (sPemilik.selectedItem.toString() == "Lainnya"){
            pemilik = etPemilikLainnya.text.toString()
        }
        val id = id_pohon
        val request = IdentitasPohonRequest()
        request.nomorPohon = nomor
        request.alamat = etAlamat.text.toString()
        request.latitude = Latitude.toFloat()
        request.longitude = Longitude.toFloat()
        request.namaProyek = etNamaProyek.text.toString()
        request.namaPemilik = pemilik
        request.jenisPohon = etJenisPohon.text.toString()
        request.nilaiSpesial = nilai_spesial
        request.gambar = "data:image/png;base64,$base64String"
        val token = preferenceManager.getAccessToken()
        val tokenAuthenticator = TokenAuthenticator(preferenceManager)
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .build()
        val Retro = RetrofitHelperV1().getApiClientAuth(okHttpClient).create(PohonService::class.java)
        Retro.updatePohon(id, request, token).enqueue(object : Callback<IdentitasPohonResponse> {
            override fun onResponse(call: Call<IdentitasPohonResponse>, response: Response<IdentitasPohonResponse>) {
                Log.e("Raw Response: ", response.raw().toString())
                val gson = GsonBuilder().setPrettyPrinting().create()
                val responseBody = gson.toJson(response.body())
                val responseCode = response.code()
                Log.e("Body: ", responseBody)

                val intent = Intent(this@EditIdentitasPohonActivity, DetailIndentitasPohonActivity::class.java)
                intent.putExtra("nomor", response.body()?.data?.nomorPohon.toString())
                //Log.e("Nomor 5:",response.body()?.data?.nomorPohon.toString())
                intent.putExtra("responseCode", responseCode.toString())
                startActivity(intent)
                finish()
            }

            override fun onFailure(call: Call<IdentitasPohonResponse>, t: Throwable) {
                Log.e("Network API Error: ", t.message.toString())
                Log.e("Error: ","network or API call failure")
            }
        })
    }

    private fun getByNomorPohon(){
        var nomorPohon = intent.getStringExtra("nomor")
        val token = preferenceManager.getAccessToken()
        val tokenAuthenticator = TokenAuthenticator(preferenceManager)
        val okHttpClient = OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .build()
        val Retro = RetrofitHelperV1().getApiClientAuth(okHttpClient).create(PohonService::class.java)
        Retro.getByNomorPohon(nomorPohon,token).enqueue(object : Callback<IdentitasPohonResponse> {
            override fun onResponse(call: Call<IdentitasPohonResponse>, response: Response<IdentitasPohonResponse>) {
                val gson = GsonBuilder().setPrettyPrinting().create()
                val responseBody = gson.toJson(response.body())
                val responseCode = response.code()
                Log.e("Body: ", responseBody)

                val latitude = response.body()?.data?.latitude
                val longitude = response.body()?.data?.longitude

                val imgUri = response.body()?.data?.gambar
                Picasso.get().invalidate(imgUri)
                Picasso.get().load(imgUri).into(ivFoto)

                etNomorPohon.text = response.body()?.data?.nomorPohon
                etAlamat.setText(response.body()?.data?.alamat)
                tvCoordinate.text = "$latitude, $longitude"
                etNamaProyek.setText(response.body()?.data?.namaProyek)

                val pemilikValue = response.body()?.data?.namaPemilik
                val pemilikPosition = getPemilikPosition(pemilikValue)
                sPemilik.setSelection(pemilikPosition)

                etJenisPohon.setText(response.body()?.data?.jenisPohon)

                val nilaiSpesialValue = response.body()?.data?.nilaiSpesial
                val nilaiSpesialPosition = getNilaiSpesialPosition(nilaiSpesialValue)
                sNilaiSpesial.setSelection(nilaiSpesialPosition)


            }
            override fun onFailure(call: Call<IdentitasPohonResponse>, t: Throwable) {
                Log.e("Network API Error: ", t.message.toString())
                Log.e("Error: ","network or API call failure")
            }
        })
    }

    private fun getPemilikPosition(value: String?): Int {
        val adapter = sPemilik.adapter
        if (adapter != null) {
            for (i in 0 until adapter.count) {
                if (adapter.getItem(i).toString() == value) {
                    return i
                } else{
                    pemilik = value.toString()
                    etPemilikLainnya.setText(value)
                    return adapter.count-1
                }
            }
        }
        return 0
    }

    private fun getNilaiSpesialPosition(value: String?): Int {
        val adapter = sNilaiSpesial.adapter
        if (adapter != null) {
            for (i in 0 until adapter.count) {
                if (adapter.getItem(i).toString() == value) {
                    return i
                }
            }
        }
        return 0
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val photo = data?.extras?.get("data") as Bitmap
            ivFoto.setImageBitmap(photo)
            base64String = convertBitmapToBase64(photo)
            //Log.e("64: ",base64String)
            //Log.e("Nomor 3: ", nomor)
        }
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

        tvCoordinate.text = "$Latitude, $Longitude"

        mapView.controller.animateTo(markerPosition)
    }

    fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, DetailIndentitasPohonActivity::class.java)
        intent.putExtra("nomor", nomor)
        intent.putExtra("responseCode", codeResponse)
        startActivity(intent)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("nomor", nomor)
        outState.putString("codeResponse", codeResponse)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        nomor = savedInstanceState.getString("nomor", "")
        codeResponse = savedInstanceState.getString("codeResponse", "")
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
        private const val MIN_TIME_BETWEEN_UPDATES = 1000L
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 50.0f

        private const val CAMERA_REQUEST_CODE = 1002
    }
}