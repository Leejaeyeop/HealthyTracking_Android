package com.ljy.data.api

import com.ljy.data.api.ApiClient.BASE_URL
import com.ljy.data.model.AddrResponse
import com.ljy.data.model.MarkerResponse
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("marker/v1/name")
    fun getMarkerByName(
        @Query("name") name:String
    ): Single<MarkerResponse>

    @GET("marker/v1/region")
    fun getMarkerByRegion(
        @Query("addr1") addr1:String,
        @Query("addr2") addr2:String,
        @Query("addr3") addr3:String
    ): Single<MarkerResponse>

    @GET("marker/v1/noted")
    fun getMarkerByNoted(
    ): Single<MarkerResponse>

    @GET("marker/v1/distance")
    fun getMarkerByDistance(
        @Query("east_boundary") east_boundary:Double,
        @Query("west_boundary") west_boundary:Double,
        @Query("southern_boundary") southern_boundary:Double,
        @Query("northern_boundary") northern_boundary:Double,
    ): Single<MarkerResponse>

    @GET("addr/v1/addr")
    fun getAddr(
        @Query("addr1") addr1:String,
        @Query("addr2") addr2:String,
    ): Single<AddrResponse>

    companion object{
        fun create(): ApiInterface {
            val logger = HttpLoggingInterceptor().apply {
                level =
                    HttpLoggingInterceptor.Level.BASIC
            }
            val interceptor = Interceptor { chain ->
                with(chain) {
                    val newRequest = request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build()
                    proceed(newRequest)
                }
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
               // .client(client)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}