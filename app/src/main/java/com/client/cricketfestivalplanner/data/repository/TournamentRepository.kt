package com.client.cricketfestivalplanner.data.repository

import com.client.cricketfestivalplanner.data.db.TournamentDao
import com.client.cricketfestivalplanner.data.db.TournamentEntity
import kotlinx.coroutines.flow.Flow

class TournamentRepository(private val tournamentDao: TournamentDao) {

    fun getAllTournaments(): Flow<List<TournamentEntity>> = tournamentDao.getAllTournaments()

    fun getActiveTournaments(): Flow<List<TournamentEntity>> = tournamentDao.getActiveTournaments()

    fun getCompletedTournaments(): Flow<List<TournamentEntity>> = tournamentDao.getCompletedTournaments()

    suspend fun getTournamentById(id: Int): TournamentEntity? = tournamentDao.getTournamentById(id)

    suspend fun getAllTournamentsOnce(): List<TournamentEntity> = tournamentDao.getAllTournamentsOnce()

    suspend fun insertTournament(tournament: TournamentEntity): Long = tournamentDao.insertTournament(tournament)

    suspend fun updateTournament(tournament: TournamentEntity) = tournamentDao.updateTournament(tournament)

    suspend fun deleteTournament(tournament: TournamentEntity) = tournamentDao.deleteTournament(tournament)

    suspend fun deleteAllTournaments() = tournamentDao.deleteAllTournaments()

    suspend fun getTournamentCount(): Int = tournamentDao.getTournamentCount()
}