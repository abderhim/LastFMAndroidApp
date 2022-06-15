package com.io.appsfactorytesttask.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.io.appsfactorytesttask.data.entities.Track

class TrackAdapter (private val context: Context, private val activity: AppCompatActivity) :
    RecyclerView.Adapter<TrackAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var trackName: TextView = view.findViewById(com.io.appsfactorytesttask.R.id.track_name)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(com.io.appsfactorytesttask.R.layout.item_track, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val track = differ.currentList[position]
        holder.trackName.text = track.name

    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }
}
