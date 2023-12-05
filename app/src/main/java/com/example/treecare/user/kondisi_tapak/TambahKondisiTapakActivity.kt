package com.example.treecare.user.kondisi_tapak

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.treecare.R
import com.example.treecare.user.kerusakan_pohon.KerusakanPohonActivity
import com.example.treecare.user.target.TambahTargetActivity

class TambahKondisiTapakActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_kondisi_tapak)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        val btnSelanjutnya: AppCompatButton = findViewById(R.id.btnSelanjutnya)
        val sKarakteristikTapak: Spinner = findViewById(R.id.sKarakteristikTapak)
        val etKarakteristikLainnya: EditText = findViewById(R.id.etKarakteristikLainnya)
        val sMasalahTanah: Spinner = findViewById(R.id.sMasalahTanah)
        val etMasalahTanahLainnya: EditText = findViewById(R.id.etMasalahTanahLainnya)
        val sGangguanLainnya: Spinner = findViewById(R.id.sGangguanLainnya)
        val etGangguanLainnya: EditText = findViewById(R.id.etGangguanLainnya)

        sKarakteristikTapak.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = sKarakteristikTapak.selectedItem.toString()
                if(selectedItem == "Lainnya"){
                    etKarakteristikLainnya.visibility = View.VISIBLE
                }else{
                    etKarakteristikLainnya.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        sMasalahTanah.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = sMasalahTanah.selectedItem.toString()
                if(selectedItem == "Lainnya"){
                    etMasalahTanahLainnya.visibility = View.VISIBLE
                }else{
                    etMasalahTanahLainnya.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        sGangguanLainnya.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = sGangguanLainnya.selectedItem.toString()
                if(selectedItem == "Lainnya"){
                    etGangguanLainnya.visibility = View.VISIBLE
                }else{
                    etGangguanLainnya.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, KerusakanPohonActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSelanjutnya.setOnClickListener {
            val intent = Intent(this, TambahTargetActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val intent = Intent(this, KerusakanPohonActivity::class.java)
        startActivity(intent)
        finish()
    }
}