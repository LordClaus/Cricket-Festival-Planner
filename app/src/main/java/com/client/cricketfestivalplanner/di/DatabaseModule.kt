package com.client.cricketfestivalplanner.di

import androidx.room.Room
import com.client.cricketfestivalplanner.data.db.AppDatabase
import com.client.cricketfestivalplanner.data.repository.MatchRepository
import com.client.cricketfestivalplanner.data.repository.TeamRepository
import com.client.cricketfestivalplanner.data.repository.TournamentRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    single { get<AppDatabase>().tournamentDao() }
    single { get<AppDatabase>().teamDao() }
    single { get<AppDatabase>().matchDao() }

    single { TournamentRepository(get()) }
    single { TeamRepository(get()) }
    single { MatchRepository(get()) }
}