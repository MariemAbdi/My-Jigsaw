package com.example.myjigsaw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ScoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
    }

    //On Back Pressed We Leave The App
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}