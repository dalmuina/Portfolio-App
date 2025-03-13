package com.dalmuina.portolioapp.domain.model

import com.dalmuina.portolioapp.data.model.GameModel

data class GamesItem(
    val count: Int,
    val result: List<GameItem>
)

data class GameItem(
    val id:Int,
    val name:String?,
    val backgroundImage: String?
)

fun GameModel.toDomain() = GameItem(id, name, backgroundImage)
