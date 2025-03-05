package com.dalmuina.portolioapp.domain

import com.dalmuina.portolioapp.data.QuoteRepository
import com.dalmuina.portolioapp.domain.model.QuoteItem
import javax.inject.Inject

class GetRandomQuoteUseCase @Inject constructor(private val repository: QuoteRepository){

    suspend operator fun invoke(): QuoteItem?{
        val quotes = repository.getAllQuotesFromDatabase()
        if (quotes.isNotEmpty()){
            val randomNumber = quotes.indices.random()
            return quotes[randomNumber]
        }
        return null
    }
}