package com.client.cricketfestivalplanner.data.repository

import com.client.cricketfestivalplanner.data.db.MatchDao
import com.client.cricketfestivalplanner.data.db.MatchEntity
import kotlinx.coroutines.flow.Flow

class MatchRepository(private val matchDao: MatchDao) {

    fun getMatchesByTournamentId(tournamentId: Int): Flow<List<MatchEntity>> =
        matchDao.getMatchesByTournamentId(tournamentId)

    suspend fun getMatchesByTournamentIdOnce(tournamentId: Int): List<MatchEntity> =
        matchDao.getMatchesByTournamentIdOnce(tournamentId)

    suspend fun getMatchById(matchId: Int): MatchEntity? =
        matchDao.getMatchById(matchId)

    fun getPendingMatches(tournamentId: Int): Flow<List<MatchEntity>> =
        matchDao.getPendingMatches(tournamentId)

    suspend fun insertMatch(match: MatchEntity): Long =
        matchDao.insertMatch(match)

    suspend fun insertMatches(matches: List<MatchEntity>) =
        matchDao.insertMatches(matches)

    suspend fun updateMatch(match: MatchEntity) =
        matchDao.updateMatch(match)

    suspend fun deleteMatch(match: MatchEntity) =
        matchDao.deleteMatch(match)

    suspend fun deleteMatchesByTournamentId(tournamentId: Int) =
        matchDao.deleteMatchesByTournamentId(tournamentId)

    suspend fun deleteAllMatches() =
        matchDao.deleteAllMatches()
}