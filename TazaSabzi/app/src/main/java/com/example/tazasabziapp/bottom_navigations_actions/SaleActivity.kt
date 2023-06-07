package com.example.tazasabziapp.bottom_navigations_actions

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tazasabziapp.MainActivity
import com.example.tazasabziapp.R
import com.example.tazasabziapp.adapter.MyAdapterPurchase
import com.example.tazasabziapp.contact.Product
import com.example.tazasabziapp.contact.Request
import com.example.tazasabziapp.contact.Response
import com.example.tazasabziapp.models.items_data
import com.example.tazasabziapp.network.IntRequestContact
import com.example.tazasabziapp.network.NetworkClient
import com.example.tazasabziapp.utils.Constants
import com.example.tazasabziapp.utils.DataProvider
import com.example.tazasabziapp.utils.showToast
import kotlinx.android.synthetic.main.activity_sale.*
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback

class SaleActivity : AppCompatActivity(), Callback<Response> {

    private lateinit var ed_item_name:TextView
    private lateinit var ed_item_price:TextView
    private lateinit var ed_item_qty:TextView


    //-------------------------------------------------
    //backend logic variables for adding sabzi products data
    private val retrofitClient = NetworkClient.getNetworkClient()
    private val requestContract = retrofitClient.create(IntRequestContact::class.java)
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId:String
    private lateinit var userName:String
    private lateinit var userPassword:String
    private lateinit var userEmail:String

    private lateinit var itemName:String
    private lateinit var itemPrice:String

    private var reason:Int = 0
    private lateinit var editedProduct: Product
    //-------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale)

        //backend logic for getting shared preferences data
        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE)
        userId = sharedPreferences.getString(Constants.KEY_USER_ID,"").toString().trim()
        userName = sharedPreferences.getString(Constants.KEY_USER_NAME,"").toString().trim()
        userEmail = sharedPreferences.getString(Constants.KEY_USER_EMAIL,"").toString().trim()
        userPassword = sharedPreferences.getString(Constants.KEY_USER_PASSWORD,"").toString().trim()

        reason = intent.getIntExtra(Constants.KEY_REASON,0)

        renderUIForEdit()

        DataProvider.userId = userId
        DataProvider.userName = userName
        DataProvider.userEmail = userEmail
        DataProvider.userPassword = userPassword


        btn_sale.setOnClickListener(){

            itemName = edItemName.text.toString().trim()
            itemPrice = edPrice.text.toString().trim()

            if(itemName.isNullOrEmpty()){
                showToast("Please enter item name")
            }
            else if(itemPrice.isNullOrEmpty()){
                showToast("Please enter price of product")
            }
            else{
                var request = Request()
                if(reason==2){
                    request = Request(
                        action = Constants.UPDATE_SABZI_PRODUCTS,
                        userId = userId,
                        productId = editedProduct.productId,
                        name = itemName,
                        price = itemPrice
                    )
                }
                else{
                    request = Request(
                        action = Constants.ADD_SABZI_PRODUCTS,
                        userId = userId,
                        name = itemName,
                        price = itemPrice
                    )
                }
                val callResponse = requestContract.makeApiCall(request)
                callResponse.enqueue(this)
            }
        }

    }

    private fun renderUIForEdit(){
        if(reason==2){
            editedProduct = DataProvider.product
            edItemName.setText(editedProduct.name)
            edPrice.setText(editedProduct.price)
            btn_sale.text = "Update sale"
        }
    }

    override fun onFailure(call: Call<Response>, t: Throwable) {
        showToast("Server is not responding. Please contact your system administrator")
    }

    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
        if(response.body()!=null){
            val serverResponse = response.body()
            if(serverResponse!!.status){
                showToast(serverResponse.message)
                Intent(this,MainActivity::class.java).apply {
                    startActivity(this)
                }
            }
            else{
                showToast(serverResponse.message)
            }
        }
        else{
            showToast("Server is not responding. Please contact your system administrator")
        }
    }

}