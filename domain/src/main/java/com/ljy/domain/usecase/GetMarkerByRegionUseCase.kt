package com.ljy.domain.usecase

import com.ljy.domain.model.Marker
import com.ljy.domain.repository.MarkerRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetMarkerByRegionUseCase @Inject constructor(private val repository: MarkerRepository) {
    fun execute(addr1:String,addr2:String,addr3:String):
            Single<List<Marker>> = repository.getMarkerByRegion(addr1,addr2,addr3)
}