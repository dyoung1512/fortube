package com.jdy.android.fortube.base

import android.app.Activity
import android.content.SharedPreferences
import com.jdy.android.fortube.BuildConfig
import com.jdy.android.fortube.R
import com.jdy.android.fortube.map.MapService
import com.jdy.android.fortube.map.MapViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val apiModule = module {
    single {
        OkHttpClient.Builder().apply {
            connectTimeout(5000, TimeUnit.MILLISECONDS)
            readTimeout(5000, TimeUnit.MILLISECONDS)
            addInterceptor(Interceptor {
                it.proceed(it.request().newBuilder().apply {
                    header("Authorization", "KakaoAK ${androidApplication().resources.getString(R.string.rest_key)}")
                }.build())
            })
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(androidApplication().resources.getString(R.string.map_api))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(get())
            .build()
            .create(MapService::class.java)
    }
}
val prefModule = module {
    single {
        androidApplication().getSharedPreferences(androidApplication().packageName, Activity.MODE_PRIVATE)
    }
    single {
        PrefHelper(get())
    }
}
val vmModule = module {
    viewModel { MapViewModel(get()) }
}