package com.example.myjigsaw.layout.grid

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.WindowManager
import com.example.myjigsaw.repo.ImageRepository
import com.facebook.drawee.view.SimpleDraweeView

class GridImageAdapter(private val mContext: Context,
    private val rownum: Int, private val clickListener: (Int) -> Unit) : RecyclerView.Adapter<GridImageAdapter.PartViewHolder>() {

    //Acts Like A Constructor/Initializer
    class PartViewHolder(val imageView: SimpleDraweeView) : RecyclerView.ViewHolder(imageView) {
        fun bind(position: Int, clickListener: (Int) -> Unit) {
            itemView.isClickable=true
            itemView.setOnClickListener { clickListener(position) }
        }
    }

    //Creating An Imageview Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartViewHolder {
        val imageView = SimpleDraweeView(mContext)

        val imageWidth: Int = getImageSize(rownum)

        imageView.layoutParams = ViewGroup.LayoutParams(imageWidth, imageWidth)
        imageView.setPadding(16, 16, 16, 16)

        return PartViewHolder(imageView)
    }

    //Filling The Holder
    override fun onBindViewHolder(holder: PartViewHolder, position: Int) {
        val imageId = ImageRepository.getImageId(position)
        if (imageId != null) {
            holder.imageView.setImageURI("res:/$imageId")
        }
        holder.bind(position, clickListener)
    }

    //Returns The Size Of The Recyclerview
    override fun getItemCount() = ImageRepository.getCount()

    //Used To Calculate The Imageview's Width
    private fun getImageSize(rownum: Int): Int {
        val window: WindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        window.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels / rownum
    }
}