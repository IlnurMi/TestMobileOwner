package com.ilnur.mobileowner.domain.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ilnur.mobileowner.domain.models.IPojo

class CarInfo : IPojo.JsonObject {

    @SerializedName("PacketId")
    @Expose
    var packetId: String? = null
    @SerializedName("RealTime")
    @Expose
    var realTime: String? = null
    @SerializedName("time")
    @Expose
    var time: String? = null
    @SerializedName("lat")
    @Expose
    var latitude: String? = null
    @SerializedName("lon")
    @Expose
    var longitude: String? = null
    @SerializedName("speed")
    @Expose
    var speed: String? = null
    @SerializedName("height")
    @Expose
    var height: String? = null
    @SerializedName("Angle")
    @Expose
    var angle: String? = null
    @SerializedName("mileage")
    @Expose
    var mileage: String? = null
    @SerializedName("Fuel")
    @Expose
    var fuel: String? = null
    @SerializedName("Temperature")
    @Expose
    var temperature: String? = null
    @SerializedName("DateTime")
    @Expose
    var lastOnlineDate: String? = null


}