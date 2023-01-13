package com.example.myjigsaw.layout

import com.example.myjigsaw.R

//Layout Class To Define The Different Classes We Have
enum class Layout(val value: Int) {
    GRID(R.layout.grid_layout),//Home Page
    PUZZLE(R.layout.puzzle_layout),//Game Page
    LEVELS(R.layout.levels_layout);//Level Page

    //The Grid Layout Is The Default/Main Page
    companion object {
        fun createFromInt(layoutId: Int): Layout {
            return when (layoutId) {
                GRID.value -> GRID
                LEVELS.value -> LEVELS
                PUZZLE.value -> PUZZLE
                else -> GRID
            }
        }
    }

}