package com.lambao.mrbeast.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lambao.mrbeast.data.remote.service.SongApiService
import com.lambao.mrbeast.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .serializeNulls()
        .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        header: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(Constants.NETWORK_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.NETWORK_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(Constants.NETWORK_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(header)
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpLogging(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideHeader(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .header(Constants.HTTP_CONTENT_TYPE_KEY, Constants.HTTP_CONTENT_TYPE_VALUE)
                    .build()
            )
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        gSon: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gSon))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideSongApiService(retrofit: Retrofit): SongApiService =
        retrofit.create(SongApiService::class.java)
}