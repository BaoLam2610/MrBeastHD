package com.lambao.mrbeast.di

import com.lambao.mrbeast.data.repository.OnlineSongsRepository
import com.lambao.mrbeast.data.repository.OnlineSongsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideOnlineSongsRepository(impl: OnlineSongsRepositoryImpl): OnlineSongsRepository = impl
}