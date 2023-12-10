package com.example.treecare.adapter

import android.content.Context
import android.net.Uri
import android.view.View
import com.example.treecare.interfaces.ImageInterface
import com.squareup.picasso.Picasso

class ImageAdapterKerusakanPohon(
    private var context: Context,
    private var listener: ImageInterface
): ImageAdapter(context, listener) {
    private lateinit var imagesString: ArrayList<String>
    constructor(
        context: Context,
        imagesString: ArrayList<String>,
        listener: ImageInterface
    ) : this(context, listener) {
        this.imagesString = imagesString
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        super.onBindViewHolder(holder, position)
        Picasso.get().invalidate(imagesString[position])
        Picasso.get().load(imagesString[position]).into(holder.ivImage)

        holder.ivXImage.visibility = View.GONE
    }

    override fun getItemCount(): Int {
//        return super.getItemCount()
        return imagesString.count()
    }
}