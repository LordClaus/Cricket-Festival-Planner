package com.client.cricketfestivalplanner

import android.app.Application
import com.client.cricketfestivalplanner.di.appModule
import com.client.cricketfestivalplanner.di.databaseModule
import com.client.cricketfestivalplanner.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CricketFestivalApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@CricketFestivalApp)
            modules(
                appModule,
                databaseModule,
                viewModelModule
            )
        }
    }
}