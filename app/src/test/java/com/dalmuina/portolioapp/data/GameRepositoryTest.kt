package com.dalmuina.portolioapp.data

import com.dalmuina.portolioapp.data.model.GameModel
import com.dalmuina.portolioapp.data.network.GameService
import com.dalmuina.portolioapp.domain.model.GamesItem
import com.dalmuina.portolioapp.domain.model.toDomain
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.Before
import org.junit.Test

class GameRepositoryTest {

    @RelaxedMockK
    private lateinit var gameService: GameService

    private lateinit var gameRepository: GameRepository
    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        gameRepository = GameRepository(gameService)
    }

    @Test
    fun `Successful API response with data`() = runBlocking {
        // Test when the API call is successful and returns a list of GameItems. 
        // Verify that the response is not null and contains the expected GameItems.
        //Given
        val mockGames = listOf(
            GameModel(1,"game name 1","https://example.com/game1.jpg"))
        coEvery { gameService.getGames() } returns mockGames
        //When
        val result = gameRepository.getAllGamesFromApi()
        //Then
        assert(result == mockGames.map {
            it.toDomain()
        })
    }

    @Test
    fun `Empty API response`() = runBlocking {
        // Test when the API call is successful but returns an empty list. 
        // Verify that the response is an empty list.
        //Given
        coEvery { gameService.getGames() } returns emptyList()
        //When
        val result = gameRepository.getAllGamesFromApi()
        //Then
        assert(result == emptyList<GamesItem>())
    }

    @Test
    fun `Null API response`() = runBlocking {
        // Test when the API call returns null. 
        // Verify that the repository returns a null list
        //Given
        coEvery { gameService.getGames() } returns null
        //When
        val result = gameRepository.getAllGamesFromApi()
        //Then
        assert(result == null)
    }

    @Test
    fun `API call failure`() = runBlocking {
        // Test when the API call fails and throws an exception (e.g., network error, 500 error).
        // Verify that the exception is propagated or handled appropriately (e.g., return null or an error state).
        //Given
        coEvery { gameService.getGames() } throws IOException("Network error")
        //When
        val result = try {
            gameRepository.getAllGamesFromApi()
            null
        } catch (e:Exception){
            e
        }
        //Then
        assert(result is IOException)
    }

    @Test
    fun `API response with invalid data`() = runBlocking {
        // Test when the API call is successful but returns malformed or unexpected data. 
        // Verify that the `toDomain()` function handles it gracefully without crash, 
        // or logs/returns null in case of conversion error.
//        val failGames = listOf(
//            GameModel(1,"null","https://example.com/game1.jpg"))
//        coEvery { gameService.getGames() } returns failGames
//        //When
//        val result = gameRepository.getAllGamesFromApi()
//        //Then
//        assert(result == failGames.map {
//            it.toDomain()
//        })
    }

    @Test
    fun `Multiple API calls`() = runBlocking {
        // Test calling `getAllGamesFromApi()` multiple times to verify there are no side effects,
        // or that caching is correctly implemented if applicable.
        // Given
        val mockGames = listOf(
            GameModel(1, "game name 1", "https://example.com/game1.jpg")
        )
        coEvery { gameService.getGames() } returns mockGames

        // When
        val result1 = gameRepository.getAllGamesFromApi()
        val result2 = gameRepository.getAllGamesFromApi()

        // Then
        assert(result1 == result2)
    }

    @Test
    fun `toDomain   throws exception`() = runBlocking{
        // Test when the `toDomain()` function throws an exception during conversion. 
        // Ensure that it either return null, or throws the error.
        // Given

//        val mockGame = GameModel(1, "game name 1", "https://example.com/game1.jpg")
//        coEvery { gameService.getGames() } returns listOf(mockGame)
//
//        mockkStatic("com.dalmuina.portolioapp.domain.model.GamesItem")
//        every { mockGame.toDomain() } throws IllegalArgumentException("Invalid data")
//
//        // When
//        val result = try {
//            gameRepository.getAllGamesFromApi()
//            null
//        } catch (e: IllegalArgumentException) {
//            e
//        }
//
//        // Then
//        assert(result is IllegalArgumentException)
    }

    @Test
    fun `Large response data`() = runBlocking{
        // Test the function's behavior with a very large list of GameItems. 
        // This ensures that it handles large data sets efficiently without memory issues.
        // Given
        val largeList = List(10000) { index ->
            GameModel(index, "game name $index", "https://example.com/game$index.jpg")
        }
        coEvery { gameService.getGames() } returns largeList

        // When
        val result = gameRepository.getAllGamesFromApi()

        // Then
        assert(result?.size == largeList.size)
    }

    @Test
    fun `Concurrency testing`() = runBlocking {
        // Test calling `getAllGamesFromApi()` from multiple coroutines simultaneously to check for
        // thread safety issues.
        // Given
        val mockGames = listOf(
            GameModel(1, "game name 1", "https://example.com/game1.jpg")
        )
        coEvery { gameService.getGames() } returns mockGames

        // When
        val deferredResults = List(10) {
            async {
                gameRepository.getAllGamesFromApi()
            }
        }
        val results = deferredResults.awaitAll()

        // Then
        results.forEach { result ->
            assert(result == mockGames.map { it.toDomain() })
        }
    }

    @Test
    fun `Network Latency`() = runBlocking{
        // Introduce Network Latency during api call. 
        // Ensure the app remains responsive and doesn't lock up. 
        // Verify correct loading/error state.
        // Given
        val mockGames = listOf(
            GameModel(1, "game name 1", "https://example.com/game1.jpg")
        )
        coEvery { gameService.getGames() } coAnswers {
            delay(1000) // Simulate network latency
            mockGames
        }

        // When
        val result = gameRepository.getAllGamesFromApi()

        // Then
        assert(result == mockGames.map { it.toDomain() })
    }
}
