package com.example.treecare.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.treecare.R
import com.example.treecare.interfaces.ImageInterface

open class ImageAdapter(
    private var context: Context,
    private var listener: ImageInterface
) : RecyclerView.Adapter<ImageAdapter.MyViewHolder>(){

    private lateinit var imagesUri: ArrayList<Uri>;
    constructor( context: Context,
                 imagesUri: ArrayList<Uri>,
                 listener: ImageInterface
    ) : this(context, listener) {
        this.imagesUri = imagesUri
    }

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        val ivImage: ImageView = view.findViewById(R.id.ivImage)
        val cvImage: CardView = view.findViewById(R.id.cvImage)
        val ivXImage: ImageView = view.findViewById(R.id.ivXImage)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.MyViewHolder {
        val inflatedView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleritem_image, parent, false)
        return MyViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ImageAdapter.MyViewHolder, position: Int) {
        holder.ivImage.setImageURI(imagesUri.getOrNull(position))
    }

    override fun getItemCount(): Int {
        return imagesUri.count()
    }
}