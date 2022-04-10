package com.ljy.healthytracking.view

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ljy.domain.model.Marker
import com.ljy.domain.usecase.GetAddrUseCase
import com.ljy.domain.usecase.GetMarkerByNameUseCase
import com.ljy.domain.usecase.GetMarkerByNotedUseCase
import com.ljy.domain.usecase.GetMarkerByRegionUseCase
import com.ljy.healthytracking.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val getMarkerByNameUseCase: GetMarkerByNameUseCase,
                                        private val getSerchByRegionMarkerUseCase: GetMarkerByRegionUseCase,
                                        private val getMarkerByNotedUseCase: GetMarkerByNotedUseCase,
                                        private val getAddrUseCase: GetAddrUseCase) :
    BaseViewModel(){

    companion object{
        val array :Array<String> = arrayOf("서울특별시", "경상북도", "경상남도", "강원도", "전라북도", "경기도",
            "인천광역시", "전라남도", "충청남도", "제주특별자치도", "세종특별자치시",
            "충청북도", "광주광역시", "대구광역시", "대전광역시", "부산광역시", "울산광역시")
        var addr1Entry = array.toList() as ArrayList<String>
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

}
