package com.affordable

import android.app.Application
import com.affordable.di.RepositoryModule
import com.affordable.di.RetrofitModule
import com.affordable.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    companion object {
        lateinit var instance: MainApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(
                listOf(
                    ViewModelModule,
                    RetrofitModule,
                    RepositoryModule
                )
            )

        }

        instance = this
    }
}