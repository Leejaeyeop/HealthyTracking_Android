package com.ljy.data.di

import com.ljy.data.repository.addr.remote.AddrRemoteDataSource
import com.ljy.data.repository.addr.remote.AddrRepositoryImpl
import com.ljy.data.repository.marker.MarkerRepositoryImpl
import com.ljy.data.repository.marker.remote.MarkerRemoteDataSource
import com.ljy.domain.repository.AddrRepository
import com.ljy.domain.repository.MarkerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMarkerRepository(
        markerRemoteDataSource: MarkerRemoteDataSource
    ): MarkerRepository{
        return MarkerRepositoryImpl(markerRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideAddrRepository(
        addrRemoteDataSource: AddrRemoteDataSource
    ): AddrRepository{
        return AddrRepositoryImpl(addrRemoteDataSource)
    }
}