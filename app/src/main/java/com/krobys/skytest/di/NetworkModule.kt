package com.krobys.skytest.di

import android.app.Application
import com.krobys.skytest.retrofit.interceptors.HeaderInterceptor
import com.krobys.skytest.retrofit.resultCallAdapter.ResultTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.krobys.skytest.BuildConfig
import com.krobys.skytest.repository.SkyNewsApiTestImpl
import com.krobys.skytest.retrofit.SkyNewsApi
import com.krobys.skytest.retrofit.resultCallAdapter.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val factory = ResultTypeAdapter.getFactory()
        val gson = GsonBuilder().registerTypeAdapterFactory(factory).create()
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideClient(
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return OkHttpClient.Builder()
            .addNetworkInterceptor(logging)
            .addNetworkInterceptor(headerInterceptor)
            .connectTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(180, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.MINUTES)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        gsonFactory: GsonConverterFactory,
        resultCallAdapterFactory: ResultCallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonFactory)
            .addCallAdapterFactory(resultCallAdapterFactory)
            .client(client)
            .build()
    }

//    @Singleton
//    @Provides
//    fun provideSkyNewsApi(retrofit: Retrofit): SkyNewsApi { //real implementation with server
//        return retrofit.create(SkyNewsApi::class.java)
//    }

    @Provides
    fun provideSkyNewsApi(application: Application, gson: Gson): SkyNewsApi { //test implementation with mock json
        return SkyNewsApiTestImpl(application, gson)
    }

}