package com.example.musictree

import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.musictree.databinding.ActivityMusicBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class MusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicBinding
    lateinit var runnable: Runnable
    private lateinit var medialPlayer: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)


        medialPlayer= MediaPlayer()
        var songName=intent.getStringExtra("songName")
        var artistName=intent.getStringExtra("artistName")
        var songCover=intent.getStringExtra("songCover")
        var songURL=intent.getStringExtra("songURL")
        var songId=intent.getIntExtra("songId",0)
        var backColor=intent.getStringExtra("backColor")

        changeStatusBarColor(backColor!!)
        //window.statusBarColor = ContextCompat.getColor(this, Color.parseColor("${backColor}")

        medialPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            binding.artistname.text = artistName.toString()
            binding.songname.text = songName.toString()
            binding.layoutmusic.setBackgroundColor(Color.parseColor("${backColor}"))
            Picasso.get().load("https://cms.samespace.com/assets/${songCover}")
                .into(binding.songImg)

            //medialPlayer = MediaPlayer.create(this, songURL!!.toUri())

            try{
                songURL=songURL!!.replace(" ","")
                medialPlayer.setDataSource(songURL)
            }catch (e:Exception){
                e.printStackTrace()
            }
            //medialPlayer.prepareAsync()
            medialPlayer.prepare()

            medialPlayer.setOnPreparedListener {
                binding.seekbar.progress = 0
                binding.seekbar.max = medialPlayer.duration
                binding.totalTime.text = formatDuration(medialPlayer.duration)
                medialPlayer.start()
            }
                medialPlayer.setOnErrorListener { mp, what, extra ->
                    // Handle media errors and move to the next song
                    when (what) {
                        MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK -> {
                            Toast.makeText(this, "Song is not availble", Toast.LENGTH_SHORT).show()
                        }
                        MediaPlayer.MEDIA_ERROR_SERVER_DIED -> {
                            Toast.makeText(this, "Song is not availble", Toast.LENGTH_SHORT).show()
                        }
                        MediaPlayer.MEDIA_ERROR_UNKNOWN -> {
                            Toast.makeText(this, "Song is not availble", Toast.LENGTH_SHORT).show()
                            medialPlayer.release()
                        }
                        MediaPlayer.MEDIA_ERROR_IO -> {
                            Toast.makeText(this, "Song is not availble", Toast.LENGTH_SHORT).show()
                        }
                        MediaPlayer.MEDIA_ERROR_TIMED_OUT -> {
                            Toast.makeText(this, "Song is not availble", Toast.LENGTH_SHORT).show()
                            medialPlayer.release()
                        }
                        MediaPlayer.MEDIA_ERROR_UNSUPPORTED -> {
                            Toast.makeText(this, "Song is not availble", Toast.LENGTH_SHORT).show()
                            medialPlayer.release()
                        }
                        else -> {
                            Toast.makeText(this, "Song is not availble", Toast.LENGTH_SHORT).show()
                            medialPlayer.release()
                        }
                    }
                    true
                }

        }catch (e:Exception){
            e.printStackTrace()
        }
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
            //medialPlayer.stop()
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
                  //medialPlayer=MediaPlayer.create(this@MusicActivity,songUrl!!.toUri())
                    medialPlayer.reset()
                    medialPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                    try {
                        songUrl=songUrl.replace(" ","")
                        medialPlayer.setDataSource(songUrl)
                        medialPlayer.prepare()
                        medialPlayer.setOnPreparedListener {
                            binding.seekbar.progress=0
                            binding.seekbar.max=medialPlayer.duration
                            binding.totalTime.text=formatDuration(medialPlayer.duration)
                            medialPlayer.start()
                        }
                        medialPlayer.setOnErrorListener { mp, what, extra ->
                            // Handle media errors and move to the next song
                            Toast.makeText(this@MusicActivity,"Song is not availble",Toast.LENGTH_SHORT).show()
                            false
                        }

                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }
                 }
            }


        }
        binding.previous.setOnClickListener(){
            if(songId>1){songId-=1}
            //medialPlayer.stop()
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
                        //medialPlayer=MediaPlayer.create(this@MusicActivity,songUrl!!.toUri())

                        medialPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                        medialPlayer.reset()
                        try {
                            songUrl=songUrl.replace(" ","")
                            medialPlayer.setDataSource(songUrl)
                            medialPlayer.prepare()
                            medialPlayer.setOnPreparedListener {
                                binding.seekbar.progress=0
                                binding.seekbar.max=medialPlayer.duration
                                binding.totalTime.text=formatDuration(medialPlayer.duration)
                                medialPlayer.start()
                            }
                            medialPlayer.setOnErrorListener { mp, what, extra ->
                                // Handle media errors and move to the next song
                                Toast.makeText(this@MusicActivity,"Song is not availble",Toast.LENGTH_SHORT).show()
                                false
                            }

                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, changed: Boolean) {
                    if(changed){

                        medialPlayer.seekTo(progress)
                        binding.currentTime.text=formatDuration(progress)

                    }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        runnable= Runnable {
            val currentPosition = medialPlayer?.currentPosition ?: 0
            binding.seekbar.progress=medialPlayer.currentPosition
            binding.currentTime.text=formatDuration(currentPosition)
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
    private fun formatDuration(duration: Int): String {
        val minutes = duration / 1000 / 60
        val seconds = duration / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun isURLValid(url: String?): Boolean {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "HEAD"
            val responseCode = connection.responseCode
            return responseCode == HttpURLConnection.HTTP_OK
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }


}