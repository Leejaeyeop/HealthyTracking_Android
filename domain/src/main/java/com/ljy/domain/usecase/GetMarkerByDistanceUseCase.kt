package com.ljy.domain.usecase

import com.ljy.domain.model.Marker
import com.ljy.domain.repository.MarkerRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetMarkerByDistanceUseCase @Inject constructor(private val repository: MarkerRepository) {
    fun execute(east_boundary:Double,west_boundary:Double,southern_boundary:Double,northern_boundary:Double):
            Single<List<Marker>> = repository.getMarkerByDistance(east_boundary,west_boundary,southern_boundary,northern_boundary)
}