package com.example.myjigsaw.layout.puzzle

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.widget.AppCompatImageView


class PuzzlePiece(context: Context) : AppCompatImageView(context) {

    constructor(
        context: Context,
        imageBitmap: Bitmap,
        xCord: Int,
        yCord: Int,
        pieceWidth: Int,
        pieceHeight: Int
    ) : this(context) {
        this.setImageBitmap(imageBitmap)
        this.xCoord = xCord
        this.yCoord = yCord
        this.pieceWidth = pieceWidth
        this.pieceHeight = pieceHeight
    }

    var xCoord: Int = 0
    var yCoord: Int = 0
    var pieceWidth: Int = 0
    var pieceHeight: Int = 0
    var canMove = true
}