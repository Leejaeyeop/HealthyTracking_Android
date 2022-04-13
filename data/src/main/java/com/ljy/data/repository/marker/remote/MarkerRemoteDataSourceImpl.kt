package com.ljy.data.repository.marker.remote

import android.util.Log
import com.ljy.data.api.ApiInterface
import com.ljy.data.model.MarkerResponse
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MarkerRemoteDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface): MarkerRemoteDataSource{
    override fun getMarkersByName(query: String): Single<MarkerResponse> {
        return apiInterface.getMarkerByName(query)
    }

    override fun getMarkersByRegion(addr1: String, addr2: String, addr3: String
    ): Single<MarkerResponse> {
       return apiInterface.getMarkerByRegion(addr1, addr2, addr3)
    }

    override fun getMarkersByNoted(): Single<MarkerResponse> {
        return apiInterface.getMarkerByNoted()
    }

    override fun getMarkersByDistance(
        east_boundary: Double,
        west_boundary: Double,
        southern_boundary: Double,
        northern_boundary: Double
    ): Single<MarkerResponse> {
        return apiInterface.getMarkerByDistance(east_boundary,west_boundary,southern_boundary,northern_boundary)
    }


}

