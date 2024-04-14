package dev.modena.heroes.home.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.modena.heroes.data.local.entity.Hero

import dev.modena.heroes.repository.HeroRepository
import dev.modena.heroes.shared.NetworkConnection
import dev.modena.marvel.model.Data
import dev.modena.marvel.model.HeroData
import dev.modena.marvel.model.ResponseMarvel
import dev.modena.marvel.model.Thumbnail
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotSame
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.UnknownHostException

@RunWith(MockitoJUnitRunner::class)
class SearchHeroViewModelTest {

    @Mock
    private lateinit var network: NetworkConnection

    @Mock
    private lateinit var repository: HeroRepository
    private lateinit var viewModel: SearchHeroViewModel
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchHeroViewModel(network, repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun initializationWithConnection() = runTest {
        // given
        val responseMarvel = mock(ResponseMarvel::class.java)
        `when`(network.isInternetAvailable()).thenReturn(true)
        `when`(repository.getCharactersMarvel()).thenReturn(flow { (Result.success(responseMarvel)) })

        // when
        viewModel.initialize()
        advanceUntilIdle()

        // then
        assertEquals(true, viewModel.hasInternet.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun initializationWithoutConnection() = runTest {
        // given
        `when`(network.isInternetAvailable()).thenReturn(false)

        // when
        viewModel.initialize()
        advanceUntilIdle()

        // then
        assertEquals(false, viewModel.hasInternet.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldShowListHeroInInitialization() = runTest {
        // given
        val mockHeroList = listOf(HeroData(1, "HeroName", "HeroDescription", "imageUrl", Thumbnail("path", "png")))
        val responseMarvel = mock(ResponseMarvel::class.java).apply {
            `when`(this.data).thenReturn(Data(0, 10, 1000, 10, mockHeroList))
        }
        `when`(network.isInternetAvailable()).thenReturn(true)
        `when`(repository.getCharactersMarvel()).thenReturn(flow { emit((Result.success(responseMarvel))) })
        `when`(repository.getAllIdsHeroesMarvel()).thenReturn(listOf())

        // when
        viewModel.initialize()
        advanceUntilIdle()

        // then
        assertEquals(true, viewModel.hasInternet.first())
        assertEquals(Hero.createByMarvel(responseMarvel, listOf()), viewModel.heroes.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldShowListHeroInInitializationWithOneFavorite() = runTest {
        // given
        val mockHeroList = listOf(HeroData(1L, "HeroName", "HeroDescription", "imageUrl", Thumbnail("path", "png")))
        val responseMarvel = mock(ResponseMarvel::class.java).apply {
            `when`(this.data).thenReturn(Data(0, 10, 1000, 10, mockHeroList))
        }
        val idsFavorites = listOf(1L)
        `when`(network.isInternetAvailable()).thenReturn(true)
        `when`(repository.getCharactersMarvel()).thenReturn(flow { emit((Result.success(responseMarvel))) })
        `when`(repository.getAllIdsHeroesMarvel()).thenReturn(idsFavorites)
        val heroes = Hero.createByMarvel(responseMarvel, idsFavorites)

        // when
        viewModel.initialize()
        advanceUntilIdle()

        // then
        assertEquals(heroes[0].isFavorite, viewModel.heroes.first()[0].isFavorite)
        assertTrue(viewModel.hasInternet.first())
        assertEquals(true, viewModel.heroes.first()[0].isFavorite)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldShowListHeroInInitializationWithoutAnyOneFavorite() = runTest {
        // given
        val mockHeroList = listOf(HeroData(1L, "HeroName", "HeroDescription", "imageUrl", Thumbnail("path", "png")))
        val responseMarvel = mock(ResponseMarvel::class.java).apply {
            `when`(this.data).thenReturn(Data(0, 10, 1000, 10, mockHeroList))
        }
        val idsFavorites = listOf(2L)
        `when`(network.isInternetAvailable()).thenReturn(true)
        `when`(repository.getCharactersMarvel()).thenReturn(flow { emit((Result.success(responseMarvel))) })
        `when`(repository.getAllIdsHeroesMarvel()).thenReturn(idsFavorites)
        val heroes = Hero.createByMarvel(responseMarvel, idsFavorites)

        // when
        viewModel.initialize()
        advanceUntilIdle()

        // then
        assertEquals(heroes[0].isFavorite, viewModel.heroes.first()[0].isFavorite)
        assertTrue(viewModel.hasInternet.first())
        assertEquals(false, viewModel.heroes.first()[0].isFavorite)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldShowTryAgainWithoutConnectionSuccess() = runTest {
        // given
        val mockHeroList = listOf(HeroData(1L, "HeroName", "HeroDescription", "imageUrl", Thumbnail("path", "png")))
        val responseMarvel = mock(ResponseMarvel::class.java).apply {
            `when`(this.data).thenReturn(Data(0, 10, 1000, 10, mockHeroList))
        }
        val idsFavorites = listOf(2L)
        `when`(network.isInternetAvailable()).thenReturn(false)
        `when`(repository.getCharactersMarvel()).thenReturn(flow { emit((Result.success(responseMarvel))) })
        `when`(repository.getAllIdsHeroesMarvel()).thenReturn(idsFavorites)
        val heroes = Hero.createByMarvel(responseMarvel, idsFavorites)

        // when
        viewModel.initialize()
        advanceUntilIdle()
        assertEquals(false, viewModel.hasInternet.first())
        `when`(network.isInternetAvailable()).thenReturn(true)
        viewModel.tryAgain()
        advanceUntilIdle()

        // then
        assertEquals(heroes[0], viewModel.heroes.first()[0])
        assertTrue(viewModel.hasInternet.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldShowTryAgainWithoutConnectionError() = runTest {
        // given
        `when`(network.isInternetAvailable()).thenReturn(false)

        // when
        viewModel.initialize()
        advanceUntilIdle()
        assertEquals(false, viewModel.hasInternet.first())
        `when`(network.isInternetAvailable()).thenReturn(false)
        viewModel.tryAgain()

        // then
        assertFalse(viewModel.hasInternet.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldShowTryAgainGenericError() = runTest {
        // given
        `when`(network.isInternetAvailable()).thenReturn(true)
        `when`(repository.getCharactersMarvel()).thenReturn(flow { emit((Result.failure(Exception()))) })

        // when
        viewModel.initialize()
        advanceUntilIdle()

        // then
        assertTrue(viewModel.hasErrors.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldShowGenericErrorTryAgainSuccess() = runTest {
        // given
        `when`(network.isInternetAvailable()).thenReturn(true)
        `when`(repository.getCharactersMarvel()).thenReturn(flow { emit((Result.failure(Exception()))) })

        // when
        viewModel.initialize()
        advanceUntilIdle()
        assertTrue(viewModel.hasErrors.first())
        val mockHeroList = listOf(HeroData(1L, "HeroName", "HeroDescription", "imageUrl", Thumbnail("path", "png")))
        val responseMarvel = mock(ResponseMarvel::class.java).apply {
            `when`(this.data).thenReturn(Data(0, 10, 1000, 10, mockHeroList))
        }
        val idsFavorites = listOf(2L)
        `when`(repository.getCharactersMarvel()).thenReturn(flow { emit((Result.success(responseMarvel))) })
        `when`(repository.getAllIdsHeroesMarvel()).thenReturn(idsFavorites)
        viewModel.tryAgain()
        advanceUntilIdle()

        // then
        assertFalse(viewModel.hasErrors.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldShowNextPage() = runTest {
        // given
        val mockHeroList = listOf(HeroData(1L, "HeroName", "HeroDescription", "imageUrl", Thumbnail("path", "png")))
        val responseMarvel = mock(ResponseMarvel::class.java).apply {
            `when`(this.data).thenReturn(Data(0, 10, 1000, 10, mockHeroList))
        }
        val idsFavorites = listOf(1L)
        `when`(network.isInternetAvailable()).thenReturn(true)
        `when`(repository.getCharactersMarvel()).thenReturn(flow { emit((Result.success(responseMarvel))) })
        `when`(repository.getAllIdsHeroesMarvel()).thenReturn(idsFavorites)
        val newMockHeroList = listOf(HeroData(2L, "NewHeroName", "NewHeroDescription", "imageUrl", Thumbnail("path", "png")))
        val newResponseMarvel = mock(ResponseMarvel::class.java).apply {
            `when`(this.data).thenReturn(Data(10, 10, 1000, 20, newMockHeroList))
        }
        val heroes = Hero.createByMarvel(responseMarvel, idsFavorites)
        val newHeroes = Hero.createByMarvel(newResponseMarvel, idsFavorites)

        // when
        viewModel.initialize()
        advanceUntilIdle()
        assertEquals(heroes[0], viewModel.heroes.first()[0])
        assertTrue(viewModel.page.first().isEnableNextPage())
        assertFalse(viewModel.page.first().isEnableBackPage())

        `when`(repository.navigatePage(viewModel.page.first().nextPage(), "")).thenReturn(flow { emit((Result.success(newResponseMarvel))) })
        viewModel.navigationPage(viewModel.page.first().nextPage())
        advanceUntilIdle()

        // then
        assertNotSame(newResponseMarvel, responseMarvel)
        assertTrue(viewModel.page.first().isEnableNextPage())
        assertTrue(viewModel.page.first().isEnableBackPage())
        assertEquals(newHeroes[0], viewModel.heroes.first()[0])
        assertFalse(viewModel.heroes.first()[0].isFavorite)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldShowBackPage() = runTest {
        // given
        val mockHeroList = listOf(HeroData(2L, "HeroName", "HeroDescription", "imageUrl", Thumbnail("path", "png")))
        val responseMarvel = mock(ResponseMarvel::class.java).apply {
            `when`(this.data).thenReturn(Data(10, 10, 1564, 10, mockHeroList))
        }
        val idsFavorites = listOf(1L)
        `when`(network.isInternetAvailable()).thenReturn(true)
        `when`(repository.getCharactersMarvel()).thenReturn(flow { emit((Result.success(responseMarvel))) })
        `when`(repository.getAllIdsHeroesMarvel()).thenReturn(idsFavorites)
        val newMockHeroList = listOf(HeroData(1L, "NewHeroName", "NewHeroDescription", "imageUrl", Thumbnail("path", "png")))
        val newResponseMarvel = mock(ResponseMarvel::class.java).apply {
            `when`(this.data).thenReturn(Data(0, 10, 1000, 10, newMockHeroList))
        }
        val heroes = Hero.createByMarvel(responseMarvel, idsFavorites)
        val newHeroes = Hero.createByMarvel(newResponseMarvel, idsFavorites)

        // when
        viewModel.initialize()
        advanceUntilIdle()
        assertEquals(heroes[0], viewModel.heroes.first()[0])
        assertTrue(viewModel.page.first().isEnableNextPage())
        assertTrue(viewModel.page.first().isEnableBackPage())

        `when`(repository.navigatePage(viewModel.page.first().backPage(), "")).thenReturn(flow { emit((Result.success(newResponseMarvel))) })
        viewModel.navigationPage(viewModel.page.first().backPage())
        advanceUntilIdle()

        // then
        assertTrue(viewModel.page.first().isEnableNextPage())
        assertFalse(viewModel.page.first().isEnableBackPage())
        assertEquals(newHeroes[0], viewModel.heroes.first()[0])
        assertTrue(viewModel.heroes.first()[0].isFavorite)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldShowSearchByNameSuccess() = runTest {
        // given
        `when`(repository.getAllIdsHeroesMarvel()).thenReturn(listOf())
        val mockHeroList = listOf(HeroData(1L, "Iron", "IronDescription", "imageUrl", Thumbnail("path", "png")))
        val responseMarvel = mock(ResponseMarvel::class.java).apply {
            `when`(this.data).thenReturn(Data(0, 1, 1, 1, mockHeroList))
        }
        val query = "Iron"
        `when`(repository.getCharactersMarvelByName(query)).thenReturn(flow { emit((Result.success(responseMarvel))) })
        val heroes = Hero.createByMarvel(responseMarvel, listOf())

        // when
        viewModel.searchByName(query)
        advanceUntilIdle()

        // then
        assertEquals(heroes[0], viewModel.heroes.first()[0])
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldShowSearchByNameResultEmpty() = runTest {
        // given
        `when`(repository.getAllIdsHeroesMarvel()).thenReturn(listOf())
        val responseMarvel = mock(ResponseMarvel::class.java).apply {
            `when`(this.data).thenReturn(Data(0, 10, 0, 0, listOf()))
        }
        val query = "Leandro"
        `when`(repository.getCharactersMarvelByName(query)).thenReturn(flow { emit((Result.success(responseMarvel))) })

        // when
        viewModel.searchByName(query)
        advanceUntilIdle()

        // then
        assertTrue(viewModel.heroes.first().isEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldShowSearchByNameResultError() = runTest {
        // given
        val query = "Leandro"
        `when`(repository.getCharactersMarvelByName(query)).thenReturn(flow { emit((Result.failure(Exception()))) })

        // when
        viewModel.searchByName(query)
        advanceUntilIdle()

        // then
        assertTrue(viewModel.hasErrors.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldShowSearchByNameNotConnection() = runTest {
        // given
        val query = "Leandro"
        `when`(repository.getCharactersMarvelByName(query)).thenReturn(flow {
            emit((Result.failure(UnknownHostException())))
        })

        // when
        viewModel.searchByName(query)
        advanceUntilIdle()

        // then
        assertFalse(viewModel.hasInternet.first())
    }

}