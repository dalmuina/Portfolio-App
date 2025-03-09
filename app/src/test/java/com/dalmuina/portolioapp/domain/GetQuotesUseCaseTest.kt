package com.dalmuina.portolioapp.domain

import com.dalmuina.portolioapp.data.QuoteRepository
import com.dalmuina.portolioapp.domain.model.QuoteItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GetQuotesUseCaseTest {

    @RelaxedMockK
    private lateinit var quoteRepository: QuoteRepository

    private lateinit var getQuotesUseCase: GetQuoteUseCase
    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getQuotesUseCase = GetQuoteUseCase(quoteRepository)
    }

    @Test
    fun `when the api doesnt return anything then get values from database`() = runBlocking{
        //Given
        coEvery { quoteRepository.getAllQuotesFromApi() } returns emptyList()
        //When
        getQuotesUseCase()
        //Then
        coVerify(exactly = 1) { quoteRepository.getAllQuotesFromDatabase() }

    }

    @Test
    fun `when the api return something then get values from api`() = runBlocking {
        //Given
        val myList = listOf(QuoteItem("test","yo"))
        coEvery { quoteRepository.getAllQuotesFromApi() } returns myList
        //When
        val response = getQuotesUseCase()
        //Then
        coVerify(exactly = 1) {
            quoteRepository.clearQuotes()
            quoteRepository.insertQuotes(any())
        }
        coVerify(exactly = 0) {quoteRepository.getAllQuotesFromDatabase()}
        assert(myList == response)
    }
}
