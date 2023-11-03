package com.example.musictree

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.musictree.databinding.ActivityMusicBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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
        var songId=intent.getIntExtra("songId",0)
        var backColor=intent.getStringExtra("backColor")

        changeStatusBarColor(backColor!!)
        //window.statusBarColor = ContextCompat.getColor(this, Color.parseColor("${backColor}")
        binding.artistname.text=artistName.toString()
        binding.songname.text=songName.toString()
       binding.layoutmusic.setBackgroundColor(Color.parseColor("${backColor}"))
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
                binding.play.setImageResource(R.drawable.playmusic_foreground)
            }
        }

        binding.next.setOnClickListener(){
            songId+=1
            medialPlayer.stop()
            val apicall=RetrofitBuild.getInstance().create(apiInterface::class.java)
            GlobalScope.launch(Dispatchers.Main) {
                val result=apicall.getSong()
                if(result!=null){
                var res= result.body()?.data!!
                //var toptrack=res.filter { it.top_track }
                var top=res.filter { it.id==songId }
                for(ele in top){
                    var songUrl=ele.url
                    var songcover=ele.cover
                    binding.songname.text=ele.name
                    binding.artistname.text=ele.artist
                    changeStatusBarColor(ele.accent)
                    binding.layoutmusic.setBackgroundColor(Color.parseColor("${ele.accent}"))
                    Picasso.get().load("https://cms.samespace.com/assets/${songcover}").into(binding.songImg)
                  medialPlayer=MediaPlayer.create(this@MusicActivity,songUrl!!.toUri())
                    binding.seekbar.progress=0
                    binding.seekbar.max=medialPlayer.duration
                    medialPlayer.start()
                }
                 }
            }


        }
        binding.previous.setOnClickListener(){
            if(songId>1){songId-=1}
            medialPlayer.stop()
            val apicall=RetrofitBuild.getInstance().create(apiInterface::class.java)
            GlobalScope.launch(Dispatchers.Main) {
                val result=apicall.getSong()
                if(result!=null){
                    var res= result.body()?.data!!
                    //var toptrack=res.filter { it.top_track }
                    var top=res.filter { it.id==songId }
                    for(ele in top){
                        var songUrl=ele.url
                        var songcover=ele.cover
                        binding.songname.text=ele.name
                        binding.artistname.text=ele.artist
                        changeStatusBarColor(ele.accent)
                        binding.layoutmusic.setBackgroundColor(Color.parseColor("${ele.accent}"))
                        Picasso.get().load("https://cms.samespace.com/assets/${songcover}").into(binding.songImg)
                        medialPlayer=MediaPlayer.create(this@MusicActivity,songUrl!!.toUri())
                        binding.seekbar.progress=0
                        binding.seekbar.max=medialPlayer.duration
                        medialPlayer.start()
                    }
                }
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
            binding.play.setImageResource(R.drawable.playmusic_foreground)
            binding.seekbar.progress=0

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        medialPlayer.stop()


    }
    private fun changeStatusBarColor(color: String) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor(color)
        }
    }
}