package com.ilnur.mobileowner.presentation.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.ilnur.mobileowner.interfaces.views.report.ReportDateTimeListener
import java.util.*

@SuppressLint("ValidFragment")
class DatePickerFragment (private var textView: TextView) : DialogFragment(),
        DatePickerDialog.OnDateSetListener {
    private var listener: ReportDateTimeListener? = null
    private var dataFlags: Boolean = false
    //constructor for send time listener
    constructor(textView: TextView, dateTimeListener: ReportDateTimeListener): this(textView){
        listener = dateTimeListener
    }

    //constructor for send more dataFlags
    constructor(textView: TextView, dateTimeListener: ReportDateTimeListener, dataFlags: Boolean): this(textView, dateTimeListener){
        this.dataFlags = dataFlags
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireContext(), this, year, month, day)
    }
    
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "${plug(dayOfMonth)}.${plug(month + 1)}.${year}"
        if (dataFlags)
            listener?.startTimePicker(textView, date)
        else{
            textView.text = date
            listener?.startTimePicker(textView)
        }
    }

    private fun plug(value: Int): String {
        if(value < 10)
            return "0$value"
        return "$value"
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }
}