package com.ljy.healthytracking.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.ljy.healthytracking.BR
import com.ljy.healthytracking.MapViewEvent.MarkerEventListener
import com.ljy.healthytracking.R
import com.ljy.healthytracking.base.BaseActivity
import com.ljy.healthytracking.databinding.ActivityMainBinding
import com.ljy.healthytracking.view.MainViewModel.Companion.addr1Entry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import net.daum.mf.map.api.MapCircle
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mapView:MapView //카카오 지도 뷰
    private lateinit var mapViewContainer: ViewGroup
    private lateinit var addr1AdapterView: ArrayAdapter<String>
    private lateinit var addr2AdapterView: ArrayAdapter<String>
    private lateinit var addr3AdapterView: ArrayAdapter<String>

    private lateinit var items1:ArrayList<String>
    private lateinit var items2:ArrayList<String>
    private lateinit var items3:ArrayList<String>

    private val eventListener = MarkerEventListener(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        mapView = MapView(this)
        mapViewContainer = findViewById(R.id.map_view)
        mapViewContainer.addView(mapView)
        mapView.setPOIItemEventListener(eventListener) //마커 클릭 이벤트 러스너 등록록


        /*var circle1 = MapCircle(
            MapPoint.mapPointWithGeoCoord(37.537094, 127.005470), // center
            500, // radius
            Color.argb(128, 255, 0, 0), // strokeColor
            Color.argb(128, 0, 255, 0)
        )*/

        initViewModelCallback()
        initSpinner()
    }

    private fun initViewModelCallback() {
        with(viewModel) {
            marker.observe( this@MainActivity,Observer{
                mapView.removeAllPOIItems()
                for(_marker in marker.value!!.listIterator()) {
                    val nmarker = MapPOIItem()
                    nmarker.itemName =_marker.name
                    nmarker.tag = _marker.id
                    nmarker.mapPoint = MapPoint.mapPointWithGeoCoord( _marker.y.toDouble(),_marker.x.toDouble())
                    nmarker.markerType = MapPOIItem.MarkerType.BluePin
                    nmarker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                    mapView.addPOIItem(nmarker);
                }
            }
            )

            addr2Entry.observe(this@MainActivity,Observer{
                items2.clear()
                items2.addAll(addr2Entry.value as ArrayList<String>)
                addr2AdapterView.notifyDataSetChanged()

                }
            )

            addr3Entry.observe(this@MainActivity,Observer{
                items3.clear()
                items3.addAll(addr3Entry.value as ArrayList<String>)
                addr3AdapterView.notifyDataSetChanged()

                }
            )

            addrChange.observe(this@MainActivity,Observer{

                when(addrChange.value){
                    1-> { //주소 1 변경
                        binding.addr2.setSelection(0)
                        }
                    2->  //주소 2 변경
                        binding.addr3.setSelection(0)

                }
            })
        }
    }

    private fun initSpinner(){

        items1 = addr1Entry
        items2 = ArrayList<String>()
        items3 = ArrayList<String>()

        addr1AdapterView = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items1)
        addr2AdapterView = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items2)
        addr3AdapterView = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items3)
        binding.addr1.adapter = addr1AdapterView
        binding.addr2.adapter = addr2AdapterView
        binding.addr3.adapter = addr3AdapterView

    }


}