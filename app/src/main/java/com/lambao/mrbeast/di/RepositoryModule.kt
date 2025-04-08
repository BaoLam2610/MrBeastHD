package com.lambao.mrbeast.di

import com.lambao.mrbeast.data.repository.offline.OfflinePlaylistRepository
import com.lambao.mrbeast.data.repository.offline.OfflinePlaylistRepositoryImpl
import com.lambao.mrbeast.data.repository.online.OnlinePlaylistRepository
import com.lambao.mrbeast.data.repository.online.OnlinePlaylistRepositoryImpl
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
    fun provideOnlinePlaylistRepository(impl: OnlinePlaylistRepositoryImpl): OnlinePlaylistRepository =
        impl

    @Provides
    @Singleton
    fun provideOfflinePlaylistRepository(impl: OfflinePlaylistRepositoryImpl): OfflinePlaylistRepository =
        impl
}