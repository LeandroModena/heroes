package dev.modena.heroes.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.modena.heroes.repository.HeroRepository
import dev.modena.heroes.shared.Hero
import dev.modena.heroes.shared.NetworkConnection
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchHeroViewModel @Inject constructor(
    private val network: NetworkConnection,
    private val repository: HeroRepository
) : ViewModel() {

    private val _hasInternet = MutableLiveData<Boolean>()
    val hasInternet = _hasInternet.asFlow()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asFlow()
    private val _heroes = MutableLiveData<List<Hero>>()
    val heroes = _heroes.asFlow()

    fun initialize() {
        checkConnection()
        initialRequest()
    }

    fun checkConnection() {
       _hasInternet.value = network.isInternetAvailable()
    }

    fun initialRequest() {
        viewModelScope.launch {
            repository.getCharactersMarvel()
                .onStart { showLoading() }
                .onCompletion { hideLoading() }
                .collect { result ->
                if (result.isSuccess) {
                    val marvelData = result.getOrNull()
                    marvelData?.let {
                        _heroes.value = Hero.createByMarvel(it)
                    }
                }
            }
        }

    }

    private fun showLoading() {
        _isLoading.value = true
    }

    private fun hideLoading() {
        _isLoading.value = false
    }

}