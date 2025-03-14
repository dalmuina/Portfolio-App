package com.dalmuina.portolioapp.data

import com.dalmuina.portolioapp.data.model.GameDetailModel
import com.dalmuina.portolioapp.data.model.GameModel
import com.dalmuina.portolioapp.data.network.GameService
import com.dalmuina.portolioapp.domain.model.GamesItem
import com.dalmuina.portolioapp.domain.model.toDomain
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.After
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

    @After
    fun onAfter() {
        unmockkStatic("com.dalmuina.portolioapp.domain.model.GamesItemKt")
        unmockkStatic("com.dalmuina.portolioapp.domain.model.GameDetailKt")
    }

    /********************************/
    /*      GetAllGamesUseCase      */
    /********************************/
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
        val failGames = listOf(
            GameModel(1,null,null))
        coEvery { gameService.getGames() } returns failGames
        //When
        val result = gameRepository.getAllGamesFromApi()
        //Then
        assert(result == failGames.map {
            it.toDomain()
        })
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
    fun `GamesItem toDomain throws exception`() = runBlocking{
        // Test when the `toDomain()` function throws an exception during conversion. 
        // Ensure that it either return null, or throws the error.
        // Given

        val mockGame = GameModel(1, "game name 1", "https://example.com/game1.jpg")
        coEvery { gameService.getGames() } returns listOf(mockGame)

        mockkStatic("com.dalmuina.portolioapp.domain.model.GamesItemKt")
        every { mockGame.toDomain() } throws IllegalArgumentException("Invalid data")

        // When
        val result = try {
            gameRepository.getAllGamesFromApi()
            null
        } catch (e: IllegalArgumentException) {
            e
        }

        // Then
        assert(result is IllegalArgumentException)
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

    @Test
    fun `Successful API response with valid game data`() = runBlocking {
        // Test when the API call to getGameById is successful
        // and returns a valid GameDetail object. Verify that the returned GameDetail object is not null
        // and its properties match the expected values.
        val mockGame = GameDetailModel("game name","description game",99,"Website","background_image")
        coEvery { gameService.getGameById(1) } returns mockGame
        //When
        val result = gameRepository.getGameById(1)
        //Then
        assert(result == mockGame.toDomain())
    }

    @Test
    fun `API returns null response for a game`() = runBlocking {
        // Test when the API call is successful but returns null.
        // Verify that the repository returns a null.
        coEvery { gameService.getGameById(1) } returns null
        //When
        val result = gameRepository.getGameById(1)
        //Then
        assert(result == null)
    }

    @Test
    fun `Non existent Game ID`() = runBlocking {
        // Test when the API is called with an ID that does not exist in the database.
        // Verify that the repository handles it appropriately, e.g., returns null.
        coEvery { gameService.getGameById(999) } returns null
        //When
        val result = gameRepository.getGameById(999)
        //Then
        assert(result == null)
    }

    @Test
    fun `Negative Game ID`() = runBlocking {
        // Test when the API is called with a negative ID.
        // This might represent an invalid ID scenario.
        // Check if the repository returns null or throws an exception.
        coEvery { gameService.getGameById(-1) } returns null
        // When
        val result = gameRepository.getGameById(-1)
        // Then
        assert(result == null)
    }

    @Test
    fun `Zero Game ID`() = runBlocking {
        // Test when the API is called with an ID of zero.
        // This might be a boundary case for the ID.
        // Check if the repository returns null or throws an exception.
        coEvery { gameService.getGameById(0) } returns null
        // When
        val result = gameRepository.getGameById(0)
        // Then
        assert(result == null)
    }

    @Test
    fun `GameDetail ToDomain throws and exception`() = runBlocking {
        // Test when the API call to getGameById fails and throws an exception
        // (e.g., network error). Verify that the exception is propagated or handled appropriately
        // (e.g., return null or an error state).
        //Given
        val mockGame = GameDetailModel("game name","description game",99,"Website","background_image")
        coEvery { gameService.getGameById(1) } returns mockGame
        mockkStatic("com.dalmuina.portolioapp.domain.model.GameDetailKt")
        every { mockGame.toDomain()} throws  IllegalArgumentException("Invalid data")
        // When
        val result = try {
            gameRepository.getGameById(1)
            null
        } catch (e: Exception) {
            e
        }
        // Then
        assert(result is IllegalArgumentException)
    }

    @Test
    fun `API call failure for game by ID`() = runBlocking {
        // Test when the API call to getGameById fails and throws an exception
        // (e.g., network error). Verify that the exception is propagated or handled appropriately
        // (e.g., return null or an error state).
        coEvery { gameService.getGameById(1) } throws IOException("Network error")
        // When
        val result = try {
            gameRepository.getGameById(1)
            null
        } catch (e: Exception) {
            e
        }
        // Then
        assert(result is IOException)
    }

    @Test
    fun `API response with invalid game data`() = runBlocking {
        // Test when the API call is successful but returns malformed or unexpected data.
        // Verify that the `toDomain()` function handles it gracefully without crashing,
        // or logs/returns null in case of a conversion error.
        val mockGame = GameDetailModel("", "", -1, "", "")
        coEvery { gameService.getGameById(1) } returns mockGame
        // When
        val result = gameRepository.getGameById(1)
        // Then
        assert(result != null)
    }

    @Test
    fun `toDomain throws exception for getGameById`() = runBlocking {
        // Test when the `toDomain()` function throws an exception
        // during the conversion of the GameDetail data. Ensure that it either returns null,
        // or throws the error.
        coEvery { gameService.getGameById(1) } throws IOException("Network error")
        //When
        val result = try {
            gameRepository.getGameById(1)
            null
        } catch (e: Exception){
            e
        }
        //Then
        assert(result is IOException)
    }

    @Test
    fun `Max integer ID`() = runBlocking {
        // Test when the input id is Int.MAX_VALUE.
        // It would ensure the code can handle the largest id value.
        val mockGame = GameDetailModel("game name","description game",99,"Website","background_image")
        coEvery { gameService.getGameById(Int.MAX_VALUE) } returns mockGame
        //When
        val result = gameRepository.getGameById(Int.MAX_VALUE)
        //Then
        assert(result == mockGame.toDomain())
    }

    @Test
    fun `Concurrency testing getGameById`() = runBlocking {
        // Test calling `getGameById()` from multiple coroutines simultaneously
        // to check for thread safety issues.
        val mockGame = GameDetailModel("game name","description game",99,"Website","background_image")
        coEvery { gameService.getGameById(1) } returns mockGame
        //When
        val deferredResults = List(10){
            async {
                gameRepository.getGameById(1)
            }
        }
        val results = deferredResults.awaitAll()
        //Then
        results.forEach{ result ->
            assert(result == mockGame.toDomain())
        }
    }

    @Test
    fun `Network Latency getGameById`() = runBlocking {
        // Introduce Network Latency during getGameById api call.
        // Ensure the app remains responsive and doesn't lock up.
        // Verify correct loading/error state.
        val mockGame = GameDetailModel("game name","description game",99,"Website","background_image")
        coEvery { gameService.getGameById(1) } coAnswers {
            delay(1000)
            mockGame
        }
        //When
        val result = gameRepository.getGameById(1)
        //Then
        assert(result == mockGame.toDomain())
    }
}
