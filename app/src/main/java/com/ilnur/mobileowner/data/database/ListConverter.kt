package com.ilnur.mobileowner.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {
    companion object {
        private var  INSTANCE: ListConverter? = null

        fun getInstance(): ListConverter {
            if(INSTANCE == null)
                INSTANCE = ListConverter()
            return INSTANCE as ListConverter
        }
    }

    @TypeConverter
    fun listToJson(value: List<String>?): String = Gson().toJson(value)

    @TypeConverter
    fun listFromJson(value: String?): List<String> {
        val listType = object : TypeToken<List<String>>() { }.type
        val newList = Gson().fromJson<List<String>>(value, listType)
        return newList
    }
}