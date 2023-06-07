package com.example.tazasabziapp.bottom_navigations_actions

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tazasabziapp.R
import com.example.tazasabziapp.adapter.MyAdapterStock
import com.example.tazasabziapp.contact.Product
import com.example.tazasabziapp.contact.Request
import com.example.tazasabziapp.contact.Response
import com.example.tazasabziapp.network.IntRequestContact
import com.example.tazasabziapp.network.NetworkClient
import com.example.tazasabziapp.utils.Constants
import com.example.tazasabziapp.utils.DataProvider
import com.example.tazasabziapp.utils.showToast
import kotlinx.android.synthetic.main.activity_stock.*
import retrofit2.Call
import retrofit2.Callback

class StockActivity : AppCompatActivity(), Callback<Response> {

    private lateinit var adapter : MyAdapterStock

    //-------------------------------------------------


    //backend logic variables for getting my sold sabzi products data
    private val retrofitClient = NetworkClient.getNetworkClient()
    private val requestContract = retrofitClient.create(IntRequestContact::class.java)
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId:String
    private lateinit var userName:String
    private lateinit var userPassword:String
    private lateinit var userEmail:String
    //-------------------------------------------------

    lateinit var dataSource:MutableList<Product>
    //-------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock)

        //backend logic for getting shared preferences data
        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE)
        userId = sharedPreferences.getString(Constants.KEY_USER_ID,"").toString().trim()
        userName = sharedPreferences.getString(Constants.KEY_USER_NAME,"").toString().trim()
        userEmail = sharedPreferences.getString(Constants.KEY_USER_EMAIL,"").toString().trim()
        userPassword = sharedPreferences.getString(Constants.KEY_USER_PASSWORD,"").toString().trim()

        DataProvider.userId = userId
        DataProvider.userName = userName
        DataProvider.userEmail = userEmail
        DataProvider.userPassword = userPassword
        //-------------------------------------------------
    }
    override fun onStart() {
        super.onStart()
        val request = Request(
            action = Constants.GET_SABZI_PRODUCTS,
            userId = userId
        )
        val callResponse = requestContract.makeApiCall(request)
        callResponse.enqueue(this)

        //backend logic for getting my sold sabzi products
        recyclerViewStock.layoutManager = LinearLayoutManager(this@StockActivity)
        recyclerViewStock.setHasFixedSize(true)
        dataSource = DataProvider.response.myProducts
        if(dataSource.size>0){
            adapter = MyAdapterStock(this@StockActivity, this, dataSource)
            recyclerViewStock.adapter = adapter
        }
    }

    override fun onFailure(call: Call<Response>, t: Throwable) {
        showToast("Server is not responding. Please contact your system administrator")
    }

    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
        if(response.body()!=null){
            val serverResponse = response.body()
            if(serverResponse!!.status){
                DataProvider.response = serverResponse
            }
            else{
                showToast(serverResponse.message)
            }
        }
        else{
            showToast("Server is not responding. Please contact your system administrator")
        }
    }

//    private fun getUserdata() {
//        for(i in imageId.indices){
//            val news = items_data(imageId[i],"apple","5000")
//            newArrayList.add(news)
//        }
//        tempArrayList.addAll(newArrayList)
//        val adapter = MyAdapterStock(tempArrayList)
//        newRecylerview.adapter = adapter
//    }
}