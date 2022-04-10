package com.ljy.domain.usecase

import com.ljy.domain.model.Marker
import com.ljy.domain.repository.MarkerRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetMarkerByNameUseCase @Inject constructor(private val repository: MarkerRepository) {
    fun execute(query:String):
            Single<List<Marker>> = repository.getMarkerByName(query)
}