package com.example.tazasabziapp.top_navbar_actions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tazasabziapp.MainActivity
import com.example.tazasabziapp.R
import com.example.tazasabziapp.contact.Request
import com.example.tazasabziapp.contact.Response
import com.example.tazasabziapp.network.IntRequestContact
import com.example.tazasabziapp.network.NetworkClient
import com.example.tazasabziapp.utils.Constants
import com.example.tazasabziapp.utils.showToast
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.edEmail
import kotlinx.android.synthetic.main.activity_profile.edPassword
import kotlinx.android.synthetic.main.activity_profile.edUsername
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback

class ProfileActivity : AppCompatActivity(), Callback<Response> {
    private val retrofitClient = NetworkClient.getNetworkClient()
    private val requestContract = retrofitClient.create(IntRequestContact::class.java)
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId:String
    private lateinit var userName:String
    private lateinit var userPassword:String
    private lateinit var userEmail:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(Constants.KEY_USER_ID,"").toString().trim()
        userName = sharedPreferences.getString(Constants.KEY_USER_NAME,"").toString().trim()
        userEmail = sharedPreferences.getString(Constants.KEY_USER_EMAIL,"").toString().trim()
        userPassword = sharedPreferences.getString(Constants.KEY_USER_PASSWORD,"").toString().trim()

        btn_update_profile.setOnClickListener {
            userName = edUsername.text.toString().trim()
            userPassword = edPassword.text.toString().trim()
            userEmail = edEmail.text.toString().trim()
            if(userName.isNullOrEmpty()){
                showToast("Please enter name")
            }
            else if(userEmail.isNullOrEmpty()){
                showToast("Please enter email")
            }
            else if(userPassword.isNullOrEmpty()){
                showToast("Please enter password")
            }
            else{
                val request = Request(
                    action = Constants.UPDATE_USER,
                    userId= userId,
                    userName = userName,
                    userEmail = userEmail,
                    userPassword = userPassword
                )
                val callResponse = requestContract.makeApiCall(request)
                callResponse.enqueue(this)
            }
        }
    }
    override fun onFailure(call: Call<Response>, t: Throwable) {
        showToast("Server is not responding. Please contact your system administrator")
        edUsername.setText("")
        edPassword.setText("")
        edEmail.setText("")
    }

    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
        if(response.body()!=null){
            val serverResponse = response.body()
            if(serverResponse!!.status){
                showToast("Profile updated successfully")
                saveUserToPref(serverResponse.userId,userName,userEmail,userPassword)
                Intent(this,MainActivity::class.java).apply {
                    startActivity(this)
                }
            }
            else{
                showToast(serverResponse.message)
                edUsername.setText("")
                edPassword.setText("")
                edEmail.setText("")
            }
        }
        else{
            showToast("Server is not responding. Please contact your system administrator")
            edUsername.setText("")
            edPassword.setText("")
            edEmail.setText("")
        }
    }

    private fun saveUserToPref(userId:String,userName:String,userEmail:String,userPassword:String){
        val editor = sharedPreferences.edit()
        editor.putString(Constants.KEY_USER_ID,userId)
        editor.putString(Constants.KEY_USER_NAME,userName)
        editor.putString(Constants.KEY_USER_EMAIL,userEmail)
        editor.putString(Constants.KEY_USER_PASSWORD,userPassword)
        editor.commit()
    }
}