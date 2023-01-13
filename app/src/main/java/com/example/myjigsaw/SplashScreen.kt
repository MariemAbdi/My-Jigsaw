package com.example.myjigsaw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
//Splash Screen Page Using Zoom Animation
class SplashScreen : AppCompatActivity() {
    var zoom: Animation? = null
    var img: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        zoom = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom)
        img = findViewById(R.id.image)
        img?.startAnimation(zoom)
        val h = Handler()
        h.postDelayed({
            val i = Intent(applicationContext, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 2500)
    }
}