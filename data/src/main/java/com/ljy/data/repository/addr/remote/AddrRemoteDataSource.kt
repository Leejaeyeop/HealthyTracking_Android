package com.ljy.data.repository.addr.remote

import com.ljy.data.model.AddrResponse
import io.reactivex.rxjava3.core.Single

interface AddrRemoteDataSource {
    fun getAddrs(
        addr1:String,
        addr2:String
    ): Single<AddrResponse>
}