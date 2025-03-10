package com.dalmuina.portolioapp.data.model

data class GamesModel(
    val count: Int,
    val results: List<GameModel>
)

data class GameModel(
    val id:Int,
    val name:String,
    val background_image: String
)
