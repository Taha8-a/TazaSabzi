package com.example.tazasabziapp.splash_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.tazasabziapp.R
import com.example.tazasabziapp.SignupActivity


class SplashActivity : AppCompatActivity() {

    private val handler = Handler()
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        runnable = Runnable {
            Intent(this, SignupActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }

        handler.postDelayed(runnable, 2000)
    }
}