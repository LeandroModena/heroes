package dev.modena.heroes.home.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.modena.heroes.extensions.getOrAwaitValue
import dev.modena.heroes.home.HomeRoute
import dev.modena.heroes.home.HomeViewModel
import dev.modena.heroes.repository.HeroRepository
import junit.framework.TestCase
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
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailHeroViewModelTest {

    @Mock
    private lateinit var repository: HeroRepository
    private lateinit var viewModel: DetailHeroViewModel
    private val testDispatcher = StandardTestDispatcher()
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = DetailHeroViewModel(repository)
    }

    @Test
    fun shouldLaunchShareImageEvent() = runTest {
        // when
        viewModel.onClickShareHero()

        // then
        assertEquals(Share.Image, viewModel.share.getOrAwaitValue())
    }

}