package com.example.myjigsaw

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.myjigsaw.layout.Layout
import com.example.myjigsaw.layout.LayoutServiceFacade
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {

    private val layoutServiceFacade = LayoutServiceFacade()
    //supportFragmentManager Is The Class Responsible For Performing Actions On The App's Fragments,
    //Such As Adding, Removing, Or Replacing Them, And Adding Them To The Back Stack.
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        //Get The First Fragment => The Home Page / Login Page
        updateView(layoutServiceFacade.currentLayout, layoutServiceFacade.position)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.logout ->{
                //Create An Alert Builder To Show Message & Show It
                val builder = AlertDialog.Builder(this)
                builder.setTitle("LOGOUT")
                builder.setMessage("Are You Sure You Want To Log Out ?")
                builder.setCancelable(false)

                builder.setPositiveButton("YES") { dialog, which ->
                    FirebaseAuth.getInstance().signOut();//SignOut From Firebase Auth Service

                    //Set loggedIn To False
                    val sharedPreference =  getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
                    var editor = sharedPreference.edit()
                    editor.putBoolean("loggedIn", false)
                    editor.commit()

                    //Go To The Login Page
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

                builder.setNegativeButton("NO") { dialog, which ->
                    dialog.dismiss()
                }

                builder.show()

                return true
            }
            R.id.scores ->{
            //Open The Scores Page
                val intent = Intent(this, ScoreActivity::class.java)
                startActivity(intent)
            return true
        }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    //On Back Pressed We Leave The App
    override fun onBackPressed() = this.finishAffinity()

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