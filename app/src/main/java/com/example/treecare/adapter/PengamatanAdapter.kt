package com.example.treecare.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.treecare.R
import com.example.treecare.interfaces.PengamatanInterface
import com.example.treecare.service.model.RiwayatPohonModel
import com.squareup.picasso.Picasso

class PengamatanAdapter (
    private var context: Context,
    private var riwayats: ArrayList<RiwayatPohonModel>,
    private var listener: PengamatanInterface
) : RecyclerView.Adapter<PengamatanAdapter.MyViewHolder>(){

    inner class MyViewHolder(view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val ivImage: ImageView = view.findViewById(R.id.ivImage)
        val tvNoPohon: TextView = view.findViewById(R.id.tvNoPohon)
        val tvProyek: TextView = view.findViewById(R.id.tvProyek)
        val tvTanggal: TextView = view.findViewById(R.id.tvTanggal)
        val tvJam: TextView = view.findViewById(R.id.tvJam)
        val tvPetugas: TextView = view.findViewById(R.id.tvPetugas)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflatedView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_history, parent, false)

        return MyViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (!(riwayats.getOrNull(position)?.identitasPohon?.gambar).isNullOrEmpty()) {
            Picasso.get().invalidate(riwayats.getOrNull(position)?.identitasPohon?.gambar)
            Picasso.get().load(riwayats.getOrNull(position)?.identitasPohon?.gambar).into(holder.ivImage)
        }

        holder.tvNoPohon.setText((riwayats.getOrNull(position)?.identitasPohon)?.nomorPohon)
        holder.tvProyek.setText((riwayats.getOrNull(position)?.identitasPohon)?.namaProjek)
        holder.tvTanggal.setText(riwayats.getOrNull(position)?.tanggal)
        holder.tvJam.setText(riwayats.getOrNull(position)?.jam)
        holder.tvPetugas.setText((riwayats.getOrNull(position)?.user)?.nama)
    }

    override fun getItemCount(): Int {
        return riwayats.count()
    }
}
