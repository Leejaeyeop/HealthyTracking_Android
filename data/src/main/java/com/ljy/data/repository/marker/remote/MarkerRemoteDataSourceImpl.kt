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


}

