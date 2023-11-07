package com.example.musictree

import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class localMusic : Fragment() {
private lateinit var webView:WebView
private lateinit var localMusicAdapter: LocalMusicAdapter
private lateinit var recyclerView: RecyclerView
companion object{
    lateinit var musicList:ArrayList<localMusicData>
}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_local_music, container, false)


        recyclerView=view.findViewById(R.id.rev)
        musicList=getAllSong()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        localMusicAdapter= LocalMusicAdapter(musicList)
        recyclerView.adapter=localMusicAdapter










//        webView=view.findViewById(R.id.webview)
//        webView.loadUrl("https://www.jiosaavn.com/")
//        var webSettings=webView.settings
//        webSettings.javaScriptEnabled=true
//        webView.webViewClient= WebViewClient()


        return view
    }
    private fun getAllSong():ArrayList<localMusicData>{
        var musicList=ArrayList<localMusicData>()

        val selection="${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.MIME_TYPE} = 'audio/mpeg'"
        val projection= arrayOf(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA)


        val cursor: Cursor? =requireContext().contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null)

        if(cursor !=null && cursor.moveToNext()){
            do{
            val id=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
            val title=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
            val artist=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
            val duration=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
            val path=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                var music=localMusicData(id,title,artist,duration,path)
                var file=File(music.path)
                if(file.exists()){
                    musicList.add(music)
                }
            }while (cursor.moveToNext())
            cursor.close()
        }



        return musicList
    }

}