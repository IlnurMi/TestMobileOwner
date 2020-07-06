package com.ilnur.mobileowner.domain.models.request

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cars")
class CarsModel(
    @PrimaryKey()
    @ColumnInfo(name = "car_id")
    var carId: Int? = null,
    @ColumnInfo(name = "car_name")
    var name: String? = "",
    @ColumnInfo(name = "car_lat")
    var lat: Float? = null,
    @ColumnInfo(name = "car_lon")
    var lon: Float? = null,
    @ColumnInfo(name = "car_description")
    var description: String? = "null",
    @ColumnInfo(name = "car_imei")
    var imei: String? = "",
    @ColumnInfo(name = "car_vin")
    var vin: String? ="",
    @ColumnInfo(name = "car_last_date")
    var lastDate: String? = "",
    @ColumnInfo(name = "car_speed")
    var speed: Float? = null,
    @ColumnInfo(name = "car_model")
    var model: String? = ""
)