package com.example.myjigsaw.layout.grid

import androidx.recyclerview.widget.RecyclerView
import android.widget.AbsListView

class GridOnScrollListener : RecyclerView.OnScrollListener() {

    //This Will Be Called Whenever The User Scrolls (In The Grid Recycler)
    //To Make The Scrolling Action Smoother
    override fun onScrollStateChanged(view: RecyclerView, scrollState: Int) {
        super.onScrollStateChanged(view, scrollState)

        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            val firstVisibleItem = view.getChildAt(0)
            val offset = Math.abs(firstVisibleItem.top)
            val height = firstVisibleItem.height

            val threshold = 2.5
            if (offset < (height / threshold)) {
                view.smoothScrollBy(0, firstVisibleItem.top)
            } else if (offset > (height * (threshold - 1) / threshold)) {
                view.smoothScrollBy(0, firstVisibleItem.bottom)
            }
        }
    }
}
