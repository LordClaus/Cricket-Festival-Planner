package com.client.cricketfestivalplanner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.client.cricketfestivalplanner.ui.analytics.AnalyticsScreen
import com.client.cricketfestivalplanner.ui.history.TournamentHistoryScreen
import com.client.cricketfestivalplanner.ui.home.HomeScreen
import com.client.cricketfestivalplanner.ui.match.MatchResultScreen
import com.client.cricketfestivalplanner.ui.onboarding.OnboardingScreen1
import com.client.cricketfestivalplanner.ui.onboarding.OnboardingScreen2
import com.client.cricketfestivalplanner.ui.onboarding.OnboardingScreen3
import com.client.cricketfestivalplanner.ui.preloader.PreloaderScreen
import com.client.cricketfestivalplanner.ui.settings.SettingsScreen
import com.client.cricketfestivalplanner.ui.tournament.CreateTournamentScreen
import com.client.cricketfestivalplanner.ui.tournament.EditTournamentScreen
import com.client.cricketfestivalplanner.ui.tournament.MatchScheduleScreen
import com.client.cricketfestivalplanner.ui.tournament.TournamentDetailScreen
import com.client.cricketfestivalplanner.ui.tournament.TournamentListScreen
import com.client.cricketfestivalplanner.ui.tournament.TournamentTableScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Preloader.route,
        modifier = modifier
    ) {
        composable(Screen.Preloader.route) {
            PreloaderScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.OnboardingScreen1.route) {
                        popUpTo(Screen.Preloader.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Preloader.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.OnboardingScreen1.route) {
            OnboardingScreen1(
                onNext = { navController.navigate(Screen.OnboardingScreen2.route) },
                onSkip = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.OnboardingScreen1.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.OnboardingScreen2.route) {
            OnboardingScreen2(
                onNext = { navController.navigate(Screen.OnboardingScreen3.route) },
                onBack = { navController.popBackStack() },
                onSkip = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.OnboardingScreen1.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.OnboardingScreen3.route) {
            OnboardingScreen3(
                onFinish = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.OnboardingScreen1.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onCreateTournament = { navController.navigate(Screen.CreateTournament.route) },
                onTournamentClick = { id ->
                    navController.navigate(Screen.TournamentDetail.createRoute(id))
                }
            )
        }

        composable(Screen.TournamentList.route) {
            TournamentListScreen(
                onTournamentClick = { id ->
                    navController.navigate(Screen.TournamentDetail.createRoute(id))
                }
            )
        }

        composable(Screen.CreateTournament.route) {
            CreateTournamentScreen(
                onTournamentCreated = { id ->
                    navController.navigate(Screen.TournamentDetail.createRoute(id)) {
                        popUpTo(Screen.CreateTournament.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TournamentDetail.route,
            arguments = listOf(navArgument("tournamentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tournamentId = backStackEntry.arguments?.getInt("tournamentId") ?: return@composable
            TournamentDetailScreen(
                tournamentId = tournamentId,
                onEdit = { id -> navController.navigate(Screen.EditTournament.createRoute(id)) },
                onSchedule = { id -> navController.navigate(Screen.MatchSchedule.createRoute(id)) },
                onTable = { id -> navController.navigate(Screen.TournamentTable.createRoute(id)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditTournament.route,
            arguments = listOf(navArgument("tournamentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tournamentId = backStackEntry.arguments?.getInt("tournamentId") ?: return@composable
            EditTournamentScreen(
                tournamentId = tournamentId,
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.MatchSchedule.route,
            arguments = listOf(navArgument("tournamentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tournamentId = backStackEntry.arguments?.getInt("tournamentId") ?: return@composable
            MatchScheduleScreen(
                tournamentId = tournamentId,
                onMatchClick = { matchId ->
                    navController.navigate(Screen.MatchResult.createRoute(matchId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.MatchResult.route,
            arguments = listOf(navArgument("matchId") { type = NavType.IntType })
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getInt("matchId") ?: return@composable
            MatchResultScreen(
                matchId = matchId,
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TournamentTable.route,
            arguments = listOf(navArgument("tournamentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tournamentId = backStackEntry.arguments?.getInt("tournamentId") ?: return@composable
            TournamentTableScreen(
                tournamentId = tournamentId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Analytics.route) {
            AnalyticsScreen()
        }

        composable(Screen.TournamentHistory.route) {
            TournamentHistoryScreen(
                onTournamentClick = { id ->
                    navController.navigate(Screen.TournamentDetail.createRoute(id))
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}