package dev.modena.heroes.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _route = MutableLiveData<HomeRoute>()
    val route: LiveData<HomeRoute> = _route

    fun onClickSearch() {
        _route.value = HomeRoute.Search
    }

    fun onClickFavorite() {
        _route.value = HomeRoute.Favorite
    }

}

sealed class HomeRoute {
    data object Search : HomeRoute()
    data object Favorite : HomeRoute()
}