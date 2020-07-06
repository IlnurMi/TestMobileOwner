package com.ilnur.mobileowner.presentation.ui.map

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Location
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.content_map.*
import com.ilnur.mobileowner.data.database.AppRoomDatabase
import com.ilnur.mobileowner.data.database.LocalDbRepository
import com.ilnur.mobileowner.data.network.ApiClient
import com.ilnur.mobileowner.data.repositories.map.MapRepository
import com.ilnur.mobileowner.data.utils.NavigationUtil
import com.ilnur.mobileowner.domain.interactors.map.MapInteractor
import com.ilnur.mobileowner.domain.models.response.CarInfo
import com.ilnur.mobileowner.domain.models.response.route.CarOnMap
import com.ilnur.mobileowner.interfaces.BackPressedInterface
import com.ilnur.mobileowner.interfaces.DrawerFragmentInterface
import com.ilnur.mobileowner.interfaces.FragmentsListener
import com.ilnur.mobileowner.interfaces.TextChangeListener
import com.ilnur.mobileowner.interfaces.repositories.LocalDbRepositoryInterface
import com.ilnur.mobileowner.interfaces.views.map.MapFragmentListeners
import com.ilnur.mobileowner.interfaces.views.map.MapView
import com.ilnur.mobileowner.interfaces.views.map.MyMapCallback
import com.ilnur.mobileowner.presentation.presenters.map.MapPresenter
import com.ilnur.mobileowner.presentation.ui.login.LoginActivity
import com.ilnur.mobileowner.presentation.ui.main.MainActivity
import java.lang.ClassCastException
import com.ilnur.mobileowner.R
import com.ilnur.mobileowner.data.utils.ConstantUtils

class MyMapFragment : Fragment(), MapView, TextChangeListener, DrawerFragmentInterface,
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, MyMapCallback, GoogleMap.OnPolylineClickListener,
    MapFragmentListeners, ClusterManager.OnClusterItemClickListener<MarkerClusterItem>,
    BackPressedInterface {

    private lateinit var map: GoogleMap
    private var myView: View? = null
    private var mainActivity: MainActivity? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    private var mapView: MapFragment? = null
    private var presenter: MapPresenter? = null
    private var progressDialog: ProgressDialog? = null
    private var coorditnates: ArrayList<LatLng>? = null
    private lateinit var myMapContext: Context
    private var bundle: Bundle? = null

    private lateinit var carsList: MutableList<CarOnMap>
    private lateinit var carsAdapter: MyMapFragmentAdapter
    private lateinit var clusterManager: ClusterManager<MarkerClusterItem>
    private var items = ArrayList<MarkerClusterItem>()

    private var choseMarker: MarkerClusterItem? = null
    private var chosenCluster: Cluster<MarkerClusterItem>? = null
    private var localRepository: LocalDbRepositoryInterface? = null
    private lateinit var fragmentsListener: FragmentsListener

    companion object {
        internal const val READ_PHONY_STATE_PERMISSION_REQUEST_CODE = 1
        internal const val LOCATION_PERMISSION_REQUEST_CODE = 2
        private var INSTANCE: MyMapFragment? = null

        fun getInstance(): MyMapFragment {
            if (INSTANCE == null)
                INSTANCE = MyMapFragment()
            return INSTANCE!!
        }

        fun newInstance(): MyMapFragment {
            INSTANCE = MyMapFragment()
            return INSTANCE!!
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is MainActivity) return
        mainActivity = context
        myMapContext = context.let { context }
        try {
            fragmentsListener = activity as FragmentsListener
        } catch (e: ClassCastException) {
            //TODO show error
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (myView != null) {
            (myView?.parent as ViewGroup?)?.removeView(myView)
        }
        try {
            myView = inflater.inflate(com.ilnur.mobileowner.R.layout.fragment_map, container, false)
        } catch (e: InflateException) {
            e.printStackTrace()
        }

        localRepository = LocalDbRepository(AppRoomDatabase.getInstance(myMapContext)!!)
        presenter = MapPresenter(
            MapInteractor(
                MapRepository(
                    ApiClient().getTestRestClient(activity as MainActivity),
                    ApiClient().getFirebaseRestClient(activity as MainActivity)
                ), localDbRepository = localRepository as LocalDbRepository
            )
        )
        presenter?.setView(this)
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        carsList = ArrayList()
        carsAdapter = MyMapFragmentAdapter(carsList, myMapContext, this)
        rv_cars_on_maps.layoutManager = LinearLayoutManager(myMapContext)
        rv_cars_on_maps.adapter = carsAdapter
    }

    override fun initViews() {
        progressDialog = ProgressDialog(context as MainActivity)
        progressDialog?.setTitle(getString(com.ilnur.mobileowner.R.string.loading_data))
        progressDialog?.setMessage(getString(com.ilnur.mobileowner.R.string.watching_cars_on_map))
    }

    override fun showProgressDialog(show: Boolean) {
        if (show)
            progressDialog?.show()
        else
            progressDialog?.hide()
    }

    override fun requestPermissions() {
        if (ActivityCompat.checkSelfPermission(
                mainActivity as MainActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                mainActivity as MainActivity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        } else
            presenter?.permissionGranted()
    }

    override fun permissions() {
        if (ActivityCompat.checkSelfPermission(
                mainActivity as MainActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                mainActivity as MainActivity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        } else
            presenter?.myLocationOn()
    }

    override fun initMap() {
        mapView = activity!!.fragmentManager!!.findFragmentById(com.ilnur.mobileowner.R.id.map) as? MapFragment
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(mainActivity as Activity)
        mapView?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.map = googleMap!!
        presenter?.mapReady()

        clusterManager = ClusterManager<MarkerClusterItem>(myMapContext, map)
        map.setOnCameraIdleListener { clusterManager }
        map.setOnMapClickListener { clusterManager }
        clusterManager.renderer = CustomMapClusterRender(myMapContext, map, clusterManager)
        clusterManager.cluster()
        map.setOnCameraMoveStartedListener { clusterManager.cluster() }

        clusterManager.setOnClusterItemClickListener { p0 ->
            carOnMapOpenDialog(p0!!.getCar())
            choseMarker = p0
            false
        }

        clusterManager.setOnClusterClickListener { p0 ->
            chosenCluster = p0
            false
        }

        map.setOnMarkerClickListener(clusterManager)

        if (this.arguments != null) {
            bundle = this.arguments!!
        }

        map.setOnPolylineClickListener(this)
        presenter?.getCarsFromDB()
    }

    override fun onClusterItemClick(p0: MarkerClusterItem?): Boolean {
        return true
    }

    override fun buildRoute(route: PolylineOptions) {
        map.addPolyline(route)
        val marker = MarkerOptions().position(
            LatLng(
                route.points.get(0).latitude,
                route.points.get(0).longitude
            )
        )
            .icon(
                bitmapDescriptorFromVector(
                    activity!!.applicationContext,
                    com.ilnur.mobileowner.R.drawable.ic_route_start
                )
            )
        map.addMarker(marker)

        val lastMarker = MarkerOptions().position(
            LatLng(
                route.points.get(route.points.lastIndex).latitude,
                route.points.get(route.points.lastIndex).longitude
            )
        )
            .icon(
                bitmapDescriptorFromVector(
                    activity!!.applicationContext,
                    com.ilnur.mobileowner.R.drawable.ic_route_end
                )
            )
        map.addMarker(lastMarker)
        presenter?.cleanRoute()
    }

    override fun hideDailog() {
        progressDialog?.hide()
    }

    override fun showUpdate(show: Boolean) {
        if (show)
            fragmentsListener.setActionBarTitle("Обновление...")
        else
            fragmentsListener.setActionBarTitle("Карта")
    }

    override fun getImeiPermission() {
        if (ActivityCompat.checkSelfPermission(
                mainActivity as MainActivity,
                android.Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                mainActivity as MainActivity,
                arrayOf(android.Manifest.permission.READ_PHONE_STATE),
                READ_PHONY_STATE_PERMISSION_REQUEST_CODE
            )
            requestPermissions()
        } else {
            requestPermissions()
            return
        }
    }

    override fun textChange(newText: String) {
        if (newText != "") {
            carsAdapter.let { it.filter.filter(newText) }
            rv_cars_on_maps.visibility = View.VISIBLE
        } else {
            rv_cars_on_maps.visibility = View.GONE
        }
    }


    override fun closeList() {
        rv_cars_on_maps.visibility = View.GONE
    }

    override fun closeSearchView() {
    }

    override fun moveCamera(latLng: LatLng) {
        rv_cars_on_maps.visibility = View.GONE
        cameraMoveCarOnMap(latLng)
        //убираем фокус из SearchView
        mainActivity?.clearFocusOnSearchView()
    }


    override fun showRoute(dateBegin: String, dateEnd: String, name: String) {
        fragmentsListener.showArrow()
        fragmentsListener.showSearchView(false)
        fragmentsListener.setActionBarTitle(name)

        map.clear()
        clusterManager.clearItems()
        progressDialog?.setMessage("Построение трека")
        progressDialog?.show()
        presenter?.getTrackInfo(dateBegin, dateEnd)
    }

    override fun setMapUiSettings() {
        map.uiSettings.isZoomControlsEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false
        map.uiSettings.isMapToolbarEnabled = false
        map.setMinZoomPreference(2.0f)
        map.setMaxZoomPreference(21.0f)
    }

    override fun setMapListeners() {
        map.setOnMarkerClickListener(this)
    }

    override fun isLocationEnabled(enable: Boolean) {
        //map.isMyLocationEnabled = enable
    }

    private fun setMarkerToMap(latLng: LatLng, isOwner: Boolean, car: CarOnMap?) {
        val markerOptions = MarkerOptions().position(latLng)

        if (isOwner) {
            markerOptions.icon(
                bitmapDescriptorFromVector(
                    activity!!.applicationContext,
                    R.drawable.ic_nav_bl
                )
            )

        } else {
            markerOptions.title(car?.name)

            val markerIcon = getMarkerIcon(
                root = activity?.findViewById(R.id.fragment_main) as ViewGroup,
                text = car?.name.toString(),
                isSelected = true
            )
            markerOptions.icon(markerIcon)
        }

        if (car != null) {
            map.addMarker(markerOptions).tag = car

        } else {
            map.addMarker(markerOptions)
        }
    }

    override fun setListenerForLocation(zoom: Float) {
        fusedLocationClient!!.lastLocation!!.addOnSuccessListener { location ->
            if (bundle != null) {
                var lat = bundle?.getParcelable<CarOnMap>("carId")?.lat
                var lon = bundle?.getParcelable<CarOnMap>("carId")?.lon
                cameraMoveCarOnMap(LatLng(lat!!.toDouble(), lon!!.toDouble()))

            } else {
                if (location != null) {
                    lastLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    setMarkerToMap(currentLatLng, true, null)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoom))
                }
            }
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    override fun setActiveCarMarker(latLng: LatLng, carOnMap: CarOnMap) {
        setMarkerToMap(latLng, false, carOnMap)
    }

    override fun setControlMapButtonsListener() {
        iv_plus_zoom.setOnClickListener { presenter?.increaseZoomClicked() }

        iv_minus_zoom.setOnClickListener { presenter?.decreaseZoomClicked() }

        iv_replay.setOnClickListener { presenter?.getCarsWeb() }

        iv_my_location.setOnClickListener { requestPermissions() }

        iv_choose_map_type.setOnClickListener { presenter?.chooseMapTypeButtonClicked() }
    }

    override fun setMapType(mapType: String) {
        when (mapType) {
            ConstantUtils.MAP_TYPE_NORMAL -> {
                map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }
            ConstantUtils.MAP_TYPE_SATELLITE -> {
                map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }
            ConstantUtils.MAP_TYPE_TERRAIN -> {
                map.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
            ConstantUtils.MAP_TYPE_HYBRID -> {
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }
    }

    override fun getMyLocation(zoom: Float) {
        val latLng = LatLng(map.myLocation.latitude, map.myLocation.longitude)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    override fun changeMapZoom(zoom: Float) {
        map.animateCamera(CameraUpdateFactory.zoomTo(zoom))
    }

    override fun cameraMoveCarOnMap(latLng: LatLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19F))
        clusterManager.cluster()
        if (this.arguments != null) {
            bundle = null
        }
    }

    override fun refreshFragment() {
        map.clear()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker?.tag is CarOnMap)
            carOnMapOpenDialog(marker?.tag as CarOnMap)
        return true
    }

    override fun onPolylineClick(line: Polyline?) {
        showAlertDialog()
    }

    private fun showAlertDialog() {
        var dialog = android.app.AlertDialog.Builder(requireContext()).create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        val view: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.track_info_dialog, null)

        view.apply {
        }

        dialog.apply {
            setView(view)
            show()
        }
    }


    override fun carOnMapOpenDialog(carOnMap: CarOnMap) {
    }

    override fun carInfoLoadSuccess(obj: CarInfo, carOnMap: CarOnMap) {
    }

    override fun carInfoLoadError() {
    }

    override fun populateMarkers(list: List<CarOnMap>) {
        map.clear()
        coorditnates = ArrayList()
        clusterManager.clearItems()
        items.clear()
        for (car in list) {
            items.add(MarkerClusterItem(car))
        }


        clusterManager.addItems(items)
        clusterManager.cluster()

        carsAdapter.addItems(list)
    }

    override fun notCars() {
        fragmentsListener.showToast("Нет машин")
    }

    override fun errorServer() {
        fragmentsListener.showToast("Ошибка сервера")
    }

    override fun tokenError() {
        Toast.makeText(
            mainActivity,
            getString(R.string.toats_user_not_registered),
            Toast.LENGTH_SHORT
        ).show()
        NavigationUtil.instance.invokeActivityWithoutData(
            mainActivity as Activity,
            LoginActivity::class.java,
            true
        );
    }

    override fun errorNoInternetConnection() {
        fragmentsListener.showToast(getString(com.ilnur.mobileowner.R.string.not_conection))
    }

    override fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDetach() {
        super.onDetach()

        if (progressDialog?.isShowing!!) {
            progressDialog?.hide()
            progressDialog?.dismiss()
        }

        if (!activity!!.isDestroyed) {
            activity?.fragmentManager?.beginTransaction()?.remove(mapView)?.commit()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity = null

    }

    override fun onStop() {
        super.onStop()
        presenter?.cancelRequest()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                } else {
                    presenter?.permissionGranted()
                }
            }

            READ_PHONY_STATE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions()
                } else {
                    requestPermissions()
                }
            }
        }
    }

    override fun copyLtng(lat: String) {
        fragmentsListener.saveTextToClipBoard(lat)
    }

    override fun changeColors(marker: Marker) {
        val markerIcon = getMarkerIcon(
            root = activity?.findViewById(R.id.fragment_main) as ViewGroup,
            text = marker.title,
            isSelected = true
        )
        marker.setIcon(markerIcon)
    }

    private fun getMarkerIcon(
        root: ViewGroup,
        text: String,
        isSelected: Boolean
    ): BitmapDescriptor {
        val markerView = CustomMarkerView(root, text, isSelected)
        markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)
        markerView.isDrawingCacheEnabled = true
        markerView.invalidate()
        markerView.buildDrawingCache(false)
        return BitmapDescriptorFactory.fromBitmap(markerView.drawingCache)
    }

    override fun onBackPressed() {
        fragmentsListener.showHome()
        fragmentsListener.showSearchView(true)
        fragmentsListener.setActionBarTitle("Карта")
        presenter?.getCarsFromDB()
    }

}