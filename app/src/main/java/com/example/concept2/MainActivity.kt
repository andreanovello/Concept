package com.example.concept2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    lateinit var logoImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logoImage = findViewById(R.id.logo)
        logoImage.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}