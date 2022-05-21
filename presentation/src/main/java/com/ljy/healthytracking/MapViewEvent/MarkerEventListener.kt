package com.ljy.healthytracking.MapViewEvent

import android.content.Context
import androidx.activity.viewModels
import com.ljy.healthytracking.view.MainViewModel
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

//viewmodel에 전달한다.
class MarkerEventListener(val context:Context , val viewModel: MainViewModel ): MapView.POIItemEventListener {

    // 마커 클릭 시
    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
        //viewmodel 에도 이벤트를 만든다.
        viewModel.onPOIItemSelected(p0 , p1)
    }

    // 말풍선 클릭 시 (Deprecated)
    // 이 함수도 작동하지만 그냥 아래 있는 함수에 작성하자
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

    }

    // 말풍선 클릭 시
    override fun onCalloutBalloonOfPOIItemTouched(
        mapView: MapView?,
        mapPOIItem: MapPOIItem?,
        buttonType: MapPOIItem.CalloutBalloonButtonType?
    ) {
       println("${mapPOIItem?.tag}")
    }

    // 마커의 속성 중 isDraggable = true 일 때 마커를 이동시켰을 경우
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
        TODO("Not yet implemented")
    }
}