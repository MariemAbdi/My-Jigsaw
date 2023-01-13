package com.example.myjigsaw

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import com.example.myjigsaw.layout.Layout
import com.example.myjigsaw.layout.LayoutServiceFacade
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File


class MainActivity : AppCompatActivity() {

    private val layoutServiceFacade = LayoutServiceFacade()
    //supportFragmentManager Is The Class Responsible For Performing Actions On The App's Fragments,
    //Such As Adding, Removing, Or Replacing Them, And Adding Them To The Back Stack.
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        //Get The First Fragment => The Home Page
        updateView(layoutServiceFacade.currentLayout, layoutServiceFacade.position)
    }


    //On Back Pressed We Call The Function Of The Current Layout Displayed
    override fun onBackPressed() = layoutServiceFacade.current.onBackPressed()


    //Used To Update The Current View's Layout Name & Position
    fun updateView(layout: Layout, position: Int) {
        layoutServiceFacade.currentLayout = layout
        layoutServiceFacade.position = position
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, layoutServiceFacade.current)
        fragmentTransaction.commit()
    }


    //Preserving The UI's State If Activity Is In The Background
    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        layoutServiceFacade.saveState(bundle)
    }


    //Restoring The State When Activity Is Back To Be A Foreground Activity Restoring The Home Page
    override fun onRestoreInstanceState(bundle: Bundle) {
        super.onRestoreInstanceState(bundle)

        layoutServiceFacade.restoreState(bundle)
        updateView(layoutServiceFacade.currentLayout, layoutServiceFacade.position)
    }

}