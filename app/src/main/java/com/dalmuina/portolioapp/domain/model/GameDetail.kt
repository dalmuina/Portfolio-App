package com.dalmuina.portolioapp.domain.model

import com.dalmuina.portolioapp.data.model.GameDetailModel

data class GameDetail(
    val name :String ="",
    val descriptionRaw: String ="",
    val metacritic: Int = 0,
    val website: String =" ",
    val backgroundImage: String = ""
)

fun GameDetailModel.toDomain() = GameDetail(name, descriptionRaw, metacritic, website, backgroundImage)
