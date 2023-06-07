package com.example.tazasabziapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tazasabziapp.R
import com.example.tazasabziapp.models.items_data
import com.google.android.material.imageview.ShapeableImageView

class MyAdapterCart(var arrayList: ArrayList<items_data>) :

    RecyclerView.Adapter<MyAdapterCart.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_cart,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val curent_item = arrayList[position]

        holder.title_image.setImageResource(curent_item.img_id)
        holder.item_name.setText(curent_item.itemname).toString()
        holder.item_price.setText(curent_item.itemprice).toString()


    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun deleteItem(adapterPosition: Int) {

    }

    fun addItem(size: Int, archiveItem: items_data) {

    }

    class MyViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val title_image : ShapeableImageView = itemView.findViewById(R.id.item_icon)
        val item_name : TextView = itemView.findViewById(R.id.item_name_val)
        val item_price : TextView = itemView.findViewById(R.id.item_price_val)
    }
}