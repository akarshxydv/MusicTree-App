package com.example.musictree

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyAdapter(var mysong:List<Data>):RecyclerView.Adapter<MyAdapter.ViewHolder>() {
   inner class ViewHolder(itemview: View):RecyclerView.ViewHolder(itemview) {
       var musicImg:ImageView=itemview.findViewById(R.id.img)
       var songName:TextView=itemview.findViewById(R.id.songname)
       var artistName:TextView=itemview.findViewById(R.id.artistname)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.songlist,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
            return mysong.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var songs=mysong[position]
        holder.songName.text=songs.name
        holder.artistName.text=songs.artist
        Picasso.get().load("https://cms.samespace.com/assets/${songs.cover}").into(holder.musicImg)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MusicActivity::class.java)
            intent.putExtra("songName", songs.name)
            intent.putExtra("artistName", songs.artist)
            intent.putExtra("songCover", songs.cover)
            intent.putExtra("songURL", songs.url)
            holder.itemView.context.startActivity(intent)





        }


    }
}