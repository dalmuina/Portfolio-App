package com.dalmuina.portolioapp.data.model

import com.google.gson.annotations.SerializedName

data class GamesModel(
    val count: Int,
    val results: List<GameModel>
)

data class GameModel(
    val id:Int,
    val name:String,
    @SerializedName("background_image") val backgroundImage: String
)
