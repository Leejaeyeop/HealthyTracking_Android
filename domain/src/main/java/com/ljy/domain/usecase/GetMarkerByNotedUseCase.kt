package com.ljy.domain.usecase

import com.ljy.domain.model.Marker
import com.ljy.domain.repository.MarkerRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetMarkerByNotedUseCase @Inject constructor(private val repository: MarkerRepository) {
    fun execute():
            Single<List<Marker>> = repository.getMarkerByNoted()
}