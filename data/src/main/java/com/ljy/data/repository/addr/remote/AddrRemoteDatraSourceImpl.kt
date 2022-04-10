package com.ljy.data.repository.addr.remote

import com.ljy.data.api.ApiInterface
import com.ljy.data.model.AddrResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AddrRemoteDatraSourceImpl @Inject constructor(private val apiInterface: ApiInterface) :AddrRemoteDataSource {
    override fun getAddrs(addr1:String,addr2:String): Single<AddrResponse> {
        return apiInterface.getAddr(addr1,addr2)
    }
}