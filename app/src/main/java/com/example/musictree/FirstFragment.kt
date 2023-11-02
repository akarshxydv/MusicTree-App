package com.example.musictree

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FirstFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_first, container, false)


                recyclerView = view.findViewById(R.id.rev)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val apicall=RetrofitBuild.getInstance().create(apiInterface::class.java)
        GlobalScope.launch(Dispatchers.Main) {
            val result=apicall.getSong()
            if(result!=null){
                var res= result.body()?.data!!
//                var toptrack=res.filter { it.top_track }
//                var top=res.filter { it.id==1 }
                myAdapter=MyAdapter(res)
                recyclerView.adapter=myAdapter
            }
        }




        return view
    }

}