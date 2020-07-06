package com.ilnur.mobileowner.interfaces.repositories.map

import com.ilnur.mobileowner.domain.models.request.FirebaseModel
import com.ilnur.mobileowner.domain.models.request.MonitoringCarInfoRequest
import com.ilnur.mobileowner.domain.models.request.MonitoringRequest
import com.ilnur.mobileowner.domain.models.request.TrackInfoRequest
import io.reactivex.Flowable
import io.reactivex.Single
import com.ilnur.mobileowner.domain.models.MainResponceArray
import com.ilnur.mobileowner.domain.models.MainResponceObject
import com.ilnur.mobileowner.domain.models.response.CarInfo
import com.ilnur.mobileowner.domain.models.response.CustomResult
import com.ilnur.mobileowner.domain.models.response.route.CarOnMap

interface MapRepositoryInterface {
    fun getCars(): Flowable<MainResponceArray<CarOnMap>>

    fun getCarInfo(Imei: String): Flowable<MainResponceObject<CarInfo>>

    fun getTrackInfo(monitoringCarInfoRequest: MonitoringCarInfoRequest): Flowable<TrackInfoRequest>

    fun getMonitoringInfo(monitoringCarInfoRequest: MonitoringCarInfoRequest): Flowable<MonitoringRequest>

    fun pushFirebaseToken(firebaseModel: FirebaseModel): Single<CustomResult>
}
