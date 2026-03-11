package com.client.cricketfestivalplanner.data.repository

import com.client.cricketfestivalplanner.data.db.TeamDao
import com.client.cricketfestivalplanner.data.db.TeamEntity
import kotlinx.coroutines.flow.Flow

class TeamRepository(private val teamDao: TeamDao) {

    fun getTeamsByTournamentId(tournamentId: Int): Flow<List<TeamEntity>> =
        teamDao.getTeamsByTournamentId(tournamentId)

    suspend fun getTeamsByTournamentIdOnce(tournamentId: Int): List<TeamEntity> =
        teamDao.getTeamsByTournamentIdOnce(tournamentId)

    suspend fun getTeamById(teamId: Int): TeamEntity? =
        teamDao.getTeamById(teamId)

    suspend fun insertTeam(team: TeamEntity): Long =
        teamDao.insertTeam(team)

    suspend fun insertTeams(teams: List<TeamEntity>) =
        teamDao.insertTeams(teams)

    suspend fun updateTeam(team: TeamEntity) =
        teamDao.updateTeam(team)

    suspend fun deleteTeam(team: TeamEntity) =
        teamDao.deleteTeam(team)

    suspend fun deleteTeamsByTournamentId(tournamentId: Int) =
        teamDao.deleteTeamsByTournamentId(tournamentId)
}