package com.ilnur.mobileowner.data.repositories.map

import io.reactivex.Flowable
import io.reactivex.Single
import com.ilnur.mobileowner.data.network.RestService
import com.ilnur.mobileowner.data.preferences.PreferenceRepository
import com.ilnur.mobileowner.domain.models.request.FirebaseModel
import com.ilnur.mobileowner.domain.models.request.MonitoringCarInfoRequest
import com.ilnur.mobileowner.domain.models.request.MonitoringRequest
import com.ilnur.mobileowner.domain.models.request.TrackInfoRequest
import com.ilnur.mobileowner.domain.models.MainResponceArray
import com.ilnur.mobileowner.domain.models.MainResponceObject
import com.ilnur.mobileowner.domain.models.response.CarInfo
import com.ilnur.mobileowner.domain.models.response.CustomResult
import com.ilnur.mobileowner.domain.models.response.route.CarOnMap
import com.ilnur.mobileowner.interfaces.repositories.map.MapRepositoryInterface

class MapRepository(private val restService: RestService, private val firebaseService: RestService):
    MapRepositoryInterface {



    override fun getCars(): Flowable<MainResponceArray<CarOnMap>> {
        return restService
                .getCarsOnMap(PreferenceRepository.INSTANCE.getToken())
    }

    override fun getCarInfo(Imei: String): Flowable<MainResponceObject<CarInfo>> {
        return restService
                .getCarInfo(PreferenceRepository.INSTANCE.getToken(), Imei)
    }

    override fun getTrackInfo(monitoringTrackRequest: MonitoringCarInfoRequest): Flowable<TrackInfoRequest> {
        return  restService.getTrackInfo(PreferenceRepository.INSTANCE.getToken(),monitoringTrackRequest)
    }

    override fun getMonitoringInfo(monitoringRequest: MonitoringCarInfoRequest): Flowable<MonitoringRequest> {
        return  restService.getMonitoringInfo(PreferenceRepository.INSTANCE.getToken(),monitoringRequest)
    }

    override fun pushFirebaseToken(firebaseModel: FirebaseModel): Single<CustomResult> {
        return firebaseService.pushToken(PreferenceRepository.INSTANCE.getToken(),firebaseModel)
    }
}