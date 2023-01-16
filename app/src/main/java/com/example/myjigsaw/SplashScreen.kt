package com.example.myjigsaw

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

//Splash Screen Page Using Zoom Animation
class SplashScreen : AppCompatActivity() {
    var zoom: Animation? = null
    var img: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        zoom = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom)
        img = findViewById(R.id.image)
        img?.startAnimation(zoom)
        val sharedPreference =  getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val loggedIn=sharedPreference.getBoolean("loggedIn",false)
        val h = Handler()
        h.postDelayed({
            if (loggedIn) {
                val i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
            }else{
                val i = Intent(applicationContext, LoginActivity::class.java)
                startActivity(i)
            }
        }, 2000)
    }
}