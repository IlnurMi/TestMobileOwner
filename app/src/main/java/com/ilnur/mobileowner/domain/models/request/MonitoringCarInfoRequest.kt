package com.ilnur.mobileowner.domain.models.request

class MonitoringCarInfoRequest {
    var carId: Int = 0
    var datetimeBegin: String? = ""
    var datetimeEnd: String? = ""
    var mileageStart: Double = 0.0
    var pageNum: Int = 0
    var count: Int = 0
}