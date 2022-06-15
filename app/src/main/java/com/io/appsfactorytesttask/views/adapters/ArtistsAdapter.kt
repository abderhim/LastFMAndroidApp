package com.io.appsfactorytesttask.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.io.appsfactorytesttask.data.entities.Artist
import com.io.appsfactorytesttask.views.topAlbumsScreen.TopAlbumsFragment

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.utilities.ARTIST_IMAGE_QUALITY
import com.io.appsfactorytesttask.utilities.showFragment


class ArtistsAdapter(private val context: Context, private val activity: AppCompatActivity) :
    RecyclerView.Adapter<ArtistsAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var artistName: TextView = view.findViewById(com.io.appsfactorytesttask.R.id.artist_name)
        var artistImage: ImageView = view.findViewById(com.io.appsfactorytesttask.R.id.artist_image)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(com.io.appsfactorytesttask.R.layout.item_artist, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val artist = differ.currentList[position]

        holder.artistName.text = artist.name

        val artistImage = artist.imagesList?.get(ARTIST_IMAGE_QUALITY)?.imageText

        if(artistImage.isNullOrEmpty()){
            holder.artistImage.setImageDrawable(context.getDrawable(R.drawable.no_photo))
        }

        else {
            Glide.with(context).load(artistImage).into(holder.artistImage)
         }

        holder.itemView.setOnClickListener {
            val topAlbumsFragment = TopAlbumsFragment()
            val arguments = Bundle()
            arguments.putString(context.getString(com.io.appsfactorytesttask.R.string.artist_name), artist.name)
            topAlbumsFragment.arguments = arguments
            showFragment(activity,topAlbumsFragment)
        }


    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }
}
