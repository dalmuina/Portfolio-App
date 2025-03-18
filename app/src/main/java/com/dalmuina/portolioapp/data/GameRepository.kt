package com.dalmuina.portolioapp.data

import com.dalmuina.portolioapp.data.network.GameService
import com.dalmuina.portolioapp.domain.model.GameDetail
import com.dalmuina.portolioapp.domain.model.GameItem
import com.dalmuina.portolioapp.domain.model.toDomain
import javax.inject.Inject

class GameRepository @Inject constructor(private val api: GameService) {

    /**
     * Retrieves a list of all games from the API.
     *
     * This function makes a network request to fetch game data from the API endpoint.
     * It then maps the received data (presumably from a network data transfer object)
     * to a list of domain-specific GameItem objects.
     *
     * This is a suspend function, meaning it should be called from within a coroutine.
     *
     * @return A List of GameItem objects if the API request is successful and returns data,
     *         otherwise returns null.
     *
     * @throws Exception if there is an error during the network request or data mapping.
     *         Specific exception types are dependent on the underlying API implementation
     *         (e.g. IOException, HttpException).
     *         The caller should handle potential exceptions appropriately.
     */
    suspend fun getAllGamesFromApi(): List<GameItem>?{
        val response = api.getGames()
        return response?.map {
           it.toDomain()
        }
    }

    suspend fun getGameById(id: Int): GameDetail? {
        val response = api.getGameById(id)
        return response?.toDomain()
    }

}
