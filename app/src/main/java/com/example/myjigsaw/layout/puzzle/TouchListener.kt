package com.example.myjigsaw.layout.puzzle

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.myjigsaw.layout.Layout
import kotlinx.android.synthetic.main.puzzle_layout.*
import java.lang.Math.*
import kotlin.system.measureTimeMillis

//Interface Definition For A Callback To Be Invoked When A Touch Event Is Dispatched To The Puzzle View.
// The Callback Will Be Invoked Before The Touch Event Is Given To The View.
class TouchListener(private val puzzleService: PuzzleService) : View.OnTouchListener {
    private var xDelta: Float = 0.toFloat()
    private var yDelta: Float = 0.toFloat()

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val x = motionEvent.rawX
        val y = motionEvent.rawY
        val tolerance = sqrt(pow(view.width.toDouble(), 2.0) + pow(view.height.toDouble(), 2.0)) / 5

        val piece = view as PuzzlePiece
        if (!piece.canMove) {
            return true
        }

        val lParams = view.getLayoutParams() as RelativeLayout.LayoutParams
        when (motionEvent.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                xDelta = x - lParams.leftMargin
                yDelta = y - lParams.topMargin
                piece.bringToFront()
            }
            MotionEvent.ACTION_MOVE -> {
                lParams.leftMargin = (x - xDelta).toInt()
                lParams.topMargin = (y - yDelta).toInt()
                view.setLayoutParams(lParams)
            }
            MotionEvent.ACTION_UP -> {
                val xDiff = abs(piece.xCoord - lParams.leftMargin)
                val yDiff = abs(piece.yCoord - lParams.topMargin)

                //If The Piece Is In Its Places Then It Can Be No Longer Moved
                //Else It's Returned To The Drawer
                if (xDiff <= tolerance && yDiff <= tolerance) {
                    lParams.leftMargin = piece.xCoord
                    lParams.topMargin = piece.yCoord
                    piece.layoutParams = lParams
                    piece.canMove = false
                    sendViewToBack(piece)
                    if (isGameOver()) {
                        puzzleService.pieces = null
                        val end = System.currentTimeMillis()
                        Toast.makeText(puzzleService.context,"Congrats! You Finished The Puzzle In ${((end-puzzleService.begin)/1000)/60}min ${((end-puzzleService.begin)/1000)%60}sec",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    puzzleService.putPieceInDrawer(piece)
                }
            }
        }
        return true
    }

    //Used To Place The Puzzle Piece
    private fun sendViewToBack(child: View) {
        val parent = child.parent as ViewGroup?
        if (null != parent) {
            parent.removeView(child)
            parent.addView(child, 0)
        }
    }

    //It's Not Game Over Yet When There Are Pieces That Can Still Be Moved
    private fun isGameOver(): Boolean {
        if (puzzleService.pieces != null) {
            for (piece in puzzleService.pieces!!) {
                if (piece.canMove) {
                    return false
                }
            }
            return true
        } else return false
    }
}