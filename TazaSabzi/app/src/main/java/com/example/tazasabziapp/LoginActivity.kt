package com.example.tazasabziapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    private lateinit var btn_login:Button
    private lateinit var signuplink:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login = findViewById(R.id.btn_login)

        signuplink = findViewById(R.id.signuplink)

        btn_login.setOnClickListener {
            intent = Intent(this, MainActivity::class.java).apply {
                startActivity(this)
            }
        }

       signuplink.setOnClickListener() {
            intent = Intent(this, SignupActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}