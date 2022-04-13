package com.ljy.domain.di

import com.ljy.domain.repository.AddrRepository
import com.ljy.domain.repository.MarkerRepository
import com.ljy.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGetMarkerByNameUseCase(markerRepository: MarkerRepository): GetMarkerByNameUseCase{
        return GetMarkerByNameUseCase(markerRepository)
    }

    @Provides
    @Singleton
    fun provideGetMarkerByRegion(markerRepository: MarkerRepository): GetMarkerByRegionUseCase {
        return GetMarkerByRegionUseCase(markerRepository)
    }

    @Provides
    @Singleton
    fun provideGetMarkerByNoted(markerRepository: MarkerRepository): GetMarkerByNotedUseCase {
        return GetMarkerByNotedUseCase(markerRepository)
    }

    @Provides
    @Singleton
    fun provideGetAddrUseCase(addrRepository: AddrRepository): GetAddrUseCase{
        return GetAddrUseCase(addrRepository)
    }

    @Provides
    @Singleton
    fun provideGetMarkerByDistance(markerRepository: MarkerRepository): GetMarkerByDistanceUseCase{
        return GetMarkerByDistanceUseCase(markerRepository)
    }

}