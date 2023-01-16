package com.example.myjigsaw.layout.puzzle

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Surface
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.myjigsaw.LoginActivity
import com.example.myjigsaw.MyApplication.Companion.number
import com.example.myjigsaw.R
import com.example.myjigsaw.layout.AbstractLayoutService
import com.example.myjigsaw.layout.Layout
import com.example.myjigsaw.repo.ImageRepository
import java.lang.Math.abs
import java.util.*


class PuzzleService : AbstractLayoutService(Layout.PUZZLE) {

    var pieces: ArrayList<PuzzlePiece>? = null

    private val touchListener = TouchListener(this)

    private var imageTopPosition: Int = 0
    private var imageLeftPosition: Int = 0
    private val padding: Int = 48
    private var drawerWidth = 0
    private var drawerHeight = 0
    private var drawerTop = 0
    private var drawerLeft = 0

    private var piecesOnEachSide = 0

    //Used To Calculate The Time Elapsed To Finish The Game
    var begin : Long = 0

    //Here We Call The Functions Responsible Of Creating Both The Drawer And Puzzle Pieces
    override fun setupLayout(position: Int) {
        this.position = position
        val layout: RelativeLayout = activity!!.findViewById(R.id.layout)

            piecesOnEachSide = number
            begin = System.currentTimeMillis()//The Time The Game Started

        layout.post {
            //Creating The Puzzle's Bitmap Then Inserting The Puzzle's Preview Image Into It
            val puzzleBitmap = createScaledPuzzleBitmap(layout.width, layout.height)
            putPuzzleBitmapInLayout(puzzleBitmap, layout)

            createDrawer(puzzleBitmap.width, puzzleBitmap.height, layout)

            createPuzzlePieces(puzzleBitmap, layout)
        }
    }

    //On Back Pressed We Set The Pieces Array Back To Null & Go Back To The Home Page
    override fun onBackPressed() {
        pieces = null
        switchView(Layout.GRID)
    }

    //Randomly Putting The Puzzle Pieces In The Drawer Then Adding An Animation
    fun putPieceInDrawer(piece: PuzzlePiece) {
        val x = Random().nextInt(drawerWidth - piece.pieceWidth) + drawerLeft
        val y = Random().nextInt(drawerHeight - piece.pieceHeight) + drawerTop

        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val params = piece.layoutParams as RelativeLayout.LayoutParams

                params.leftMargin += ((x - params.leftMargin) * interpolatedTime).toInt()
                params.topMargin += ((y - params.topMargin) * interpolatedTime).toInt()
                piece.layoutParams = params
            }
        }
        a.duration = 500
        piece.startAnimation(a)
    }

    //Preserving The UI's State If Activity Is In The Background
    override fun saveState(bundle: Bundle) {
        super.saveState(bundle)
        bundle.putSerializable("PIECES", pieces)
    }

    //Restoring The State When Activity Is Back To Be A Foreground Activity
    override fun restoreState(bundle: Bundle) {
        super.restoreState(bundle)
        pieces = bundle.getSerializable("PIECES") as ArrayList<PuzzlePiece>
    }

    //Divide The Pieces Array Into Puzzle Pieces Then Place Each One In The Drawer
    private fun createPuzzlePieces(puzzleBitmap: Bitmap, layout: RelativeLayout) {
        pieces = splitImage(puzzleBitmap)
        for (piece in pieces!!) {
            piece.setOnTouchListener(touchListener)
            layout.addView(piece)
            if (piece.canMove) {
                putPieceInDrawer(piece)
            } else {
                val pieceParams = piece.layoutParams as RelativeLayout.LayoutParams
                pieceParams.leftMargin = piece.xCoord
                pieceParams.topMargin = piece.yCoord
                piece.layoutParams = pieceParams
            }
        }
    }

    //Create The Drawer Where The Puzzle Pieces Will Be Randomly Placed
    private fun createDrawer(puzzleBitmapWidth: Int, puzzleBitmapHeight: Int, layout: RelativeLayout) {
        //Setting Its Size Based On The Device's Orientation
        if (isPortrait()) {
            drawerWidth = layout.width - 2 * padding
            drawerHeight = layout.height - puzzleBitmapHeight - 3 * padding
        } else {
            drawerWidth = layout.width - puzzleBitmapWidth - 3 * padding
            drawerHeight = layout.height - 2 * padding
        }
        //Create The drawer Which Is An ImageView Having The Color Gray & Add It To The Layout
        val drawerImage = Bitmap.createBitmap(drawerWidth, drawerHeight, Bitmap.Config.ARGB_8888)
        drawerImage.eraseColor(android.graphics.Color.LTGRAY)
        val solidImage = ImageView(activity)
        solidImage.setImageBitmap(drawerImage)
        layout.addView(solidImage)

        //Setting Its Margins Based On The Device's Orientation
        drawerLeft = if (isPortrait()) padding else puzzleBitmapWidth + 2 * padding
        drawerTop = if (isPortrait()) puzzleBitmapHeight + 2 * padding else padding
        val drawerParams = solidImage.layoutParams as RelativeLayout.LayoutParams
        drawerParams.leftMargin = drawerLeft
        drawerParams.topMargin = drawerTop

        solidImage.layoutParams = drawerParams
    }

    //The Puzzle's Layout => Where The Preview Is Showing & Where We Will Place The Pieces
    private fun putPuzzleBitmapInLayout(puzzleBitmap: Bitmap, layout: RelativeLayout) {
        val puzzleImageView = ImageView(activity!!.applicationContext)//Create An ImageView
        puzzleImageView.setImageBitmap(puzzleBitmap)//Set The Preview Image
        puzzleImageView.alpha = 0.1.toFloat() // The Preview's Transparency
        layout.addView(puzzleImageView)//Add Image To Layout

        //Then Based On Th Device's Orientation We Set The Paddings
        if (isPortrait()) {
            this.imageLeftPosition = (layout.width - puzzleBitmap.width) / 2
            this.imageTopPosition = this.padding
        } else {
            this.imageTopPosition = (layout.height - puzzleBitmap.height) / 2
            this.imageLeftPosition = this.padding
        }

        //Setting The Margins
        val puzzleImageParams = puzzleImageView.layoutParams as RelativeLayout.LayoutParams
        puzzleImageParams.leftMargin = this.imageLeftPosition
        puzzleImageParams.topMargin = this.imageTopPosition

        puzzleImageView.layoutParams = puzzleImageParams
    }

    //Creating The Puzzle's Preview Place => This Is Where The Puzzle Pieces Will Be Places By tHe Player
    private fun createScaledPuzzleBitmap(maxWidth: Int, maxHeight: Int): Bitmap {
        val srcBitmap = BitmapFactory.decodeResource(resources, ImageRepository.getImageId(position)!!)
        val scale = getScale(srcBitmap.width, srcBitmap.height, maxWidth, maxHeight)
        return Bitmap.createScaledBitmap(
            srcBitmap,
            (srcBitmap.width * scale).toInt(),
            (srcBitmap.height * scale).toInt(),
            true
        )
    }

    //Splitting The Clicked Image Into Equal Pieces Having The Same Width/Height
    private fun splitImage(puzzleBitmap: Bitmap): ArrayList<PuzzlePiece> {
        //Setting The Number Of Puzzle Pieces On Both Rows & Columns
        val rows = piecesOnEachSide
        val cols = piecesOnEachSide

        //Creating An Array Having A Size Based On The Chosen Rows/Cols
        val localPieces: ArrayList<PuzzlePiece> = ArrayList(rows * cols)

        // Calculate the With & Height Of The Pieces
        val pieceWidth = abs(puzzleBitmap.width / cols)
        val pieceHeight = abs(puzzleBitmap.height / rows)

        // Create Each Bitmap Piece & Add It To The Resulting Array
        var y = 0
        for (row in 0 until rows) {
            var x = 0
            for (col in 0 until cols) {
                val pieceBitmap = Bitmap.createBitmap(puzzleBitmap, x, y, pieceWidth, pieceHeight)
                val piece = PuzzlePiece(
                    activity!!.applicationContext,
                    pieceBitmap,
                    x + this.imageLeftPosition,
                    y + this.imageTopPosition,
                    pieceWidth,
                    pieceHeight
                )
                localPieces.add(piece)
                x += pieceWidth
            }
            y += pieceHeight
        }

        if (this.pieces != null && localPieces.size == this.pieces!!.size) {
            for (i in 0 until localPieces.size) {
                localPieces[i].canMove = this.pieces!![i].canMove
            }
        }
        return localPieces
    }

    //Calculate The Needed Scale Depending On The Device's Orientation
    private fun getScale(bitmapWidth: Int, bitmapHeight: Int, layoutWidth: Int, layoutHeight: Int): Double {
        var maxHeight: Int = layoutHeight - padding * 2
        var maxWidth: Int = layoutWidth - padding * 2

        if (isPortrait()) {
            maxHeight = (maxHeight * 0.5).toInt()
        } else {
            maxWidth = (maxWidth * 0.5).toInt()
        }

        val widthScale: Double = maxWidth / bitmapWidth.toDouble()
        val heightScale: Double = maxHeight / bitmapHeight.toDouble()

        return if (widthScale < heightScale) widthScale else heightScale
    }

    //We Get The Device's Orientation => Portrait OR Landscape
    private fun getRotation(): Int {
        val window: WindowManager = activity!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return window.defaultDisplay.rotation
    }

    //If The Device's Orientation Is Portrait It Returns True
    private fun isPortrait(): Boolean {
        val rotation = getRotation()
        return rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180
    }
}