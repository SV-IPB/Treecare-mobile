package com.example.treecare.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.treecare.R
import com.example.treecare.interfaces.KerusakanInterface
import com.example.treecare.service.model.RiwayatKerusakanPohonModel

class KerusakanAdapter(
    private var context: Context,
    private var listKerusakan: ArrayList<RiwayatKerusakanPohonModel>,
    private var listener: KerusakanInterface
) : RecyclerView.Adapter<KerusakanAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener {
            val tvKerusakan: TextView = view.findViewById(R.id.tvKerusakan)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflatedView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_kerusakan, parent, false)

        return MyViewHolder(inflatedView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currPos = position + 1
        holder.tvKerusakan.text = "Kerusakan $currPos"
    }

    override fun getItemCount(): Int {
        return listKerusakan.count()
    }
}
