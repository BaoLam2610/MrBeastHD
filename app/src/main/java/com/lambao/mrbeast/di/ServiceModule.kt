package com.lambao.mrbeast.di

import com.lambao.mrbeast.domain.service.media_player.BaseMediaPlayer
import com.lambao.mrbeast.domain.service.media_player.MediaPlayerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideMediaPlayer(): BaseMediaPlayer = MediaPlayerImpl()
}