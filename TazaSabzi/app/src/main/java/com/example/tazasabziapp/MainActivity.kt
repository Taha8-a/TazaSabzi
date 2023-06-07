package com.example.tazasabziapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tazasabziapp.adapter.MyAdapterPurchase
import com.example.tazasabziapp.bottom_navigations_actions.CartActivity
import com.example.tazasabziapp.bottom_navigations_actions.SaleActivity
import com.example.tazasabziapp.bottom_navigations_actions.StockActivity
import com.example.tazasabziapp.contact.Product
import com.example.tazasabziapp.contact.Request
import com.example.tazasabziapp.contact.Response
import com.example.tazasabziapp.databinding.ActivityMainBinding
import com.example.tazasabziapp.network.IntRequestContact
import com.example.tazasabziapp.network.NetworkClient
import com.example.tazasabziapp.top_navbar_actions.AboutActivity
import com.example.tazasabziapp.top_navbar_actions.ProfileActivity
import com.example.tazasabziapp.top_navbar_actions.SettingActivity
import com.example.tazasabziapp.utils.Constants
import com.example.tazasabziapp.utils.DataProvider
import com.example.tazasabziapp.utils.showToast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item_sale.*
import kotlinx.android.synthetic.main.nav_header.*
import retrofit2.Call
import retrofit2.Callback


class MainActivity : AppCompatActivity(), Callback<Response> {

    lateinit var binding : ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var bottomNav : BottomNavigationView

    private lateinit var adapter: MyAdapterPurchase
    //-------------------------------------------------

    //backend logic variables for getting the sabzi products data
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


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        val navigationView : NavigationView = findViewById(R.id.navView)
        val headerView : View = navigationView.getHeaderView(0)
        val useName: TextView = headerView.findViewById(R.id.username)
        useName.text = getString(R.string.user_name,userName)
//-------------------------------------------------
        bottomNav = findViewById(R.id.menu_item)

        bottomNav.setOnItemSelectedListener { item ->

            when(item.itemId){
                R.id.stock_item -> {
                    //replaceFragment(HomeFragment())
                   val i = Intent(this@MainActivity, StockActivity::class.java)
                    startActivity(i)
                    true
                }
                R.id.home_item -> {
                   // replaceFragment(HomeFragment())
                    val i = Intent(this@MainActivity, SaleActivity::class.java)
                    startActivity(i)
                    true
                }
                R.id.cart_item -> {
                    //replaceFragment(CartFragment())
                    val i = Intent(this@MainActivity, CartActivity::class.java)
                    startActivity(i)
                    true
                }
            }

            true
        }

        binding.apply {

            toggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.firstItem -> {
                        val i = Intent(this@MainActivity, ProfileActivity::class.java)
                        startActivity(i)
                    }
                    R.id.secondtItem -> {
                        val i = Intent(this@MainActivity, AboutActivity::class.java)
                        startActivity(i)
                    }
                    R.id.thirdItem -> {
                        val i = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(i)

                    }
                    R.id.fourthItem -> {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("TazaSabzi App Alert")
                            .setMessage("Are you sure? You want to logout")
                            .setPositiveButton("Yes") { dialog, which ->
                                val editor = sharedPreferences.edit()
                                editor.clear().commit()
                                val i = Intent(this@MainActivity,SignupActivity::class.java)
                                    startActivity(i)
                            }
                            .setNegativeButton("No") { dialog, which ->
                                dialog?.dismiss()
                            }
                            .create()
                            .show()
                    }
                }
                true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val request = Request(
            action = Constants.GET_SABZI_PRODUCTS,
            userId = userId
        )
        val callResponse = requestContract.makeApiCall(request)
        callResponse.enqueue(this)
        //backend logic for getting all sabzi products

        recyclerViewPurchase.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerViewPurchase.setHasFixedSize(true)
        dataSource = DataProvider.response.allProducts
        if(dataSource.size>0){
            adapter = MyAdapterPurchase(this@MainActivity,this, dataSource)
            recyclerViewPurchase.adapter = adapter
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

// fragment replacement
    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null){
            val Transaction = supportFragmentManager.beginTransaction()
            Transaction.replace(R.id.fragment_container,fragment)
            Transaction.commit()
        }

    }
// drawer item selection
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }
}