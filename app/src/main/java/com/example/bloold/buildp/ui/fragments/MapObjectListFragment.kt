package com.example.bloold.buildp.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bloold.buildp.MyApplication
import com.example.bloold.buildp.R
import com.example.bloold.buildp.api.ServiceGenerator
import com.example.bloold.buildp.api.data.BaseResponse
import com.example.bloold.buildp.api.data.BaseResponseWithDataObject
import com.example.bloold.buildp.api.data.CatalogObject
import com.example.bloold.buildp.common.IntentHelper
import com.example.bloold.buildp.common.RxHelper
import com.example.bloold.buildp.common.Settings
import com.example.bloold.buildp.components.EventFragment
import com.example.bloold.buildp.databinding.FragmentMapObjectsBinding
import com.example.bloold.buildp.model.MyItem
import com.example.bloold.buildp.services.NetworkIntentService
import com.example.bloold.buildp.ui.*
import com.example.bloold.buildp.utils.PermissionUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import io.reactivex.observers.DisposableSingleObserver
import java.io.UnsupportedEncodingException
import java.net.ConnectException
import java.net.URLEncoder
import java.net.UnknownHostException


class MapObjectListFragment : EventFragment(), GoogleMap.OnMarkerClickListener {
    /*   override fun onClusterClick(cluster: Cluster<MyItem>?): Boolean {
           cluster?.let {
               //Увеличиваем, чтобы помещались все кластеры
               val builder = LatLngBounds.Builder()
               for (marker in cluster.items)
                   builder.include(marker.position)

               val bounds = builder.build()

               val padding = 100 // offset from edges of the map in pixels
               //CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(14.0f).build();
               val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
               googleMap?.animateCamera(cu)
           }
           return true
       }*/

    private lateinit var mBinding: FragmentMapObjectsBinding
    private lateinit var mClusterManager: ClusterManager<MyItem>
    var mEventMapItem: HashMap<String, MyItem> = HashMap()
    private var googleMap: GoogleMap?=null
    private var currentMarker: Marker? = null
    private var categoryId: Int?=null
    private var searchType: String?=null
    private var searchQuery: String?=null

    // Координаты куда надо переметстить карту, если кликнули по объекту
    private var latMoveTo:Double?=null
    private var lngMoveTo:Double?=null

    companion object {
        private val REQUEST_RESOLVE_SERVICES_ERROR = 125
        private val REQUEST_USER_LOCATION = 128
        fun newInstance(obj:CatalogObject?=null): MapObjectListFragment
                = MapObjectListFragment().apply { arguments=Bundle() }
                .apply { obj?.getLocation().let {
                    arguments.putDouble(IntentHelper.EXTRA_LATITUDE, it?.latitude?:-1.0)
                    arguments.putDouble(IntentHelper.EXTRA_LONGITUDE, it?.longitude?:-1.0)
                }}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latMoveTo=arguments.getDouble(IntentHelper.EXTRA_LATITUDE)
            lngMoveTo=arguments.getDouble(IntentHelper.EXTRA_LONGITUDE)
            if(latMoveTo==-1.0) latMoveTo=null
            if(lngMoveTo==-1.0) lngMoveTo=null
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map_objects, container, false)
        mBinding.listener=this
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener
        {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    currentMarker =null
                    categoryId=if(it.tag!=null) it.tag as Int else null
                    loadObjectsOnMap()
                }
            }

        })
        loadTabs()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode==ChooseEditFieldActivity.REQUEST_CODE_EDIT_OBJECT||
                    requestCode==EditStateActivity.REQUEST_CODE_EDIT_STATE_OBJECT)
            {
                mBinding.mapInfo.visibility=View.GONE
                loadObjectsOnMap()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val resCode= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)
        when(resCode)
        {
            ConnectionResult.SUCCESS -> setupMap()
            else ->
            {
                if(GoogleApiAvailability.getInstance().isUserResolvableError(resCode))
                    GoogleApiAvailability.getInstance().getErrorDialog(activity, resCode, REQUEST_RESOLVE_SERVICES_ERROR).show()
                else Toast.makeText(activity, R.string.google_services_error, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun setupMap()
    {
        if(googleMap!=null) return
        (childFragmentManager.findFragmentById(R.id.fMap) as SupportMapFragment).getMapAsync({
            googleMap=it
            googleMap?.let {
                it.uiSettings.isMyLocationButtonEnabled=true
                mClusterManager = ClusterManager(activity, it)
                //mClusterManager.setOnClusterItemClickListener(this)
                //it.setOnInfoWindowClickListener(mClusterManager)
                //mClusterManager.setOnClusterClickListener(this)
                //mClusterManager.setOnClusterInfoWindowClickListener(this)
                mClusterManager.renderer = OwnIconRendered(activity, it, mClusterManager)
                it.setOnMarkerClickListener(this)
                it.setOnCameraIdleListener {
                    mClusterManager.onCameraIdle()
                    currentMarker = null

                    mBinding.mapInfo.visibility=View.GONE
                    loadObjectsOnMap()
                }
                if(latMoveTo!=null&&lngMoveTo!=null)
                {
                    //Перемещаем на место объекта
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latMoveTo?:0.0, lngMoveTo?:0.0), 16f))
                }
                else {
                    //Перемещаем на местоположения пользователя
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_USER_LOCATION)
                    } else moveToUserLocation()
                }
            }
        })
    }
    private fun moveToUserLocation()
    {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                val locationManager = (activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
                val userLocation=locationManager.getLastKnownLocation(locationManager.getBestProvider(Criteria(), true))
                if(userLocation!=null)
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(userLocation.latitude, userLocation.longitude), 16f))
            }
            catch(ex:Exception)
            {
                ex.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_USER_LOCATION) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                moveToUserLocation()
            }
        }
    }

    private fun loadTabs()
    {
       getCompositeDisposable().add(ServiceGenerator.serverApi.getMapStructure()
                .compose(RxHelper.applySchedulers())
                .subscribeWith(object : DisposableSingleObserver<BaseResponse<CatalogObject>>() {
                    override fun onSuccess(result: BaseResponse<CatalogObject>) {
                        result.data?.forEach { mBinding.tabs.addTab(mBinding.tabs.newTab().setText(it.name).setTag(it.id)) }
                        mBinding.tabs.visibility=if(mBinding.tabs.tabCount==0) View.GONE else View.VISIBLE
                        if(mBinding.tabs.visibility==View.VISIBLE)
                            mBinding.tabs.addTab(mBinding.tabs.newTab().setText(getString(R.string.all)),0)
                    }
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        if (e is UnknownHostException || e is ConnectException)
                            Toast.makeText(activity, R.string.error_check_internet, Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(activity, R.string.server_error, Toast.LENGTH_SHORT).show()
                    }
                }))
    }

    private fun loadObjectsOnMap()
    {
        googleMap?.let {
            val bounds = it.projection.visibleRegion.latLngBounds
            var lat1 = bounds.southwest.latitude
            var lat2 = bounds.northeast.latitude

            var lng1 = bounds.southwest.longitude
            var lng2 = bounds.northeast.longitude
            if (lat1 > lat2) {
                val tmp = lat1
                lat1 = lat2
                lat2 = tmp
            }

            if (lng1 > lng2) {
                val tmp = lng1
                lng1 = lng2
                lng2 = tmp
            }

            val filters = HashMap<String,String>()
            filters.put("filter[INCLUDE_SUBSECTIONS]","Y")
            filters.put("filter[<=LAT_FROM]",lat2.toString())
            filters.put("filter[>=LAT_TO]",lat1.toString())
            filters.put("filter[<=LNG_FROM]",lng2.toString())
            filters.put("filter[>=LNG_TO]",lng1.toString())
            if(!searchQuery.isNullOrEmpty()&&!searchType.isNullOrEmpty())
            {
                var search = searchType
                try {
                    search = URLEncoder.encode(searchQuery, "utf-8")
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }

                search?.let {
                    when(searchType) {
                        "object" -> filters.put("filter[?NAME]",it)
                        "egrn" -> filters.put("filter[?EGRKN_NUMBER]",it)
                        "address" -> filters.put("filter[?ADDRESS]",it)
                        else -> { }
                    }
                }
            }
            categoryId?.let {
                filters.put("filter[SECTION_ID]", categoryId.toString())
            }
            getCompositeDisposable().add(ServiceGenerator.serverApi.searchMapObjects(filters, 3500, 1)
                    .compose(RxHelper.applySchedulers())
                    .subscribeWith(object : DisposableSingleObserver<BaseResponseWithDataObject<MyItem>>() {
                        override fun onSuccess(result: BaseResponseWithDataObject<MyItem>) {
                            mClusterManager.clearItems()
                            mClusterManager.addItems(result.data?.items?.toMutableList())
                            mClusterManager.cluster()
                        }
                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            if (e is UnknownHostException || e is ConnectException)
                                Toast.makeText(activity, R.string.error_check_internet, Toast.LENGTH_SHORT).show()
                            else
                                Toast.makeText(activity, R.string.server_error, Toast.LENGTH_SHORT).show()
                        }
                    }))
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.let {
            val address = it.title
            var name = it.snippet
            if (address == null && name == null) {
                return true
            }
            if(currentMarker?.id!=it.id) {
                currentMarker?.let {
                    try {
                        it.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker", 67, 96)))
                    }
                    catch (ex: Exception)
                    {
                        ex.printStackTrace()
                    }
                }
            }

            mBinding.markerAddress.text=address
            mBinding.markerName.text=name
            mBinding.mapInfo.visibility=View.VISIBLE

            val item = mEventMapItem[it.id]
            it.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("red_marker",67,96)))
            currentMarker = it
            //val ObjId = item?.objId

            val mrLatitude = it.position.latitude.toString()
            val mrLongitude = it.position.longitude.toString()
            var geoLatitude = 0.0
            var geoLongitude = 0.0

            var dist=0f
            val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var location: Location?
            try {
                location = locationManager.getLastKnownLocation(locationManager.getBestProvider(Criteria(), true))
                if(location!=null) {
                    geoLatitude = location.latitude
                    geoLongitude = location.longitude
                    dist = calculationDistance(geoLatitude,geoLongitude,it.position.latitude,it.position.longitude);
                }
            }
            catch (ex:Exception)
            {
                ex.printStackTrace()
            }

            dist = dist.toInt().toFloat()
            mBinding.tvDist.text=dist.toString()+"м"

            if(Settings.userToken.isNullOrEmpty())
            {
                mBinding.ivFavourite.visibility = View.GONE

            }
            else
            {
                mBinding.ivFavourite.visibility = View.VISIBLE
                updateFavouriteBtn(item?.isFavourite!=null && item.isFavourite==true)
                mBinding.ivFavourite.setOnClickListener({
                    item?.isFavourite=(mBinding.ivFavourite.tag as Int)==0
                    updateFavouriteBtn((mBinding.ivFavourite.tag as Int)==0)
                    mEventMapItem[marker.id]?.let {
                        NetworkIntentService.toogleFavourite(activity, it.id)
                    }
                })
            }

/*String mrPosition = String.valueOf(mrLatitude+","+mrLongitude);
            intentRoute.putExtra("mrPosition", mrPosition);
            intentSingleObject.putExtra("ObjId", ObjId);
            String geoPosition = String.valueOf(geoLatitude+","+geoLongitude);
            intentRoute.putExtra("geoPosition", geoPosition);
            intentRoute.putExtra("mrTitle",marker.getSnippet());*/

        }
        return true
    }

    fun onInfoBlockClick(v:View)
    {
        currentMarker?.let {
            val item = mEventMapItem[it.id]
            if(item!=null)
                startActivity(Intent(activity, CatalogObjectDetailsActivity::class.java)
                        .putExtra(IntentHelper.EXTRA_OBJECT_ID, item.id))
        }
    }

    private fun updateFavouriteBtn(isFavourite:Boolean)
    {
        if(isFavourite)
        {
            mBinding.ivFavourite.setImageResource(R.drawable.ic_favourite_on)
            mBinding.ivFavourite.tag=1

        }
        else
        {
            mBinding.ivFavourite.setImageResource(R.drawable.ic_favourite_off)
            mBinding.ivFavourite.tag=0
        }
    }


    private fun calculationDistance(startPointLat: Double,startPointLon: Double,endPointLat: Double,endPointLon: Double):Float =
            calculationDistanceByCoord(startPointLat, startPointLon, endPointLat, endPointLon)

    private fun calculationDistanceByCoord(startPointLat: Double,startPointLon: Double,endPointLat: Double,endPointLon: Double): Float
    {
        var results=FloatArray(1)
        Location.distanceBetween(startPointLat, startPointLon, endPointLat, endPointLon, results)
        return results[0]
    }

    fun onEditClick(v:View)
    {
        mEventMapItem[currentMarker?.id]?.let {
            startActivityForResult(Intent(activity, ChooseEditFieldActivity::class.java)
                    .putExtra(IntentHelper.EXTRA_OBJECT_ID, it.id), ChooseEditFieldActivity.REQUEST_CODE_EDIT_OBJECT)
        }
    }
    fun onAlertClick(v:View)
    {
        mEventMapItem[currentMarker?.id]?.let {
            startActivityForResult(Intent(activity, EditStateActivity::class.java)
                    .putExtra(IntentHelper.EXTRA_OBJECT_ID, it.id), EditStateActivity.REQUEST_CODE_EDIT_STATE_OBJECT)
        }
    }
    fun onPhotoClick(v:View)
    {
        mEventMapItem[currentMarker?.id]?.let { startActivity(Intent(activity, EditPhotoVideoAudioActivity::class.java)
                .putExtra(IntentHelper.EXTRA_OBJECT_ID, it.id)) }
    }
    fun onRouteClick(v:View)
    {
        mEventMapItem[currentMarker?.id]?.let { RouteActivity.launch(activity, it.id) }
    }

    //---- Ловим события от сервиса ----
    override val actions: Array<String>
        get() = arrayOf(IntentHelper.ACTION_TOGGLE_FAVOURITE)

    override fun onEventReceived(action: String, errorMsg: String?, data: Intent?) {
        if(!errorMsg.isNullOrEmpty()) {
            Toast.makeText(activity, errorMsg, Toast.LENGTH_LONG).show()
            //Ошибка при добавлении в избранное, поэтому инвертируем
            data?.let {
                val item = mEventMapItem[it.getIntExtra(IntentHelper.EXTRA_VALUE,-100).toString()]
                if(currentMarker !=null&&item?.id.toString()==currentMarker?.id) onMarkerClick(currentMarker)
            }
        }
    }
     //------
    inner class OwnIconRendered(context: Context, map: GoogleMap,
                                   clusterManager: ClusterManager<MyItem>) : DefaultClusterRenderer<MyItem>(context, map, clusterManager) {


        override fun onClusterItemRendered(item: MyItem?, marker: Marker?) {
            super.onClusterItemRendered(item, marker)
            marker?.let {
                mEventMapItem.put(it.id!!, item!!)

            }
        }

        override fun onBeforeClusterItemRendered(item: MyItem?, markerOptions: MarkerOptions?) {
            markerOptions!!.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker", 67, 96)))
            markerOptions.snippet(item!!.snippet)
            markerOptions.title(item.title)
            super.onBeforeClusterItemRendered(item, markerOptions)

        }

        override fun getColor(clusterSize: Int): Int = Color.parseColor("#65bacc")
    }
    fun resizeMapIcons(iconName: String, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(MyApplication.instance.resources,
                MyApplication.instance.resources.getIdentifier(iconName, "drawable", MyApplication.instance.packageName))
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }
}
