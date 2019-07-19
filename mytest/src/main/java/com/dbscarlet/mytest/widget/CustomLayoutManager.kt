package com.dbscarlet.mytest.widget

import android.support.v7.widget.RecyclerView

/**
 * Created by Daibing Wang on 2019/7/19.
 */
class CustomLayoutManager: RecyclerView.LayoutManager() {
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT)
    }

}