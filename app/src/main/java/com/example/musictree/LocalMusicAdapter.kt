package com.example.musictree

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.NonDisposableHandle.parent

class LocalMusicAdapter(var lmusic:List<localMusicData>):RecyclerView.Adapter<LocalMusicAdapter.ViewHolder>() {
    private var mediaPlayer: MediaPlayer? = null
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var musicImg: ImageView =view.findViewById(R.id.img)
        var songName: TextView =view.findViewById(R.id.songname)
        var artistName: TextView =view.findViewById(R.id.artistname)
        var playmusic:ImageButton=view.findViewById(R.id.playsong)
        var pausemusic:ImageButton=view.findViewById(R.id.pausesong)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocalMusicAdapter.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.localmusic,parent,false)
        return ViewHolder(view)
    }

    @RequiresApi(34)
    override fun onBindViewHolder(holder: LocalMusicAdapter.ViewHolder, position: Int) {
        holder.songName.text=lmusic[position].songname
        holder.artistName.text=lmusic[position].artist
        holder.playmusic.setOnClickListener(){
            mediaPlayer= MediaPlayer()
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(lmusic[position].path)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()





        }
        holder.pausemusic.setOnClickListener(){
            mediaPlayer!!.stop()
        }
    }

    override fun getItemCount(): Int {
        return lmusic.size
    }

}