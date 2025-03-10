package com.dalmuina.portolioapp.ui.state

data class GameState(
    val name :String ="",
    val descriptionRaw: String ="",
    val metacritic: Int = 0,
    val website: String =" ",
    val backgroundImage: String = ""
)
