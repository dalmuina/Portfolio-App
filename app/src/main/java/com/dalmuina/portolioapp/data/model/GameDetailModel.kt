package com.dalmuina.portolioapp.data.model

import com.google.gson.annotations.SerializedName

data class GameDetailModel(
    @SerializedName("name") val name : String,
    @SerializedName("description_raw") val descriptionRaw : String,
    @SerializedName("metacritic") val metacritic: Int,
    @SerializedName("website") val website : String,
    @SerializedName("background_image") val backgroundImage: String
)
