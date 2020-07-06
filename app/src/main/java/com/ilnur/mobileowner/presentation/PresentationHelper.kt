package com.ilnur.mobileowner.presentation

import java.text.SimpleDateFormat

class PresentationHelper {
    companion object{
        var INSTANCE: PresentationHelper? = null

        fun getInstance(): PresentationHelper {
            if (INSTANCE == null)
                INSTANCE = PresentationHelper()
            return INSTANCE!!
        }

        fun newInstance(): PresentationHelper {
            INSTANCE = PresentationHelper()
            return INSTANCE!!
        }
    }

    fun comparisonDate(start: String, end: String): Boolean{
        val format = SimpleDateFormat("dd.MM.yyyy HH:mm")
        return format.parse(start).time <= format.parse(end).time
    }
}