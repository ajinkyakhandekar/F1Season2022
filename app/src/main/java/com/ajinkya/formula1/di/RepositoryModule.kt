package com.ajinkya.formula1.di

import com.ajinkya.formula1.data.local.repository.LocalRepository
import com.ajinkya.formula1.data.local.repository.LocalRepositoryImpl
import com.ajinkya.formula1.data.remote.repository.RemoteRepository
import com.ajinkya.formula1.data.remote.repository.RemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/*
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun getLocalDataSource(localRepositoryImpl: LocalRepositoryImpl): LocalRepository

    @Binds
    abstract fun getDataSource(dataRepository: RemoteRepositoryImpl): RemoteRepository

}*/
