package com.curso.kotlin.domain

import com.dalmuina.portolioapp.data.QuoteRepository
import com.dalmuina.portolioapp.domain.GetRandomQuoteUseCase
import com.dalmuina.portolioapp.domain.model.QuoteItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GetRandomQuoteUseCaseTest{

    @RelaxedMockK
    private lateinit var quoteRepository: QuoteRepository

    private lateinit var getRandomQuoteUseCase: GetRandomQuoteUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getRandomQuoteUseCase = GetRandomQuoteUseCase(quoteRepository)
    }

    @Test
    fun `when the database is empty return null`() = runBlocking{
        //Given
        coEvery { quoteRepository.getAllQuotesFromDatabase() } returns emptyList()
        //When
        val response = getRandomQuoteUseCase()
        //Then
        assert(response == null)
    }

    @Test
    fun `when database is not empty then return quote`() = runBlocking {
        //Given
        val myList = listOf(QuoteItem("test","yo"))
        coEvery { quoteRepository.getAllQuotesFromDatabase() } returns myList

        //When
        val response = getRandomQuoteUseCase()

        //Then
        assert(myList[0] == response)
    }

}