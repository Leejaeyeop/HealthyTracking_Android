package com.ljy.domain.repository

import com.ljy.domain.model.Addr
import com.ljy.domain.model.Marker
import io.reactivex.rxjava3.core.Single

interface AddrRepository {
    fun getAddr(
        addr1:String,
        addr2:String
    ): Single<List<String>> //marker list 형태로 반환
}