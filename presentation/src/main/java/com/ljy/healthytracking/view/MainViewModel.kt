package com.ljy.healthytracking.view

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask.execute
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ljy.domain.model.Marker
import com.ljy.domain.usecase.*
import com.ljy.healthytracking.base.BaseViewModel
import com.ljy.healthytracking.di.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.round


@HiltViewModel
class MainViewModel @Inject constructor(private val getMarkerByNameUseCase: GetMarkerByNameUseCase,
                                        private val getSerchByRegionMarkerUseCase: GetMarkerByRegionUseCase,
                                        private val getMarkerByNotedUseCase: GetMarkerByNotedUseCase,
                                        private val getAddrUseCase: GetAddrUseCase,
                                        private val getMarkerByDistanceUseCase:GetMarkerByDistanceUseCase,
                                        aplication: Application
                                        ) :
    BaseViewModel(aplication){

    companion object{
        val array :Array<String> = arrayOf("서울특별시", "경상북도", "경상남도", "강원도", "전라북도", "경기도",
            "인천광역시", "전라남도", "충청남도", "제주특별자치도", "세종특별자치시",
            "충청북도", "광주광역시", "대구광역시", "대전광역시", "부산광역시", "울산광역시")
        var addr1Entry = array.toList() as ArrayList<String>
        private const val LATITUDE_SECOND_PER_100M = ((100/30.8)/3600)
        private const val LONGITUDE_SECOND_PER_100M = ((100/25.3)/3600)
        private const val LATITUDE_SECOND_PER_1KM = 10*LATITUDE_SECOND_PER_100M
        private const val LONGITUDE_SECOND_PER_1KM = 10*LONGITUDE_SECOND_PER_100M
        private const val LATITUDE_SECOND_PER_5KM = 50*LATITUDE_SECOND_PER_100M
        private const val LONGITUDE_SECOND_PER_5KM = 50*LONGITUDE_SECOND_PER_100M
        private const val LATITUDE_SECOND_PER_10KM = 100*LATITUDE_SECOND_PER_100M
        private const val LONGITUDE_SECOND_PER_10KM = 100*LONGITUDE_SECOND_PER_100M
    }
    private val _marker = MutableLiveData<MutableList<Marker>>()
    private val _addr2Entry= MutableLiveData<MutableList<String>>()
    private val _addr3Entry= MutableLiveData<MutableList<String>>()
    private val _addrChange=MutableLiveData<Int>()
    private val _visiblityOfRegion = MutableLiveData<Boolean>()  //지역 검색 visiblity
    private val _visiblityOfName = MutableLiveData<Boolean>() //이름 검색 visiblity

    val marker:LiveData<MutableList<Marker>> get() = _marker
    val addr2Entry: LiveData<MutableList<String>?> get() = _addr2Entry
    val addr3Entry: LiveData<MutableList<String>?> get() = _addr3Entry
    val addrChange: LiveData<Int> get() = _addrChange
    val visiblityOfRegion: LiveData<Boolean> get() = _visiblityOfRegion
    val visiblityOfName : LiveData<Boolean> get() = _visiblityOfName
    val mountainName = MutableLiveData<String>()

    private lateinit var curAddr1:String
    private lateinit var curAddr2:String
    private lateinit var curAddr3:String
    private var west_boundary = 0.0
    private var east_boundary=0.0
    private var northern_boundary = 0.0
    private var southern_boundary = 0.0
    private var curLat:Double = 0.0
    private var curLng:Double =0.0
    private lateinit var locatioNManager: LocationManager
    private var context: Context = getApplication<Application>().applicationContext

    fun getMarkerByName(){ //view 함수
        compositeDisposable.add(
            getMarkerByNameUseCase.execute(mountainName.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { markers ->
                        if (markers.isEmpty()) {
                            println("비었음")
                        } else {
                            _marker.value = markers as ArrayList<Marker>
                        }
                    }, {
                        println("통신실패")
                    }
                )
        )
    }

    fun getMarkerByRegion()
    {
        compositeDisposable.add(
            getSerchByRegionMarkerUseCase.execute(curAddr1,curAddr2,curAddr3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { markers ->
                        if (markers.isEmpty()) {
                            println("비었음")
                        } else {
                            _marker.value = markers as ArrayList<Marker>
                        }
                    }, {
                        println("통신실패")
                    }
                )
        )
    }

    fun getMarkerByNoted()
    {
        compositeDisposable.add(
            getMarkerByNotedUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { markers ->
                        if (markers.isEmpty()) {
                            println("비었음")
                        } else {
                            _marker.value = markers as ArrayList<Marker>
                        }
                    }, {
                        println("통신실패")
                    }
                )
        )
    }

    fun getMarkerByCurLoc_1km()
    {
        getLocation()
        calcBoundary(LATITUDE_SECOND_PER_1KM, LONGITUDE_SECOND_PER_1KM)
        getMarkerByDistance(1000)
    }

    fun getMarkerByCurLoc_5km()
    {
        getLocation()
        calcBoundary(LATITUDE_SECOND_PER_5KM, LONGITUDE_SECOND_PER_5KM)
        getMarkerByDistance(5000)
    }

    fun getMarkerByCurLoc_10km()
    {
        getLocation()
        calcBoundary(LATITUDE_SECOND_PER_10KM, LONGITUDE_SECOND_PER_10KM)
        getMarkerByDistance(10000)
    }

    fun getMarkerByDistance(boundary:Int)
    {
        var locationA = Location("curLoc")
        var locationB = Location("otherLoc")


        locationA.latitude = curLat
        locationA.longitude = curLng
        //println("경계"+boundary)
        compositeDisposable.add(
            getMarkerByDistanceUseCase.execute(east_boundary,west_boundary,southern_boundary,northern_boundary)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { markers ->
                        if (markers.isEmpty()) {
                            println("비었음")
                        } else {
                            ////내 위치와 비교
                            var filtered_markers = ArrayList<Marker>() //필터링된 mark
                            for(marker in markers)
                            {
                                locationB.latitude = marker.y.toDouble()
                                locationB.longitude = marker.x.toDouble()
                                val distance = locationA.distanceTo(locationB)
                                if(distance <= boundary)
                                {
                                    filtered_markers.add(marker)
                                }
                            }
                           _marker.value = filtered_markers
                        }
                    }, {
                        println("통신실패")
                    }
                )
        )
    }
    //spinner event listener
    fun onSpinnerItemSelectedAddr1 (parent: AdapterView<*>, view: View, position: Int, id: Long)
    {
        curAddr1 = addr1Entry[position]
        _addrChange.value =1
        getAddr(curAddr1,"",2) //주소2 갱신

    }

    fun onSpinnerItemSelectedAddr2 (parent: AdapterView<*>, view: View, position: Int, id: Long)
    {
        _addrChange.value =2
        curAddr2 = addr2Entry.value!![position]
        if(position>0) getAddr(curAddr1,curAddr2,3) //주소3 갱신
        else {
            var list = ArrayList<String>()
            list.add("전체")
            _addr3Entry.value = list
        }

    }

    fun onSpinnerItemSelectedAddr3 (parent: AdapterView<*>, view: View, position: Int, id: Long)
    {
        _addrChange.value =3
        curAddr3= addr3Entry.value!![position]
    }

    fun onVisibilityRegion(){
        _visiblityOfRegion.value = _visiblityOfRegion.value != true
        _visiblityOfName.value=false
    }

    fun onVisibilityName(){
        _visiblityOfName.value = _visiblityOfName.value != true
        _visiblityOfRegion.value=false
    }

    fun getAddr(addr1:String,addr2:String,type:Int)
    {
        compositeDisposable.add(
            getAddrUseCase.execute(addr1,addr2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { addrs ->
                        if (addrs.isEmpty()) {
                            println("비었음")
                        } else {
                            var list = addrs as ArrayList<String>
                            list.add(0,"전체") //전체 삽입

                            if(type==2) {_addr2Entry.value = list }
                            else {_addr3Entry.value = list}
                        }
                    }, {
                        println("통신실패")
                    }
                )
        )
    }

    fun getLocation() {
        locatioNManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var userLocation: Location = getLatLng()

        if (userLocation != null) {
            curLat = userLocation.latitude
            curLng = userLocation.longitude
            Log.d("CheckCurrentLocation", "현재 내 위치 값: ${curLat}, ${curLng}")
            //calcBoundary(curLat,curLng,LATITUDE_SECOND_PER_1KM, LONGITUDE_SECOND_PER_1KM)

             //Log.d("위도",(5*LATITUDE_SECOND_PER_100M).toString())
             //Log.d("경도",(5*LONGITUDE_SECOND_PER_100M).toString())

           /* val marker = MapPOIItem()
            marker.itemName = "나"
            marker.mapPoint = MapPoint.mapPointWithGeoCoord(latitude.toDouble(), longitude.toDouble())
            marker.markerType = MapPOIItem.MarkerType.BluePin
            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
           // mapView.addPOIItem(marker);

          //  mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude.toDouble(), longitude.toDouble()),true)

             var mGeoCoder = Geocoder(appli, Locale.KOREAN)
            var mResultList: List<Address>? = null
                try {
                    mResultList = mGeoCoder.getFromLocation(
                        latitude!!, longitude!!, 1
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (mResultList != null) {
                    Log.d("CheckCurrentLocation", mResultList[0].getAddressLine(0))
                }*/
        }
    }

    //서버 보내기
    private fun calcBoundary(radiusLat:Double,radiusLng:Double){

        west_boundary = round((curLng-radiusLng)*100000)/100000
        east_boundary = round((curLng+radiusLng)*100000)/100000
        northern_boundary = round((curLat+radiusLat)*100000)/100000
        southern_boundary = round((curLat-radiusLat)*100000)/100000

    }

    private fun getLatLng(): Location {

        var currentLatLng: Location? = null
        var hasFineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        var hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        var isProviderEnabled = locatioNManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED &&
            isProviderEnabled
        ) {
            val locationGpsProvider = LocationManager.GPS_PROVIDER
            val locationNetworkProvider = LocationManager.NETWORK_PROVIDER
            currentLatLng = locatioNManager?.getLastKnownLocation(locationGpsProvider)
                ?: locatioNManager?.getLastKnownLocation(locationNetworkProvider)

        } else {
           /* // if (!isProviderEnabled) //startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, MainActivity.REQUIRED_PERMISSIONS[0])) {
                ActivityCompat.requestPermissions(this,
                    MainActivity.REQUIRED_PERMISSIONS,
                    MainActivity.PERMISSIONS_REQUEST_CODE
                )
            }
            else if(!isProviderEnabled &&!tost){
              //  tost = true
               // Toast.makeText(this, "위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                // ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }*/
            currentLatLng = getLatLng()
        }
        return currentLatLng!!
    }



}
