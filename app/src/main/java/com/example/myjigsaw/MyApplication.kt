package com.example.myjigsaw

import android.app.Application
import com.example.myjigsaw.layout.Layout
import com.facebook.drawee.backends.pipeline.Fresco

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // This library can be used to load the images with the image URL
        //Here we're initializing it for future use
        Fresco.initialize(this)
    }

    companion object {
        var number = 0 //The Puzzle Pieces Number Depending On The Difficulty Level
    }
}