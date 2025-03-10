package com.dalmuina.portolioapp.data.network

import com.dalmuina.portolioapp.data.model.GamesModel
import com.dalmuina.portolioapp.utils.Constants.API_KEY
import com.dalmuina.portolioapp.utils.Constants.ENDPOINT
import retrofit2.Response
import retrofit2.http.GET

interface GameApiClient {

    @GET(ENDPOINT + API_KEY)
    suspend fun getGames(): Response<GamesModel>
}

