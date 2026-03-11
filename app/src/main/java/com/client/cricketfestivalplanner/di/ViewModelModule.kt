package com.client.cricketfestivalplanner.di

import com.client.cricketfestivalplanner.ui.analytics.AnalyticsViewModel
import com.client.cricketfestivalplanner.ui.history.TournamentHistoryViewModel
import com.client.cricketfestivalplanner.ui.home.HomeViewModel
import com.client.cricketfestivalplanner.ui.match.MatchResultViewModel
import com.client.cricketfestivalplanner.ui.onboarding.OnboardingViewModel
import com.client.cricketfestivalplanner.ui.preloader.PreloaderViewModel
import com.client.cricketfestivalplanner.ui.settings.SettingsViewModel
import com.client.cricketfestivalplanner.ui.tournament.CreateTournamentViewModel
import com.client.cricketfestivalplanner.ui.tournament.EditTournamentViewModel
import com.client.cricketfestivalplanner.ui.tournament.MatchScheduleViewModel
import com.client.cricketfestivalplanner.ui.tournament.TournamentDetailViewModel
import com.client.cricketfestivalplanner.ui.tournament.TournamentListViewModel
import com.client.cricketfestivalplanner.ui.tournament.TournamentTableViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::PreloaderViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::TournamentListViewModel)
    viewModelOf(::TournamentDetailViewModel)
    viewModelOf(::CreateTournamentViewModel)
    viewModelOf(::EditTournamentViewModel)
    viewModelOf(::MatchScheduleViewModel)
    viewModelOf(::MatchResultViewModel)
    viewModelOf(::TournamentTableViewModel)
    viewModelOf(::AnalyticsViewModel)
    viewModelOf(::TournamentHistoryViewModel)
    viewModelOf(::SettingsViewModel)
}