package com.example.tazasabziapp.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tazasabziapp.R
import com.example.tazasabziapp.contact.Product
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.list_item_sale.view.*

class MyAdapterPurchase(var activity: Activity, var context: Context, var dataSource:MutableList<Product>) :

    RecyclerView.Adapter<MyAdapterPurchase.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.list_item_sale,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = dataSource[position]

        holder.titleImage.setImageResource(R.drawable.item_image)
        holder.itemNameVal.text = currentItem.name
        holder.itemPriceVal.text = currentItem.price

        holder.btnBuy.setOnClickListener{
            AlertDialog.Builder(activity)
                .setTitle("TazaSabzi App Alert")
                .setMessage("Are you sure? You want to buy this stock")
                .setPositiveButton("Yes") { dialog, which ->
                    dialog?.dismiss()
                    Toast.makeText(activity,"Stock bought successfully",Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog?.dismiss()
                }
                .create()
                .show()
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    class MyViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        var titleImage : ShapeableImageView = itemView.p_item_icon
        var itemNameVal : TextView = itemView.pItemNameVal
        var itemPriceVal : TextView = itemView.pItemPriceVal
        var btnBuy : Button = itemView.btnBuyItem
    }
}