package com.ilnur.mobileowner.presentation.ui.map

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.ilnur.mobileowner.R

class CustomMarkerView(root: ViewGroup, text: String, isSelected: Boolean): FrameLayout(root.context) {
    private var mImage: ImageView
    private var mTitle: TextView

    init {
        View.inflate(context, R.layout.marker_layout, this)
        mImage = findViewById(R.id.marker_image)
        mTitle = findViewById(R.id.marker_title)
        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)

        mTitle.text = text
        if (isSelected){
            mImage.setImageResource(R.drawable.rd)
        }else{
            mImage.setImageResource(R.drawable.gr2)
        }
    }

}