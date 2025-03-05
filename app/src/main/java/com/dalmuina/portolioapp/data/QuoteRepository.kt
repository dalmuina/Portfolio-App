package com.dalmuina.portolioapp.data

import com.dalmuina.portolioapp.data.database.dao.QuoteDao
import com.dalmuina.portolioapp.data.database.entities.QuoteEntity
import com.dalmuina.portolioapp.data.network.QuoteService
import com.dalmuina.portolioapp.domain.model.QuoteItem
import com.dalmuina.portolioapp.domain.model.toDomain
import javax.inject.Inject

class QuoteRepository @Inject constructor(private val api : QuoteService, private val dao: QuoteDao){

    suspend fun getAllQuotesFromApi(): List<QuoteItem>{
        val response = api.getQuotes()
        return response.map {
            it.toDomain()
        }
    }

    suspend fun getAllQuotesFromDatabase(): List<QuoteItem>{
        val response = dao.getAllQuotes()
        return response.map {
            it.toDomain()
        }
    }

    suspend fun insertQuotes(quotes: List<QuoteEntity>){
        dao.insertAll(quotes)
    }

    suspend fun clearQuotes(){
        dao.deleteAllQuotes()
    }


}