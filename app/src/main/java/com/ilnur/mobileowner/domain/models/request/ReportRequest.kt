package com.ilnur.mobileowner.domain.models.request

import com.google.gson.annotations.SerializedName
import com.ilnur.mobileowner.domain.models.IPojo

class ReportRequest: IPojo.JsonArray {
    @SerializedName("reportId")
    var reportId: Int? = null
    @SerializedName("report")
    var report: String? = null
    @SerializedName("readiness")
    var readiness: Boolean? = null
    @SerializedName("groupId")
    var groupId: String? = null
    @SerializedName("date")
    var date: String? = null
    override fun toString(): String = report.toString()
}