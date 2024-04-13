package dev.modena.heroes.home.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.modena.heroes.repository.HeroRepository
import dev.modena.heroes.data.local.entity.Hero
import dev.modena.heroes.shared.NetworkConnection
import dev.modena.heroes.shared.model.Page
import dev.modena.marvel.model.ResponseMarvel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SearchHeroViewModel @Inject constructor(
    private val network: NetworkConnection,
    private val repository: HeroRepository
) : ViewModel() {

    private val _query = MutableLiveData("")
    private val _hasInternet = MutableLiveData<Boolean>()
    val hasInternet = _hasInternet.asFlow()
    private val _hasErrors = MutableLiveData<Boolean>()
    val hasErrors = _hasErrors.asFlow()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asFlow()
    private val _heroes = MutableLiveData<List<Hero>>()
    val heroes = _heroes.asFlow()
    private val _page = MutableLiveData<Page>()
    val page = _page.asFlow()

    fun initialize() {
        _hasInternet.value = hasConnection()
        if (hasConnection()) initialRequest()
    }

    private fun hasConnection(): Boolean {
         return network.isInternetAvailable()
    }

    private fun initialRequest() {
        viewModelScope.launch {
            repository.getCharactersMarvel()
                .onStart { showLoading() }
                .onCompletion { hideLoading() }
                .collect { result ->
                    updateScreen(result)
                }
        }
    }

    fun searchByName(query: String) {
        _query.value = query
        if (query.isBlank()) {
            initialRequest()
            return
        }
        viewModelScope.launch {
            repository.getCharactersMarvelByName(query)
                .onStart { showLoading() }
                .onCompletion { hideLoading() }
                .collect { result ->
                    updateScreen(result)
                }
        }
    }

    private suspend fun updateScreen(result: Result<ResponseMarvel>) {
        if (result.isSuccess) updateScreenSuccess(result) else updateScreenError(result)
    }

    private suspend fun updateScreenSuccess(result: Result<ResponseMarvel>) {
        val marvelData = result.getOrNull()
        marvelData?.let {
            _page.value = Page.createByMarvel(it)
            _heroes.value = Hero.createByMarvel(it, repository.getAllIdsHeroesMarvel())
        }
    }

    private fun updateScreenError(result: Result<ResponseMarvel>) {
        // Location to handle exception or http request error
        when(result.exceptionOrNull()) {
            is UnknownHostException -> { _hasInternet.value = false }
            //Example for time out handling: is SocketTimeoutException -> { }
            else -> { _hasErrors.value = true }
        }
    }


    fun navigationPage(offset: Long) {
        viewModelScope.launch {
            repository.navigatePage(offset, _query.value!!)
                .onStart { showLoading() }
                .onCompletion { hideLoading() }
                .collect { result ->
                    updateScreen(result)
            }
        }
    }


    fun tryAgain() {
        _hasInternet.value = hasConnection()
        _hasErrors.value = false
        if (hasConnection()) {
            _page.value?.let {
                navigationPage(_page.value?.offset ?: 0)
            } ?: run {
                searchByName(_query.value ?: "")
            }
        }
    }

    fun saveOrDeleteFavoriteHero(isFavorite: Boolean, hero: Hero) {
        viewModelScope.launch {
            repository.saveOrDeleteHeroesMarvel(isFavorite, hero)
        }
    }

    private fun showLoading() {
        _isLoading.value = true
    }

    private fun hideLoading() {
        _isLoading.value = false
    }

}