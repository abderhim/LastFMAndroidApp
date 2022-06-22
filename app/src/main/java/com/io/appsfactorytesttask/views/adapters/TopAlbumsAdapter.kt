package com.io.appsfactorytesttask.views.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.io.appsfactorytesttask.views.topAlbumsScreen.DetailAlbumFragment
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.data.entities.Album
import com.io.appsfactorytesttask.databinding.ItemTopAlbumBinding
import com.io.appsfactorytesttask.utilities.*

class TopAlbumsAdapter(private val context: Context, private val activity: AppCompatActivity,private val fragmentUsing: String) :
    ListAdapter<Album, TopAlbumsAdapter.AlbumViewHolder>(AlbumDiffUtil()) {


    inner class AlbumViewHolder(val binding: ItemTopAlbumBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemTopAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = currentList[position]
        var albumName = ""

        albumName = if (album.name?.contains(context.getString(R.string.album_name_null)) == true) {
            context.getString(R.string.unavailable)
        } else {
            album.name.toString()
        }

        holder.binding.topAlbumName.text = albumName

        if (fragmentUsing == context.getString(R.string.top_albums_fragment)) {
            val albumImage = album.imagesList?.get(ALBUM_IMAGE_QUALITY)?.imageText
            if (albumImage.isNullOrEmpty()) {
                holder.binding.topAlbumImage.setImageDrawable(context.getDrawable(R.drawable.no_photo))
            } else {
                Glide.with(context).load(albumImage).apply(
                    RequestOptions().override(Target.SIZE_ORIGINAL)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                ).into(holder.binding.topAlbumImage)
            }
        }


        //coming from MainScreenFragment
        else {
            val bitmap = stringToBitMap(album.imagesList?.get(0)?.imageText)
            val drawable: Drawable = BitmapDrawable(context.resources, bitmap)
            holder.binding.topAlbumImage.setImageDrawable(drawable)
        }


        holder.itemView.setOnClickListener {
            val detailAlbumFragment = DetailAlbumFragment()
            val arguments = Bundle()

            arguments.putString(
                context.getString(R.string.album_name),
                albumName
            )
            arguments.putString(
                context.getString(R.string.artist_name),
                album.artist?.name
            )

            arguments.putString(
                context.getString(R.string.coming_from),
                fragmentUsing
            )

            val bitmap = (holder.binding.topAlbumImage.drawable as BitmapDrawable).bitmap
            arguments.putString(
                context.getString(R.string.album_image),
                bitMapToString(bitmap)
            )

            detailAlbumFragment.arguments = arguments
            showFragment(activity, detailAlbumFragment)
        }

    }


    class AlbumDiffUtil : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }
}

