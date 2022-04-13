package com.ljy.domain.repository

import com.ljy.domain.model.Marker
import io.reactivex.rxjava3.core.Single

interface MarkerRepository {

    fun getMarkerByName(
        query:String
    ): Single<List<Marker>> //marker list 형태로 반환

    fun getMarkerByRegion(
        addr1:String,addr2:String,addr3:String
    ): Single<List<Marker>> //marker list 형태로 반환

    fun getMarkerByNoted(): Single<List<Marker>> //marker list 형태로 반환

    fun getMarkerByDistance(east_boundary:Double,west_boundary:Double,southern_boundary:Double,northern_boundary:Double): Single<List<Marker>> //marker list 형태로 반환
}