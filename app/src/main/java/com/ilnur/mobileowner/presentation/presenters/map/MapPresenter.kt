package com.ilnur.mobileowner.presentation.presenters.map

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import com.ilnur.mobileowner.data.utils.ConstantUtils
import com.ilnur.mobileowner.domain.interactors.map.MapInteractor
import com.ilnur.mobileowner.domain.models.MainResponceArray
import com.ilnur.mobileowner.domain.models.request.CarsModel
import com.ilnur.mobileowner.domain.models.request.WayRequest
import com.ilnur.mobileowner.domain.models.response.MonitoringInfo
import com.ilnur.mobileowner.domain.models.response.route.CarOnMap
import com.ilnur.mobileowner.interfaces.views.map.MapView
import com.ilnur.mobileowner.presentation.presenters.extensions.returnMessage

class MapPresenter(private val interactor: MapInteractor) {

    private var mapView: MapView? = null
    private var zoom: Float = 4f
    private var mapType: String = ConstantUtils.MAP_TYPE_NORMAL
    private var carId: String? = null
    private var carImei: String? = null
    private var dateBegin: String? = null
    private var dateEnd: String? = null
    private var carLatLng: LatLng? = null
    private var carOnMap: CarOnMap? = null
    private var carName: String? = null
    private var page: Int = 0
    private var flag = true
    private var dbRequest: Disposable? = null
    private var carsRequest: Disposable? = null
    private var trackInfoRequest: Disposable? = null
    private var loadTrackRequest: Disposable? = null
    var route = PolylineOptions()

    fun setView(view: MapView) {
        mapView = view
        mapView?.initViews()
        mapView?.initMap()
    }

    fun permissionGranted() {
        mapView?.isLocationEnabled(true)
        mapView?.setListenerForLocation(zoom)
    }

    fun myLocationOn() {
        mapView?.isLocationEnabled(true)
        myLocationButtonClicked()
    }

    fun mapReady() {
        mapView?.getImeiPermission()
        mapView?.setMapUiSettings()
        mapView?.setMapListeners()
        mapView?.setControlMapButtonsListener()
    }

    /**
     *  region requests the map's
     * */
    // method get cars data from local db
    fun getCarsFromDB() {
        dbRequest = interactor
            .getCarsFromDB()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                checkGetCarsDB(it)
            }, {
                errorGetCarsDB(it)
            })
    }

    // method get cars data from server
    fun getCarsWeb() {
        mapView?.showUpdate(true)
        carsRequest = interactor
            .getCars()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ result ->
                successGetCars(result)
            }, { error ->
                responseError(error)
            })
    }

    // method get base track info (number of page)
    fun getTrackInfo(dateBegin: String, dateEnd: String) {
        this.dateBegin = dateBegin
        this.dateEnd = dateEnd

        var monitoringInfo = interactor.populateMonitoringInfo(
            carId.toString(),
            dateBegin,
            dateEnd,
            carImei.toString(),
            0.0,
            0,
            0
        )

        trackInfoRequest = interactor
            .getMonitoringInfo(monitoringInfo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result -> result.objects?.let { responseMonitoringInfoSuccess(it) } },
                { error -> responseError(error) })
    }

    // method get page of track
    private fun pagingTrack(pageNum: Int, mileageStart: Double) {
        var monitoringInfo = interactor.populateMonitoringInfo(
            carId.toString(),
            dateBegin.toString(),
            dateEnd.toString(),
            carImei.toString(),
            mileageStart,
            pageNum,
            1
        )

        loadTrackRequest =
            interactor.getTrackInfo(monitoringInfo).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (pageNum <= page) {
                        addPointsAtWay(it.objects)
                        return@subscribe pagingTrack(pageNum + 1, mileageStart)
                    } else {
                        responseTrackSuccess()
                    }
                }, { error ->
                    reponseTrackError(error)
                })
    }

    /**
     *  region handle request methods
    * */
    private fun checkGetCarsDB(result: List<CarsModel>) {
        if (result.isEmpty())
            getCarsWeb()
        else
            successGetCarsDB(result)
    }

    private fun errorGetCarsDB(error: Throwable?) {
        error?.returnMessage()?.let { mapView?.showToast(it) }
    }

    private fun successGetCarsDB(result: List<CarsModel>) {
        if (flag)
            getCarsWeb()
        changeCarsList(result)
        flag = false
    }

    private fun successGetCars(result: MainResponceArray<CarOnMap>) {
        mapView?.showUpdate(false)
        saveDataDB(result.list)
        mapView?.populateMarkers(result.list)
    }

    private fun saveDataDB(cars: List<CarOnMap>) {
        interactor.insert(cars)
    }

    private fun changeCarsList(cars: List<CarsModel>) {
        var carOnMap: CarOnMap
        val carList: ArrayList<CarOnMap> = ArrayList()
        for (item in cars) {
            carOnMap = interactor.setCarOnMap(item)
            carList.add(carOnMap)
        }
        mapView?.populateMarkers(carList)
    }

    private fun responseMonitoringInfoSuccess(result: MonitoringInfo) {
        page = result.page.toInt()
        pagingTrack(0, result.mileageStart.toDouble())
    }

    private fun responseTrackSuccess() {
        mapView?.buildRoute(route)
        mapView?.hideDailog()
    }

    fun cleanRoute() {
        if (route != null)
            route = PolylineOptions()
    }

    private fun responseError(error: Throwable) {
        mapView?.apply {
            showProgressDialog(false)
            //останавливаем прогресс-бар
            showUpdate(false)
            //выводим тост-сообщение ошибки
            showToast(error.returnMessage())
        }
    }

    private fun reponseTrackError(error: Throwable?) {
    }

    private fun responceSuccess(car: MainResponceArray<CarOnMap>) {
        mapView?.showProgressDialog(false)
        if (car.resultObject?.status?.toInt() == 403) {
            mapView?.tokenError()
            return
        }

        if (car.list.isEmpty()) {
            mapView?.notCars()
            return
        }

        mapView?.populateMarkers(car.list)
    }

    private fun addPointsAtWay(result: WayRequest) {
        route.width(5F)
            .color(Color.RED)
        for (data in result.way) {
            route.add(LatLng(data.lat.toDouble(), data.lng.toDouble()))
        }
    }


    fun increaseZoomClicked() {
        zoom += 1
        mapView?.changeMapZoom(zoom)
    }

    fun decreaseZoomClicked() {
        zoom -= 1
        mapView?.changeMapZoom(zoom)
    }

    private fun myLocationButtonClicked() {
        mapView?.getMyLocation(zoom)
    }

    fun chooseMapTypeButtonClicked() {
        when (mapType) {
            ConstantUtils.MAP_TYPE_NORMAL -> {
                mapView?.setMapType(ConstantUtils.MAP_TYPE_SATELLITE)
                mapType = ConstantUtils.MAP_TYPE_SATELLITE
            }
            ConstantUtils.MAP_TYPE_SATELLITE -> {
                mapView?.setMapType(ConstantUtils.MAP_TYPE_TERRAIN)
                mapType = ConstantUtils.MAP_TYPE_TERRAIN
            }
            ConstantUtils.MAP_TYPE_TERRAIN -> {
                mapView?.setMapType(ConstantUtils.MAP_TYPE_HYBRID)
                mapType = ConstantUtils.MAP_TYPE_HYBRID
            }
            ConstantUtils.MAP_TYPE_HYBRID -> {
                mapView?.setMapType(ConstantUtils.MAP_TYPE_SATELLITE)
                mapType = ConstantUtils.MAP_TYPE_SATELLITE
            }
        }
    }

    fun cancelRequest() {
        if (this.dbRequest != null) {
            dbRequest!!.dispose()
            dbRequest = null
        }

        if (this.carsRequest != null) {
            carsRequest!!.dispose()
            carsRequest = null
        }

        if (this.trackInfoRequest != null) {
            trackInfoRequest!!.dispose()
            trackInfoRequest = null
        }

        if (this.loadTrackRequest != null) {
            loadTrackRequest!!.dispose()
            loadTrackRequest = null
        }
    }
}