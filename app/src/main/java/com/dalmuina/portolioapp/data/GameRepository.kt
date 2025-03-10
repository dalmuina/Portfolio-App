package com.dalmuina.portolioapp.data

import com.dalmuina.portolioapp.data.network.GameService
import com.dalmuina.portolioapp.domain.model.GameItem
import com.dalmuina.portolioapp.domain.model.toDomain
import javax.inject.Inject

class GameRepository @Inject constructor(private val api: GameService) {

    suspend fun getAllGamesFromApi(): List<GameItem>?{
        val response = api.getGames()
        return response?.map {
           it.toDomain()
        }
    }

}
