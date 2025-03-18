package com.dalmuina.portolioapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.dalmuina.portolioapp.domain.GetAllGamesUseCase
import com.dalmuina.portolioapp.domain.GetGameByIdUseCase
import com.dalmuina.portolioapp.domain.model.GameDetail
import com.dalmuina.portolioapp.domain.model.GameItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GamesViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var getAllGamesUseCase: GetAllGamesUseCase

    @RelaxedMockK
    private lateinit var getGameByIdUseCase: GetGameByIdUseCase

    private lateinit var gamesViewModel: GamesViewModel

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        gamesViewModel = GamesViewModel(getAllGamesUseCase,getGameByIdUseCase)
    }

    @After
    fun onAfter(){
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `getGames initial state`() = runTest {
        // Verify that `getGames()` initially emits an empty list when the ViewModel 
        // is first created.
        //When
        val games = gamesViewModel.games.first()
        //Then
        assert(games == emptyList<GameItem>())
    }

    @Test
    fun `getGames successful fetch`() = runTest {
        // Given
        val gameList = listOf(GameItem(id = 1, name = "Game 1", "image_background"))
        coEvery { getAllGamesUseCase() } returns gameList

        assertEquals(emptyList<GameItem>(), gamesViewModel.games.value)

        // When
        // Then
        gamesViewModel.games.test {
            gamesViewModel.fetchGames()
            advanceUntilIdle()
            assertEquals(emptyList<GameItem>(), awaitItem()) // Initial state
            assertEquals(gameList, awaitItem()) // Updated state
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getGames fetch failure`() = runTest {
        // Test that `getGames()` emits an empty list when `getAllGamesUseCase` returns 
        // `null`.
        coEvery { getAllGamesUseCase() } returns null
        //When
        val games = gamesViewModel.games.first()
        //Then
        assert(games == emptyList<GameItem>())
    }

    @Test
    fun `getGames multiple emissions`() = runTest {
        // Ensure that `getGames()` correctly emits updates if `getAllGamesUseCase` 
        // result changes or is called multiple times.
        // Given
        val gameList1 = listOf(GameItem(id = 1, name = "Game 1", "image_background"))
        val gameList2 = listOf(GameItem(id = 2, name = "Game 2", "image_background"))
        coEvery { getAllGamesUseCase() } returnsMany listOf(gameList1, gameList2)
        // Then
        gamesViewModel.games.test {
            gamesViewModel.fetchGames()
            advanceUntilIdle()
            assertEquals(emptyList<GameItem>(), awaitItem()) // Initial state
            assertEquals(gameList1, awaitItem()) // Updated state
            gamesViewModel.fetchGames()
            advanceUntilIdle()
            assertEquals(gameList2, awaitItem()) // Updated state
            cancelAndIgnoreRemainingEvents()
            }
    }


    @Test
    fun `getGameById successful fetch`() = runTest {
        // Check if `getGameById()` correctly updates the `detail` state with the 
        // information from `getGameByIdUseCase` when given a valid id.
        // Given
        val gameDetail = GameDetail(
            name = "Game 1",
            descriptionRaw = "Description",
            metacritic = 90,
            website = "https://example.com",
            backgroundImage = "image_url"
        )
        coEvery { getGameByIdUseCase(1) } returns gameDetail

        // When
        gamesViewModel.getGameById(1)
        advanceUntilIdle()
        // Then
        val actualDetail = gamesViewModel.detail.first()
        Assert.assertEquals(gameDetail, actualDetail)
    }

    @Test
    fun `getGameById invalid id`() = runTest {
        // Test if `getGameById()` sets the `detail` state to default values when 
        // `getGameByIdUseCase` returns `null` for an invalid id.
        // Given
        coEvery { getGameByIdUseCase(999) } returns null

        // When
        gamesViewModel.getGameById(999)
        advanceUntilIdle()

        // Then
        with(gamesViewModel.detail.value) {
            assertEquals("", name)
            assertEquals("", descriptionRaw)
            assertEquals(0, metacritic)
        }
    }

    @Test
    fun `getGameById boundary ids`() = runTest {
        // Check `getGameById()` with boundary ids such as 0, 1, or `Int.MAX_VALUE` to
        // verify that no issues happens with extreme cases.
        // Given
        val gameDetail = GameDetail(
            name = "Game 1",
            descriptionRaw = "Description",
            metacritic = 90,
            website = "https://example.com",
            backgroundImage = "image_url"
        )
        coEvery { getGameByIdUseCase(any()) } returns gameDetail

        // When & Then
        gamesViewModel.getGameById(0)
        advanceUntilIdle()
        val actualDetail1 = gamesViewModel.detail.first()
        assertEquals("Game 1", actualDetail1.name)
        gamesViewModel.getGameById(1)
        advanceUntilIdle()
        val actualDetail2 = gamesViewModel.detail.first()
        assertEquals("Game 1", actualDetail2.name)
        gamesViewModel.getGameById(Int.MAX_VALUE)
        advanceUntilIdle()
        val actualDetail3 = gamesViewModel.detail.first()
        assertEquals("Game 1", actualDetail3.name)
    }

    @Test
    fun `getGameById multiple calls`() = runTest {
        // Verify if `getGameById()` works correctly when called multiple times with 
        // different or same ids.
        // Given
        val gameDetail1 = GameDetail(name = "Game 1", descriptionRaw = "Description 1",
            metacritic = 90, website = "", backgroundImage = "")
        val gameDetail2 = GameDetail(name = "Game 2", descriptionRaw = "Description 2",
            metacritic = 80, website = "", backgroundImage = "")
        coEvery { getGameByIdUseCase(1) } returns gameDetail1
        coEvery { getGameByIdUseCase(2) } returns gameDetail2

        gamesViewModel.detail.test {
            // Check the initial default value.
            val initialDetail = awaitItem()
            assertEquals("", initialDetail.name)
            gamesViewModel.getGameById(1)
            advanceUntilIdle()
            val actualDetail1 = awaitItem()
            assertEquals("Game 1", actualDetail1.name)
            gamesViewModel.getGameById(2)
            advanceUntilIdle()
            val actualDetail2 = awaitItem()
            assertEquals("Game 2", actualDetail2.name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `clean after getGameById`() = runTest {
        // Ensure that calling `clean()` after calling `getGameById()` correctly reverts 
        // the `detail` state to the default.
        // Given
        val gameDetail = GameDetail(name = "Game 1", descriptionRaw = "Description",
            metacritic = 90, website = "", backgroundImage = "")
        coEvery { getGameByIdUseCase(1) } returns gameDetail

        // When

        // Then
        gamesViewModel.detail.test {
            gamesViewModel.getGameById(1)
            gamesViewModel.clean()
            advanceUntilIdle()
            val initialDetail = awaitItem()
            assertEquals("", initialDetail.name)
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun `clean multiple calls`() = runTest {
        // Check that calling `clean()` multiple times in a row does not produce unexpected 
        // behavior
        // Given
        gamesViewModel.clean()

        // When
        gamesViewModel.clean()
        gamesViewModel.clean()
        gamesViewModel.clean()

        // Then
        with(gamesViewModel.detail.value) {
            assertEquals("", name)
            assertEquals("", descriptionRaw)
            assertEquals(0, metacritic)
        }
    }

    @Test
    fun `detail initial state`() = runTest {
        // Verify that the initial state of the `detail` variable is set to a 
        // `GameDetail` with the default parameters.
        // Then
        with(gamesViewModel.detail.value) {
            assertEquals("", name)
            assertEquals("", descriptionRaw)
            assertEquals(0, metacritic)
        }
    }

    @Test
    fun `StateFlow no concurrent modification`() = runTest {
        // Verify no concurrent modification when working with the StateFlow 
        // Given
        val gameList = listOf(GameItem(id = 1, name = "Game 1", "image_background"))
        coEvery { getAllGamesUseCase() } returns gameList

        // When
        // Then
        gamesViewModel.games.test {
            gamesViewModel.fetchGames()
            advanceUntilIdle()
            assertEquals(emptyList<GameItem>(), awaitItem()) // Initial state
            assertEquals(gameList, awaitItem()) // Updated state
            cancelAndIgnoreRemainingEvents()
        }
    }

}
