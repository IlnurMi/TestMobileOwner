package com.ilnur.mobileowner.interfaces.views.report

import android.widget.TextView

interface ReportDateTimeListener {
    fun startTimePicker(textView: TextView)
    fun startTimePicker(textView: TextView, date: String)
}