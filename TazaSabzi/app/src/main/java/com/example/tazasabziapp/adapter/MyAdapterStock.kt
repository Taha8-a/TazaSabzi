package com.example.tazasabziapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tazasabziapp.R
import com.example.tazasabziapp.bottom_navigations_actions.SaleActivity
import com.example.tazasabziapp.contact.Product
import com.example.tazasabziapp.contact.Request
import com.example.tazasabziapp.contact.Response
import com.example.tazasabziapp.network.IntRequestContact
import com.example.tazasabziapp.network.NetworkClient
import com.example.tazasabziapp.utils.Constants
import com.example.tazasabziapp.utils.DataProvider
import com.example.tazasabziapp.utils.showToast
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.list_item_stock.view.*
import retrofit2.Call
import retrofit2.Callback

class MyAdapterStock(var activity:Activity, var context: Context, var dataSource:MutableList<Product>) :

    RecyclerView.Adapter<MyAdapterStock.MyViewHolder>(), Callback<Response> {
    private val retrofitClient = NetworkClient.getNetworkClient()
    private val requestContract = retrofitClient.create(IntRequestContact::class.java)
    private lateinit var deletedProduct:Product
    private var deletedPosition:Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.list_item_stock,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val currentItem = dataSource[position]

        holder.titleImage.setImageResource(R.drawable.item_image)
        holder.itemValue.text = currentItem.name
        holder.itemPriceVal.text = currentItem.price

        holder.btnUpdateItem.setOnClickListener {
            Intent(context,SaleActivity::class.java).apply {
                DataProvider.product = currentItem
                putExtra(Constants.KEY_REASON,2)  //2 Means Edit
                activity.startActivity(this)
            }
        }

        holder.btnDeleteItem.setOnClickListener{
            AlertDialog.Builder(context)
                .setTitle("TazaSabzi App Alert")
                .setMessage("Are you sure? You want to delete this Stock")
                .setPositiveButton("Yes") { dialog, which ->
                    deletedProduct = currentItem
                    deletedPosition = position
                    val request = Request(
                        action = Constants.DELETE_SABZI_PRODUCTS,
                        userId = DataProvider.userId,
                        productId = currentItem.productId
                    )
                    val callResponse = requestContract.makeApiCall(request)
                    callResponse.enqueue(this)
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

        var titleImage : ShapeableImageView = itemView.item_icon
        var itemValue : TextView = itemView.itemNameVal
        var itemPriceVal : TextView = itemView.itemPriceVal
        var btnUpdateItem : Button = itemView.btnUpdateItem
        var btnDeleteItem : Button = itemView.btnDeleteItem
    }

    override fun onFailure(call: Call<Response>, t: Throwable) {
        context.showToast("Server is not responding. Please contact your system administrator")
    }

    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
        if(response.body()!=null){
            val serverResponse = response.body()
            if(serverResponse!!.status){
                dataSource.remove(deletedProduct)
                notifyItemRemoved(deletedPosition)
                notifyItemRangeChanged(deletedPosition,dataSource.size)
                context.showToast(serverResponse.message)
            }
            else{
                context.showToast(serverResponse.message)
            }
        }
        else{
            context.showToast("Server is not responding. Please contact your system administrator")
        }
    }
}