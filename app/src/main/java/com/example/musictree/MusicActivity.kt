package com.example.musictree

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.core.net.toUri
import com.example.musictree.databinding.ActivityMusicBinding
import com.squareup.picasso.Picasso


class MusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicBinding
    lateinit var runnable: Runnable
    private lateinit var medialPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var songName=intent.getStringExtra("songName")
        var artistName=intent.getStringExtra("artistName")
        var songCover=intent.getStringExtra("songCover")
        var songURL=intent.getStringExtra("songURL")
        binding.artistname.text=artistName.toString()
        binding.songname.text=songName.toString()

        Picasso.get().load("https://cms.samespace.com/assets/${songCover}").into(binding.songImg)

        medialPlayer=MediaPlayer.create(this,songURL!!.toUri())
        binding.seekbar.progress=0
        binding.seekbar.max=medialPlayer.duration
        medialPlayer.start()
        binding.play.setOnClickListener(){
            if(!medialPlayer.isPlaying){
                medialPlayer.start()
                binding.play.setImageResource(R.drawable.baseline_pause_circle_filled_24)
            }else{
                medialPlayer.pause()
                binding.play.setImageResource(R.drawable.baseline_play_circle_filled_24)
            }
        }
        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, changed: Boolean) {
                    if(changed){

                        medialPlayer.seekTo(progress)

                    }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        runnable= Runnable {
            binding.seekbar.progress=medialPlayer.currentPosition
            Handler().postDelayed(runnable,1000)

        }
        Handler().postDelayed(runnable,1000)
        medialPlayer.setOnCompletionListener {
            binding.play.setImageResource(R.drawable.baseline_play_circle_filled_24)
            binding.seekbar.progress=0

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        medialPlayer.stop()


    }
}