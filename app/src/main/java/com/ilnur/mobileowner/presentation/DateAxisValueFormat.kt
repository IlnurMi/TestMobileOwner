package com.ilnur.mobileowner.presentation

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat

class DateAxisValueFormat(): IAxisValueFormatter{
    private lateinit var mValues: Array<String>

    var sdf = SimpleDateFormat("yyyy.MM.dd.hh")

    fun DateAxisValueFormatter(values: Array<String>) {
        mValues = values
    }


    override fun getFormattedValue(value: Float, axis: AxisBase?): String? {
        return mValues[value.toInt()]
    }

}