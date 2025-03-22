package com.lambao.base.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @Provides
    @IODispatcher
    fun provideIODispatcher() = Dispatchers.IO

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher() = Dispatchers.Default
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher