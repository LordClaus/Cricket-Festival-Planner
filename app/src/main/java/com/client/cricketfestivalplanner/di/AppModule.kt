package com.client.cricketfestivalplanner.di

import com.client.cricketfestivalplanner.data.datastore.PreferencesManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { PreferencesManager(androidContext()) }
}