package com.dalmuina.portolioapp.domain.model

import com.dalmuina.portolioapp.data.database.entities.QuoteEntity
import com.dalmuina.portolioapp.data.model.QuoteModel

data class QuoteItem(
    val quote:String,
    val author:String
)

fun QuoteModel.toDomain() = QuoteItem(quote,author)
fun QuoteEntity.toDomain() = QuoteItem(quote,author)
