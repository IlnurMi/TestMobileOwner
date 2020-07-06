package com.ilnur.mobileowner.domain.models.request

import com.google.gson.annotations.SerializedName

class WayObject(@SerializedName("lat")
                val lat: String,
                @SerializedName("lng")
                val lng: String,
                @SerializedName("angle")
                val angle: String,
                @SerializedName("realTime")
                val realTime: String,
                @SerializedName("time")
                val time: String,
                @SerializedName("speed")
                val speed: String,
                @SerializedName("mileage")
                val mileage: String,
                @SerializedName("mileageStart")
                val mileageStart: String,
                @SerializedName("temperature")
                val temperature: String,
                @SerializedName("speedString")
                val speedString: String,
                @SerializedName("mileageString")
                val mileageString: String,
                @SerializedName("mileageStartString")
                val mileageStartString: String,
                @SerializedName("temperatureString")
                val temperatureString: String
) {
}