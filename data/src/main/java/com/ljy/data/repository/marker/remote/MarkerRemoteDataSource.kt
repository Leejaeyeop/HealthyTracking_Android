package com.ljy.data.repository.marker.remote

import com.ljy.data.model.MarkerEntity
import com.ljy.data.model.MarkerResponse
import com.ljy.domain.repository.MarkerRepository
import io.reactivex.Completable
import io.reactivex.rxjava3.core.Single

interface MarkerRemoteDataSource {

    fun getMarkersByName(
        query:String,
        //start: Int=1
    ): Single<MarkerResponse>

    fun getMarkersByRegion(
        addr1:String,
        addr2:String,
        addr3:String
    ): Single<MarkerResponse>

    fun getMarkersByNoted(
    ): Single<MarkerResponse>
}