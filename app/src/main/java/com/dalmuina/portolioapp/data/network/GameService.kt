package com.dalmuina.portolioapp.data.network

import com.dalmuina.portolioapp.data.model.GameDetailModel
import com.dalmuina.portolioapp.data.model.GameModel
import javax.inject.Inject

class GameService @Inject constructor(private val api:GameApiClient) {

    suspend fun getGames(): List<GameModel>? {
        val response = api.getGames()
        if (response.isSuccessful){
            return response.body()?.results
        }
        return null
    }

    suspend fun getGameById(id: Int): GameDetailModel? {
        val response = api.getGameById(id)
        if(response.isSuccessful){
            return response.body()
        }
        return null
    }
}
