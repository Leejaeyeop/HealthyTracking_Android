package com.ljy.data.di

import com.ljy.data.api.ApiInterface
import com.ljy.data.repository.addr.remote.AddrRemoteDataSource
import com.ljy.data.repository.addr.remote.AddrRemoteDatraSourceImpl
import com.ljy.data.repository.addr.remote.AddrRepositoryImpl
import com.ljy.data.repository.marker.remote.MarkerRemoteDataSource
import com.ljy.data.repository.marker.remote.MarkerRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteDataModule {

    @Provides
    @Singleton
    fun provideMarkerRemoteDataSource(apiInterface: ApiInterface): MarkerRemoteDataSource{
        return MarkerRemoteDataSourceImpl(apiInterface)
    }

    @Provides
    @Singleton
    fun provideAddrRemoteDataSource(apiInterface: ApiInterface): AddrRemoteDataSource {
        return AddrRemoteDatraSourceImpl(apiInterface)
    }
}