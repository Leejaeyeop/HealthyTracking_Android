package com.ljy.data.repository.marker

import com.ljy.data.mapper.mapperToMarker
import com.ljy.data.repository.marker.remote.MarkerRemoteDataSource
import com.ljy.domain.model.Marker
import com.ljy.domain.repository.MarkerRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

 //markerrepository 구현
class MarkerRepositoryImpl @Inject constructor(
    private val markerRemoteDataSource: MarkerRemoteDataSource
) :MarkerRepository{
    override fun getMarkerByName( //marker list 반환
        query: String): Single<List<Marker>> {
        return markerRemoteDataSource.getMarkersByName(query)
            .flatMap {
                Single.just(mapperToMarker(it.markers))

            }

    }

     override fun getMarkerByRegion(
         addr1: String,
         addr2: String,
         addr3: String
     ): Single<List<Marker>> {
         return markerRemoteDataSource.getMarkersByRegion(addr1, addr2, addr3)
             .flatMap {
                 Single.just(mapperToMarker(it.markers))

             }
     }

     override fun getMarkerByNoted(): Single<List<Marker>> {
         return markerRemoteDataSource.getMarkersByNoted()
             .flatMap {
                 Single.just(mapperToMarker(it.markers))

             }
     }
 }