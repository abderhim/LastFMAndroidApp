package com.io.appsfactorytesttask.views.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.io.appsfactorytesttask.data.entities.Album
import com.io.appsfactorytesttask.views.topAlbumsScreen.DetailAlbumFragment

import android.graphics.drawable.BitmapDrawable

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.io.appsfactorytesttask.R
import com.io.appsfactorytesttask.utilities.ALBUM_IMAGE_QUALITY
import com.io.appsfactorytesttask.utilities.bitMapToString
import com.io.appsfactorytesttask.utilities.showFragment
import com.io.appsfactorytesttask.utilities.stringToBitMap


class TopAlbumsAdapter(
    private val context: Context,
    private val activity: AppCompatActivity,
    private val fragmentUsing: String
) :
    RecyclerView.Adapter<TopAlbumsAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var albumName: TextView = view.findViewById(R.id.top_album_name)
        var albumImage: ImageView = view.findViewById(R.id.top_album_image)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_top_album, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val album = differ.currentList[position]
        var albumName = ""

        albumName = if (album.name?.contains(context.getString(R.string.album_name_null)) == true) {
            context.getString(R.string.unavailable)
        } else {
            album.name.toString()
        }

        holder.albumName.text = albumName

        if (fragmentUsing == context.getString(com.io.appsfactorytesttask.R.string.top_albums_fragment)) {
            val albumImage = album.imagesList?.get(ALBUM_IMAGE_QUALITY)?.imageText
            if (albumImage.isNullOrEmpty()) {
                holder.albumImage.setImageDrawable(context.getDrawable(R.drawable.no_photo))
            } else {
                Glide.with(context).load(albumImage).apply(
                    RequestOptions().override(Target.SIZE_ORIGINAL)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                ).into(holder.albumImage)
            }
        }


        //coming from MainScreenFragment
        else {
            val bitmap = stringToBitMap(album.imagesList?.get(0)?.imageText)
            val drawable: Drawable = BitmapDrawable(context.resources, bitmap)
            holder.albumImage.setImageDrawable(drawable)
        }


        holder.itemView.setOnClickListener {
            val detailAlbumFragment = DetailAlbumFragment()
            val arguments = Bundle()

            arguments.putString(
                context.getString(com.io.appsfactorytesttask.R.string.album_name),
                albumName
            )
            arguments.putString(
                context.getString(com.io.appsfactorytesttask.R.string.artist_name),
                album.artist?.name
            )

            arguments.putString(
                context.getString(com.io.appsfactorytesttask.R.string.coming_from),
                fragmentUsing
            )

            val bitmap = (holder.albumImage.drawable as BitmapDrawable).bitmap
            arguments.putString(
                context.getString(com.io.appsfactorytesttask.R.string.album_image),
                bitMapToString(bitmap)
            )

            detailAlbumFragment.arguments = arguments
            showFragment(activity, detailAlbumFragment)
        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}
