package com.example.myjigsaw.layout.grid

import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Surface
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.example.myjigsaw.LoginActivity
import com.example.myjigsaw.R
import com.example.myjigsaw.ScoreActivity
import com.example.myjigsaw.layout.AbstractLayoutService
import com.example.myjigsaw.layout.Layout
import com.google.firebase.auth.FirebaseAuth


class GridService : AbstractLayoutService(Layout.GRID) {

    private var viewAdapter: RecyclerView.Adapter<*>? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: GridLayoutManager
    private val onScrollListener: GridOnScrollListener = GridOnScrollListener()
    private var rotation = Surface.ROTATION_0


    //Here We Set The Recycler View
    override fun setupLayout(position: Int) {
        val newRotation = getRotation()
        if (viewAdapter == null || newRotation != rotation) {
            rotation = newRotation
            viewAdapter = GridImageAdapter(activity!!, getRownum(), { pos: Int -> gridItemClicked(pos) })
        }
        viewManager = GridLayoutManager(activity, getRownum()) //To Make The Recycler Look Like A Grid View
        recyclerView = activity!!.findViewById<RecyclerView>(R.id.gridview).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        recyclerView.addOnScrollListener(onScrollListener)
        recyclerView.scrollToPosition(position)
    }


    //On Back Pressed We Finish The Activity To Go Back To The Previous One
    override fun onBackPressed() = activity!!.finish()

    //When The Grid Item Is Clicked We Get Forwarded To The Item's Puzzle Page
    private fun gridItemClicked(position: Int): Boolean {
        this.position = position
        switchView(Layout.LEVELS)
        return true
    }

    //We Get The Device's Orientation => Portrait OR Landscape
    private fun getRotation(): Int {
        val window: WindowManager = activity!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return window.defaultDisplay.rotation
    }

    //If The Device's Orientation Is Portrait Then We Have 2 Columns Else 4 Columns
    private fun getRownum(): Int {
        val rotation = getRotation()
        return if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) 2 else 4
    }
}