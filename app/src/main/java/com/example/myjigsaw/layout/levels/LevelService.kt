package com.example.myjigsaw.layout.levels

import android.widget.Button
import android.widget.RadioButton
import com.facebook.drawee.view.SimpleDraweeView
import com.example.myjigsaw.R
import com.example.myjigsaw.layout.AbstractLayoutService
import com.example.myjigsaw.layout.Layout
import com.example.myjigsaw.repo.ImageRepository
import com.example.myjigsaw.MyApplication.Companion.number

//Choosing The Difficulty Level & Setting The Number Of Pieces
class LevelService: AbstractLayoutService(Layout.LEVELS) {

    override fun setupLayout(position: Int) {
        this.position = position
        updateCurrentImage { position }

        //Unchecking The Previous Choice
        activity!!.findViewById<RadioButton>(R.id.easy).isChecked=false
        activity!!.findViewById<RadioButton>(R.id.medium).isChecked=false
        activity!!.findViewById<RadioButton>(R.id.hard).isChecked=false

        //If One Button Is Checked We're Forwarded To The Game Page Having The Number Of Puzzle Pieces Per Row
        //If No Level Is Chosen Nothing Happens
        activity!!.findViewById<RadioButton>(R.id.easy).setOnClickListener {
            number = 3
            activity!!.findViewById<Button>(R.id.start)
                .setOnClickListener { switchView(Layout.PUZZLE) }
        }

        activity!!.findViewById<RadioButton>(R.id.medium).setOnClickListener {
            number = 5
            activity!!.findViewById<Button>(R.id.start)
                .setOnClickListener { switchView(Layout.PUZZLE) }
        }

        activity!!.findViewById<RadioButton>(R.id.hard).setOnClickListener {
            number = 9
            activity!!.findViewById<Button>(R.id.start)
                .setOnClickListener { switchView(Layout.PUZZLE) }
        }
        }


    //On Back Pressed We Go Back To The Home Page
    override fun onBackPressed() = switchView(Layout.GRID)

    //Setting The Chosen Puzzle's Image
    private fun updateCurrentImage(getNewPosition: (Int) -> Int?) {
        val imageView = activity!!.findViewById<SimpleDraweeView>(R.id.imageView)

        val newPosition = getNewPosition(this.position)
        if (newPosition != null) {
            val imageId: Int? = ImageRepository.getImageId(newPosition)
            if (imageId != null) {
                imageView.setImageURI("res:/$imageId")
            }
        }
        this.position = newPosition!!
    }
}