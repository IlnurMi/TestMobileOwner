package com.ilnur.mobileowner.domain.models.response

import com.google.gson.annotations.SerializedName

class MonitoringInfo(
    @SerializedName("pathLength")
    val pathLength: String,
    @SerializedName("averageSpeed")
    val averageSpeed: String,
    @SerializedName("mileageStart")
    val mileageStart: String,
    @SerializedName("pageNum")
    val pageNum: String,
    @SerializedName("count")
    val count: String,
    @SerializedName("page")
    val page: String,
    @SerializedName("pathLengthString")
    val pathLengthString: String,
    @SerializedName("averageSpeedString")
    val averageSpeedString: String,
    @SerializedName("mileageStartString")
    val mileageStartString: String
) {
}