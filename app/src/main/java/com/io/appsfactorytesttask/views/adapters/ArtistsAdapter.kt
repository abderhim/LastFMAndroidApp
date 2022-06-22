package com.io.appsfactorytesttask.views.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.data.entities.Artist
import com.io.appsfactorytesttask.databinding.ItemArtistBinding
import com.io.appsfactorytesttask.utilities.ARTIST_IMAGE_QUALITY
import com.io.appsfactorytesttask.utilities.showFragment
import com.io.appsfactorytesttask.views.topAlbumsScreen.TopAlbumsFragment


class ArtistsAdapter(private val context: Context, private val activity: AppCompatActivity) :
    ListAdapter<Artist, ArtistsAdapter.ArtistViewHolder>(ArtistDiffUtil()) {

    inner class ArtistViewHolder(val binding: ItemArtistBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {

        val artist = currentList[position]

        holder.binding.artistName.text = artist.name

        val artistImage = artist.imagesList?.get(ARTIST_IMAGE_QUALITY)?.imageText

        if(artistImage.isNullOrEmpty()){
            holder.binding.artistImage.setImageDrawable(context.getDrawable(R.drawable.no_photo))
        }

        else {
            Glide.with(context).load(artistImage).into(holder.binding.artistImage)
        }

        holder.itemView.setOnClickListener {
            val topAlbumsFragment = TopAlbumsFragment()
            val arguments = Bundle()
            arguments.putString(context.getString(R.string.artist_name), artist.name)
            topAlbumsFragment.arguments = arguments
            showFragment(activity,topAlbumsFragment)
        }

    }

    class ArtistDiffUtil : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem == newItem
        }
    }
}
