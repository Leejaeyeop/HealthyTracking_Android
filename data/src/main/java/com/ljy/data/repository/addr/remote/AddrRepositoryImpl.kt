package com.ljy.data.repository.addr.remote

import android.app.DownloadManager.Query
import com.ljy.data.mapper.mapperToMarker
import com.ljy.data.mapper.mapperToString
import com.ljy.data.repository.addr.remote.AddrRemoteDataSource
import com.ljy.domain.model.Addr
import com.ljy.domain.repository.AddrRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AddrRepositoryImpl @Inject constructor( private val addrRemoteDataSource: AddrRemoteDataSource)
    : AddrRepository{
    override fun getAddr(addr1:String,addr2:String): Single<List<String>> {
        return addrRemoteDataSource.getAddrs(addr1,addr2)
            .flatMap{
                Single.just(mapperToString(it.addr))
            }
    }
}