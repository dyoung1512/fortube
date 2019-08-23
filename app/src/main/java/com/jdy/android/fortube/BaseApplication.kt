package com.jdy.android.fortube

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
//            modules(module {
//                single {}
//            })
        }
    }
}