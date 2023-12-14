package com.example.treecare.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.treecare.R
import com.example.treecare.interfaces.PerubahanInterface
import com.example.treecare.service.model.RiwayatPerubahanModel

class RiwayatPerubahanAdapter(
    private var context: Context,
    private var listPerubahan: ArrayList<RiwayatPerubahanModel>,
    private var listener: PerubahanInterface
): RecyclerView.Adapter<RiwayatPerubahanAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        val tvPerubahan: TextView = view.findViewById(R.id.tvPerubahan)
        val tvTanggal: TextView = view.findViewById(R.id.tvTanggal)
        val tvJam: TextView = view.findViewById(R.id.tvJam)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflatedView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_perubahan, parent, false)
        return MyViewHolder(inflatedView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvPerubahan.text = "Data ${listPerubahan.get(position).fieldName} telah diubah oleh ${listPerubahan.get(position).user?.nama}"
        holder.tvTanggal.text = listPerubahan.get(position).tanggal
        holder.tvJam.text = listPerubahan.get(position).jam
    }

    override fun getItemCount(): Int {
        return listPerubahan.count()
    }

}