package com.lambao.mrbeast.di

import android.content.Context
import android.content.SharedPreferences
import com.lambao.base.data.pref.PreferenceRepository
import com.lambao.base.data.pref.PreferenceRepositoryImpl
import com.lambao.mrbeast.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Provides
    @Singleton
    fun providePreferenceRepository(
        pref: SharedPreferences,
        editor: SharedPreferences.Editor,
    ): PreferenceRepository = PreferenceRepositoryImpl(
        pref,
        editor
    )

    @Provides
    @Singleton
    fun providePreference(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(
            Constants.PREF_FILE_NAME,
            Context.MODE_PRIVATE
        )

    @Provides
    @Singleton
    fun providePreferenceEditor(pref: SharedPreferences): SharedPreferences.Editor =
        pref.edit()
}