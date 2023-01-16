package com.example.myjigsaw.layout

import android.os.Bundle
import com.example.myjigsaw.layout.Layout.*
import com.example.myjigsaw.layout.grid.GridService
import com.example.myjigsaw.layout.levels.LevelService
import com.example.myjigsaw.layout.puzzle.PuzzleService

class LayoutServiceFacade {
    //Getting Each Page's Service
    private val gridService: AbstractLayoutService = GridService()
    private val puzzleService: AbstractLayoutService = PuzzleService()
    private val levelsService: AbstractLayoutService = LevelService()

    var currentLayout: Layout = GRID //Serves The Initial Layout

    //The Current Layout Changes Depending On The Selected Layout
    val current: AbstractLayoutService
        get() = when (currentLayout) {
            GRID -> gridService
            LEVELS -> levelsService
            PUZZLE -> puzzleService
        }
    var position: Int
        get() = current.position
        set(position) {
            current.position = position
        }


    //Preserving The UI's State If Activity Is In The Background
    fun saveState(bundle: Bundle) = current.saveState(bundle)

    //Restoring The State When Activity Is Back To Be A Foreground Activity
    fun restoreState(bundle: Bundle){
        currentLayout = AbstractLayoutService.restoreLayout(bundle)
        current.restoreState(bundle)
    }
}