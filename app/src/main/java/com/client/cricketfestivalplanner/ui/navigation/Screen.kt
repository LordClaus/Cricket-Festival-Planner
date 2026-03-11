package com.client.cricketfestivalplanner.ui.navigation

sealed class Screen(val route: String) {
    object Preloader : Screen("preloader")
    object OnboardingScreen1 : Screen("onboarding_1")
    object OnboardingScreen2 : Screen("onboarding_2")
    object OnboardingScreen3 : Screen("onboarding_3")
    object Home : Screen("home")
    object TournamentList : Screen("tournament_list")
    object TournamentDetail : Screen("tournament_detail/{tournamentId}") {
        fun createRoute(tournamentId: Int) = "tournament_detail/$tournamentId"
    }
    object CreateTournament : Screen("create_tournament")
    object EditTournament : Screen("edit_tournament/{tournamentId}") {
        fun createRoute(tournamentId: Int) = "edit_tournament/$tournamentId"
    }
    object MatchSchedule : Screen("match_schedule/{tournamentId}") {
        fun createRoute(tournamentId: Int) = "match_schedule/$tournamentId"
    }
    object MatchResult : Screen("match_result/{matchId}") {
        fun createRoute(matchId: Int) = "match_result/$matchId"
    }
    object TournamentTable : Screen("tournament_table/{tournamentId}") {
        fun createRoute(tournamentId: Int) = "tournament_table/$tournamentId"
    }
    object Analytics : Screen("analytics")
    object TournamentHistory : Screen("tournament_history")
    object Settings : Screen("settings")
}