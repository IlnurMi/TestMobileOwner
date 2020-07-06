package com.ilnur.mobileowner.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ilnur.mobileowner.domain.models.request.Place

class PlaceConverter {
    @TypeConverter
    fun placeObjToJson(place: Place?): String = Gson().toJson(place)

    @TypeConverter
    fun placeObjFromJson(value: String?): Place {
        return Gson().fromJson(value, Place::class.java)
    }

}