package com.ljy.domain.usecase

import com.ljy.domain.model.Addr
import com.ljy.domain.repository.AddrRepository
import com.ljy.domain.repository.MarkerRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetAddrUseCase @Inject constructor(private val repository: AddrRepository) {
    fun execute(addr1:String,addr2:String):
            Single<List<String>> = repository.getAddr(addr1,addr2)
}