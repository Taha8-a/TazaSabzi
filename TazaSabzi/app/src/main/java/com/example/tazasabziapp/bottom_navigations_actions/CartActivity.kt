package com.example.tazasabziapp.bottom_navigations_actions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tazasabziapp.R
import com.example.tazasabziapp.adapter.MyAdapterCart
import com.example.tazasabziapp.models.items_data

class CartActivity : AppCompatActivity() {

    private lateinit var newRecylerview : RecyclerView
    private lateinit var newArrayList : ArrayList<items_data>
    private lateinit var tempArrayList : ArrayList<items_data>

    lateinit var imageId : Array<Int>
    lateinit var heading : Array<String>
    lateinit var news : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        //for image of item
        imageId = arrayOf(
            R.drawable.item_image
        )
        // items detail here
        heading = arrayOf(
            "Biden aims to expand vaccines for adults and children"
        )

        newRecylerview =findViewById(R.id.recycler_view_cart)
        newRecylerview.layoutManager = LinearLayoutManager(this)
        newRecylerview.setHasFixedSize(true)

        newArrayList = arrayListOf<items_data>()
        tempArrayList = arrayListOf<items_data>()

        getUserdata()
    }

    private fun getUserdata() {
        for(i in imageId.indices){
            val news = items_data(imageId[i],"apple","5000")
            newArrayList.add(news)
        }
        tempArrayList.addAll(newArrayList)
        val adapter = MyAdapterCart(tempArrayList)
        newRecylerview.adapter = adapter
    }
}