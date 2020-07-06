package com.ilnur.mobileowner.interfaces.views.map

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.ilnur.mobileowner.domain.models.response.CarInfo
import com.ilnur.mobileowner.domain.models.response.route.CarOnMap
import com.ilnur.mobileowner.interfaces.ErrorsInterface
import com.ilnur.mobileowner.interfaces.LoaderInterface

interface MapView : ErrorsInterface, LoaderInterface {
    fun initMap()
    fun setMapUiSettings()
    fun setMapListeners()
    fun requestPermissions()
    fun permissions()
    fun isLocationEnabled(enable: Boolean)
    fun setListenerForLocation(zoom: Float)
    fun initViews()
    fun notCars()
    fun populateMarkers(list: List<CarOnMap>)
    fun setControlMapButtonsListener()
    fun changeMapZoom(fl: Float)
    fun getMyLocation(zoom: Float)
    fun setMapType(mapType: String)
    fun carInfoLoadError()
    fun carInfoLoadSuccess(obj: CarInfo, carOnMap: CarOnMap)
    fun setActiveCarMarker(latLng: LatLng, carOnMap: CarOnMap)
    fun refreshFragment()
    fun carOnMapOpenDialog(carOnMap: CarOnMap)
    fun buildRoute(route: PolylineOptions)
    fun cameraMoveCarOnMap(latLng: LatLng)
    fun hideDailog()
    fun showUpdate(show: Boolean)
    fun getImeiPermission()
    fun showToast(message: String)
}