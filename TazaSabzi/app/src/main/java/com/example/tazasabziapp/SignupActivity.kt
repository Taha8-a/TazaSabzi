package com.example.tazasabziapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tazasabziapp.contact.Request
import com.example.tazasabziapp.contact.Response
import com.example.tazasabziapp.network.IntRequestContact
import com.example.tazasabziapp.network.NetworkClient
import com.example.tazasabziapp.utils.Constants
import com.example.tazasabziapp.utils.showToast
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback

class SignupActivity : AppCompatActivity(), Callback<Response> {
   // private lateinit var loginlink: TextView

    private val retrofitClient = NetworkClient.getNetworkClient()
    private val requestContract = retrofitClient.create(IntRequestContact::class.java)
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userName:String
    private lateinit var userPassword:String
    private lateinit var userEmail:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //backend logic for getting shared preferences data
        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

        checkIfUserAlreadyRegistered()

        btnSignup.setOnClickListener {
            userName = edUsername.text.toString().trim()
            userPassword = edPassword.text.toString().trim()
            userEmail = edEmail.text.toString().trim()
            if(userName.isNullOrEmpty()){
                showToast("Please enter your name")
            }
            else if(userEmail.isNullOrEmpty()){
                showToast("Please enter your email")
            }
            else if(userPassword.isNullOrEmpty()){
                showToast("Please enter your password")
            }
            else{
                val request = Request(
                    action = Constants.REGISTER_USER,
                    userName = userName,
                    userEmail = userEmail,
                    userPassword = userPassword
                )
                val callResponse = requestContract.makeApiCall(request)
                callResponse.enqueue(this)
            }
        }

//       loginlink.setOnClickListener {
//            intent = Intent(this, LoginActivity::class.java).apply {
//                startActivity(this)
//            }
//        }
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
                saveUserToPref(serverResponse.userId,userName,userEmail,userPassword)
                Intent(this,MainActivity::class.java).apply {
                    startActivity(this)
                    finish()
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

    private fun checkIfUserAlreadyRegistered(){
        val userId = sharedPreferences.getString(Constants.KEY_USER_ID,"invalid user id")
        val userName = sharedPreferences.getString(Constants.KEY_USER_NAME,"invalid user name")
        val userEmail = sharedPreferences.getString(Constants.KEY_USER_EMAIL,"invalid user email")
        val userPassword = sharedPreferences.getString(Constants.KEY_USER_PASSWORD,"invalid user password")

        if(!userId.contentEquals("invalid user id")
            && !userName.contentEquals("invalid user name")
            && !userEmail.contentEquals("invalid user email")
            && !userPassword.contentEquals("invalid user password")){
            Intent(this,MainActivity::class.java).apply {
                putExtra(Constants.KEY_USER_ID,userId)
                putExtra(Constants.KEY_USER_NAME,userName)
                putExtra(Constants.KEY_USER_EMAIL,userEmail)
                putExtra(Constants.KEY_USER_PASSWORD,userPassword)
                startActivity(this)
                finish()
            }
        }
    }
}