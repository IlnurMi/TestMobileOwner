package com.ilnur.mobileowner.domain.interactors.map

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import com.ilnur.mobileowner.data.database.LocalDbRepository
import com.ilnur.mobileowner.data.repositories.map.MapRepository
import com.ilnur.mobileowner.domain.models.request.*
import com.ilnur.mobileowner.domain.models.MainResponceArray
import com.ilnur.mobileowner.domain.models.MainResponceObject
import com.ilnur.mobileowner.domain.models.response.CarInfo
import com.ilnur.mobileowner.domain.models.response.CustomResult
import com.ilnur.mobileowner.domain.models.response.route.CarOnMap

class MapInteractor(private val mapRepository: MapRepository, private val localDbRepository: LocalDbRepository) {

    fun getCarsFromDB(): Single<List<CarsModel>>{
        return localDbRepository.getCars()
    }

    fun insert(cars: List<CarOnMap>){
        for (item in cars){
            val carsModel = setCarsModel(item)
            addCar(carsModel)
        }

    }

    fun setCarOnMap(carsModel: CarsModel): CarOnMap {
        val carOnMap = CarOnMap()
        carOnMap.apply {
            carOnMap?.carId = carsModel.carId.toString()
            carOnMap?.name = carsModel.name
            carOnMap?.lat = carsModel.lat.toString()
            carOnMap?.lon = carsModel.lon.toString()
            carOnMap?.description = carsModel.description
            carOnMap?.imei = carsModel.imei
            carOnMap?.vin = carsModel.vin
            carOnMap?.lastDate = carsModel.lastDate
            carOnMap?.speed = carsModel.speed?.toInt()
            carOnMap?.model = carsModel.model
        }
        return carOnMap
    }

    fun pushFirebaseToken(firebaseModel: FirebaseModel): Single<CustomResult>{
        return mapRepository.pushFirebaseToken(firebaseModel)
    }

    fun getFirebaseModel(imei: String, token: String): FirebaseModel {
        val firebaseModel = FirebaseModel()
        firebaseModel.imei = imei
        firebaseModel.token = token
        return firebaseModel
    }

    private fun setCarsModel(carOnMap: CarOnMap): CarsModel {
        return CarsModel(
            carOnMap?.carId?.toInt(),
            carOnMap?.name,
            carOnMap?.lat?.toFloat(),
            carOnMap?.lon?.toFloat(),
            carOnMap?.description,
            carOnMap?.imei,
            carOnMap?.vin,
            carOnMap?.lastDate,
            carOnMap?.speed?.toFloat(),
            carOnMap?.model
        )
    }

    private fun addCar(carsModel: CarsModel){
        localDbRepository.insertCars(carsModel)
    }

    fun getCars(): Flowable<MainResponceArray<CarOnMap>>? {
        return mapRepository
                .getCars()
                .subscribeOn(Schedulers.io())
    }

    fun getCarInfo(carIdString: String): Flowable<MainResponceObject<CarInfo>> {
        return mapRepository
                .getCarInfo(carIdString)
                .subscribeOn(Schedulers.io())
    }

    fun getTrackInfo(monitoringCarInfoRequest: MonitoringCarInfoRequest): Flowable<TrackInfoRequest>{
            return mapRepository
                .getTrackInfo(monitoringCarInfoRequest)
                .subscribeOn(Schedulers.io())
    }

    fun getMonitoringInfo(monitoringCarInfoRequest: MonitoringCarInfoRequest): Flowable<MonitoringRequest>{
        return mapRepository
            .getMonitoringInfo(monitoringCarInfoRequest)
            .subscribeOn(Schedulers.io())
    }

    private fun changeDate(dat: String): String{
        val date = dat.replace(' ', 'T')
        val date1 = date.substringBefore('T')
        val hour = date.substringAfter('T')
        val day = date1.substringBefore('.')
        val prom = date1.substringAfter('.')
        val year = prom.substringAfter('.')
        val month = prom.substringBefore('.')
        return "${year}-${month}-${day}T${hour}"
    }

    fun populateMonitoringInfo(carId: String, dateBegin: String, dateEnd: String, imei: String, mileageStart: Double, pageNum: Int, count: Int): MonitoringCarInfoRequest {
        val monitoringCarInfoRequest = MonitoringCarInfoRequest()
        monitoringCarInfoRequest.apply {
            this.carId = carId.toInt()
            this.datetimeBegin = changeDate(dateBegin)
            this.datetimeEnd = changeDate(dateEnd)
            this.mileageStart = mileageStart
            this.pageNum = pageNum
            this.count = count
        }
        return monitoringCarInfoRequest
    }
}
