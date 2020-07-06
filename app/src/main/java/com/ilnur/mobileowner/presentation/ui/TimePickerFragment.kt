package com.ilnur.mobileowner.presentation.ui

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment(private var textView: TextView): DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private var date: String? = ""
    //constructor for send data
    constructor(textView: TextView, date: String): this(textView){
        this.date = date
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        val second = c.get(Calendar.SECOND)
        return TimePickerDialog(activity,this,hour, minute, true)
    }



    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val time: String = "${plug(hourOfDay)}:${plug(minute)}"
        if (date!!.isEmpty())
            textView.text = "${textView.text} $time"
        else
            textView.text = "$date $time"
    }

    private fun plug(value: Int): String {
        if(value < 10)
            return "0$value"
        return "$value"
    }

    override fun onCancel(dialog: DialogInterface) {
        if (date!!.isNotEmpty())
            textView.text = date
        super.onCancel(dialog)
    }

}