package dev.modena.heroes.home.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.modena.heroes.data.local.entity.Hero
import dev.modena.heroes.home.HomeViewModel
import dev.modena.heroes.repository.HeroRepository
import dev.modena.marvel.model.ResponseMarvel
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavoriteHeroViewModelTest {

    @Mock
    private lateinit var repository: HeroRepository
    private lateinit var viewModel: FavoriteHeroViewModel
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = FavoriteHeroViewModel(repository)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldInitializeWithoutFavorites() = runTest {
        // given
        val heroes = listOf<Hero>()
        `when`(repository.getListHeroes()).thenReturn(flow { emit(heroes) })
        `when`(repository.getAllIdsHeroesMarvel()).thenReturn(listOf())

        // when
        viewModel.initialize()
        advanceUntilIdle()

        // then
        assertTrue(viewModel.heroes.first().isEmpty())
        assertEquals(heroes, viewModel.heroes.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldInitializeWithHeroesFavorites() = runTest {
        // given
        val heroes = listOf<Hero>(
            Hero(1, "Iron", "IronDescription", "png"),
            Hero(2, "3-D Man", "3-D ManDescription", "png")
        )
        val countFavorite = listOf(1L, 2L)
        `when`(repository.getListHeroes()).thenReturn(flow { emit(heroes) })
        `when`(repository.getAllIdsHeroesMarvel()).thenReturn(countFavorite)

        // when
        viewModel.initialize()
        advanceUntilIdle()

        // then
        assertEquals(heroes, viewModel.heroes.first())
        assertEquals(countFavorite.size.toLong(), viewModel.page.first().total)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldAllowMoveNextButNotBack() = runTest {
        // given
        val heroes = listOf<Hero>(
            Hero(1, "Iron", "IronDescription", "png"),
            Hero(2, "3-D Man", "3-D ManDescription", "png")
        )
        val countFavorite = (1L..11L).toList()

        `when`(repository.getListHeroes()).thenReturn(flow { emit(heroes) })
        `when`(repository.getAllIdsHeroesMarvel()).thenReturn(countFavorite)

        // when
        viewModel.initialize()
        advanceUntilIdle()

        // then
        assertEquals(heroes, viewModel.heroes.first())
        assertEquals(countFavorite.size.toLong(), viewModel.page.first().total)
        assertFalse(viewModel.page.first().isEnableBackPage())
        assertTrue(viewModel.page.first().isEnableNextPage())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldAllowMoveBackButNotNext() = runTest {
        // given
        val heroes = listOf<Hero>(
            Hero(1, "Iron", "IronDescription", "png"),
            Hero(2, "3-D Man", "3-D ManDescription", "png")
        )
        val countFavorite = (1L..11L).toList()

        `when`(repository.getListHeroes()).thenReturn(flow { emit(heroes) })
        `when`(repository.getAllIdsHeroesMarvel()).thenReturn(countFavorite)

        // when
        viewModel.initialize()
        advanceUntilIdle()
        val newHeroes = listOf<Hero>(
            Hero(3, "A-Bomb", "A-BombDescription", "png")
        )
        `when`(repository.getHeroesByPage(viewModel.page.first().nextPage().toInt())).thenReturn(newHeroes)
        viewModel.getHeroesByPage(viewModel.page.first().nextPage())

        // then
        assertEquals(newHeroes, viewModel.heroes.first())
        assertEquals(countFavorite.size.toLong(), viewModel.page.first().total)
        assertTrue(viewModel.page.first().isEnableBackPage())
        assertFalse(viewModel.page.first().isEnableNextPage())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldDoNotAllowForwardOrBack() = runTest {
        // given
        val heroes = listOf<Hero>(
            Hero(1, "Iron", "IronDescription", "png"),
            Hero(2, "3-D Man", "3-D ManDescription", "png")
        )
        val countFavorite = (1L..10L).toList()

        `when`(repository.getListHeroes()).thenReturn(flow { emit(heroes) })
        `when`(repository.getAllIdsHeroesMarvel()).thenReturn(countFavorite)

        // when
        viewModel.initialize()
        advanceUntilIdle()

        assertFalse(viewModel.page.first().isEnableBackPage())
        assertFalse(viewModel.page.first().isEnableNextPage())
    }


}