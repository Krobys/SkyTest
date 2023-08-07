package com.krobys.skytest.di

import com.krobys.skytest.repository.SkyRepository
import com.krobys.skytest.repository.SkyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSkyRepository(skyRepositoryImpl: SkyRepositoryImpl): SkyRepository
}
