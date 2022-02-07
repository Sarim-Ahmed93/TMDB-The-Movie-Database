package com.example.tmdb_themoviedatabase.main.common

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView


internal class SquaredImageView : ImageView {
    private var mContext: Context? = null

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
    }

     override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth())
    }
}