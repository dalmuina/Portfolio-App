package com.dalmuina.portolioapp.domain

import com.dalmuina.portolioapp.data.QuoteRepository
import com.dalmuina.portolioapp.data.database.entities.toDatabase
import com.dalmuina.portolioapp.domain.model.QuoteItem
import javax.inject.Inject

class GetQuoteUseCase @Inject constructor(private val repository: QuoteRepository){

    suspend operator fun invoke():List<QuoteItem> {
        val quotes = repository.getAllQuotesFromApi()

        return if (quotes.isNotEmpty()) {
            repository.clearQuotes()
            repository.insertQuotes(quotes.map { it.toDatabase() })
            quotes
        } else {
            repository.getAllQuotesFromDatabase()
        }
    }
}
