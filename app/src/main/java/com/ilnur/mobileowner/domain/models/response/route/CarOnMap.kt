package com.ilnur.mobileowner.domain.models.response.route

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ilnur.mobileowner.domain.models.IPojo

class CarOnMap() : IPojo.JsonArray, Parcelable {
    @SerializedName("carId")
    var carId: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("lat")
    var lat: String? = null
    @SerializedName("lon")
    var lon: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("imei")
    var imei: String? = null
    @SerializedName("vin")
    var vin: String? = null
    @SerializedName("lastDate")
    var lastDate: String? = null
    @SerializedName("speed")
    var speed: Int? = null
    @SerializedName("model")
    var model: String? = null

    constructor(parcel: Parcel) : this() {
        carId = parcel.readString()
        name = parcel.readString()
        lat = parcel.readString()
        lon = parcel.readString()
        description = parcel.readString()
        imei = parcel.readString()
        vin = parcel.readString()
        lastDate = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(carId)
        parcel.writeString(name)
        parcel.writeString(lat)
        parcel.writeString(lon)
        parcel.writeString(description)
        parcel.writeString(imei)
        parcel.writeString(vin)
        parcel.writeString(lastDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CarOnMap> {
        override fun createFromParcel(parcel: Parcel): CarOnMap {
            return CarOnMap(parcel)
        }

        override fun newArray(size: Int): Array<CarOnMap?> {
            return arrayOfNulls(size)
        }
    }
}