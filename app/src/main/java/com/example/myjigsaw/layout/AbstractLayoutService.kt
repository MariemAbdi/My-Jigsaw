package com.example.myjigsaw.layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myjigsaw.MainActivity

abstract class AbstractLayoutService(protected val layout: Layout) : Fragment() {

    var position: Int = 0

    abstract fun setupLayout(position: Int)
    abstract fun onBackPressed()

    //Will Be Used In The Gris & Puzzle Services To Change The Fragment
    fun switchView(target: Layout) = (activity as MainActivity).updateView(target, position)

    //Preserving The UI's layout and position When Activity Is In The Background
    open fun saveState(bundle: Bundle) {
        bundle.putInt("LAYOUT", layout.value)
        bundle.putInt("POSITION", position)
    }

    //Restoring The State When Activity Is Back To Be A Foreground Activity => Layout's Position
    open fun restoreState(bundle: Bundle) {
        position = bundle.getInt("POSITION")
    }

    //Creating The View Depending On The Selected Layout's Value
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(layout.value, container, false)
    }

    //On Activity Start We Set Up The Selected Layout Per Its Position
    override fun onStart() {
        super.onStart()
        setupLayout(position)
    }

    //Used For Restoring The Layout In On RestoreState In The LayoutServiceFacade File
    companion object {
        fun restoreLayout(bundle: Bundle): Layout = Layout.createFromInt(bundle.getInt("LAYOUT"))
    }
}