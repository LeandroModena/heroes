package dev.modena.heroes.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.modena.heroes.extensions.getOrAwaitValue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel()
    }

    @Test
    fun shouldLaunchFavoriteRouteEvent() = runTest {
        // when
        viewModel.onClickFavorite()

        // then
        assertEquals(HomeRoute.Favorite, viewModel.route.getOrAwaitValue())
    }

    @Test
    fun shouldLaunchSearchRouteEvent() = runTest {
        // when
        viewModel.onClickSearch()

        // then
        assertEquals(HomeRoute.Search, viewModel.route.getOrAwaitValue())
    }

}