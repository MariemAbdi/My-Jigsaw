package com.example.myjigsaw.repo

import com.example.myjigsaw.R


class ImageRepository {
    companion object {

        private val repo = listOf<Int>(
            R.drawable.drawable1,
            R.drawable.drawable2,
            R.drawable.drawable3,
            R.drawable.drawable4,
            R.drawable.drawable5,
            R.drawable.drawable6,
            R.drawable.drawable7,
            R.drawable.drawable8,
            R.drawable.drawable9,
            R.drawable.drawable10,
            R.drawable.drawable11,
            R.drawable.drawable12,
            R.drawable.drawable13,
            R.drawable.drawable14,
            R.drawable.drawable15,
            R.drawable.drawable16,
            R.drawable.drawable17,
            R.drawable.drawable18,
            R.drawable.drawable19,
            R.drawable.drawable20,
            R.drawable.drawable21,
            R.drawable.drawable22,
            R.drawable.drawable23,
            R.drawable.drawable24,
            R.drawable.drawable25,
            R.drawable.drawable26,
            R.drawable.drawable27,
            R.drawable.drawable28,
            R.drawable.drawable29,
            R.drawable.drawable30,
            R.drawable.drawable31,
            R.drawable.drawable32
        ).shuffled()


        //The Size Of The Array Of Images
        fun getCount(): Int = repo.size

        //Returning The Id Of The Current Image
        fun getImageId(position: Int): Int? = if (position >= 0 && position < repo.size) repo[position] else null
    }
}