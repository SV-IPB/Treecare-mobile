package com.example.treecare.user.kerusakan_pohon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treecare.R
import com.example.treecare.adapter.KerusakanAdapter
import com.example.treecare.interfaces.KerusakanInterface
import com.example.treecare.service.KerusakanPreferenceManager
import com.example.treecare.service.PreferenceManager
import com.example.treecare.service.model.RiwayatKerusakanPohonModel
import com.example.treecare.user.kesehatan_pohon.TambahKesehatanPohonActivity
import com.example.treecare.user.kondisi_tapak.TambahKondisiTapakActivity
import com.example.treecare.user.pengamatan_visual.PengamatanVisualActivity

class KerusakanPohonActivity : AppCompatActivity(), KerusakanInterface {

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var id_pohon: String
    private lateinit var nomorPohon: String
    private lateinit var codeResponse: String
    private lateinit var rvKerusakanPohon: RecyclerView
    private lateinit var kerusakanAdapter: KerusakanAdapter
    private val listKerusakan: ArrayList<RiwayatKerusakanPohonModel> = ArrayList()

    private lateinit var kerusakanPreferenceManager: KerusakanPreferenceManager
    private lateinit var counterPreferences: SharedPreferences
    private var counter:Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kerusakan_pohon)

        preferenceManager = PreferenceManager(this)
        kerusakanPreferenceManager = KerusakanPreferenceManager(this)
        counterPreferences = getSharedPreferences("kerusakanCounter", Context.MODE_PRIVATE)
        counter = counterPreferences.getInt("counter",-1)
        Log.e("Debug counter list: ", counter.toString())

        id_pohon = intent.getStringExtra("idPohon").toString()
        nomorPohon = intent.getStringExtra("nomor").toString()
        codeResponse = intent.getStringExtra("responseCode").toString()

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnSelanjutnya: AppCompatButton = findViewById(R.id.btnSelanjutnya)
        val btnTambahKerusakan: AppCompatButton = findViewById(R.id.btnTambahKerusakan)

        rvKerusakanPohon = findViewById(R.id.rvKerusakanPohon)

        val allLists = kerusakanPreferenceManager.getAllLists()

        if (counter > 0){
            for ((key, list) in allLists) {
                Log.e("Debug List","List for key $key:")
                if (list != null) {
                    for (riwayatKerusakanPohonModel in list) {
                        //Log.e("Debug List","list: $riwayatKerusakanPohonModel")
                        if (riwayatKerusakanPohonModel != null) {
                            listKerusakan.add(riwayatKerusakanPohonModel)
                        }
                    }
                } else {
                    Log.e("Debug List","List is null")
                }
            }
        }else{
            Log.e("Debug List","counter < 0")
        }

        kerusakanAdapter = KerusakanAdapter(this, listKerusakan, this)
        rvKerusakanPohon.layoutManager = LinearLayoutManager(this)
        rvKerusakanPohon.adapter = kerusakanAdapter

        btnBack.setOnClickListener {
            val intent = Intent(this, TambahKesehatanPohonActivity::class.java)
            intent.putExtra("idPohon",id_pohon)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivity(intent)
            finish()
        }

        btnSelanjutnya.setOnClickListener {
            val intent = Intent(this, TambahKondisiTapakActivity::class.java)
            intent.putExtra("idPohon",id_pohon)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivity(intent)
            finish()
        }

        btnTambahKerusakan.setOnClickListener {
            val intent = Intent(this, TambahKerusakanPohonActivity::class.java)
            intent.putExtra("idPohon",id_pohon)
            intent.putExtra("nomor",nomorPohon)
            intent.putExtra("responseCode",codeResponse)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, TambahKesehatanPohonActivity::class.java)
        intent.putExtra("idPohon",id_pohon)
        intent.putExtra("nomor",nomorPohon)
        intent.putExtra("responseCode",codeResponse)
        startActivity(intent)
        finish()
    }

    override fun onItemClick(position: Int) {
        Log.e("Debug rvCounter onItemClick: ", (position + 1).toString())
        val intent = Intent(this, TambahKerusakanPohonActivity::class.java)
        intent.putExtra("rvCounter",position+1)
        intent.putExtra("idPohon",id_pohon)
        intent.putExtra("nomor",nomorPohon)
        intent.putExtra("responseCode",codeResponse)
        startActivity(intent)
        finish()
    }
}