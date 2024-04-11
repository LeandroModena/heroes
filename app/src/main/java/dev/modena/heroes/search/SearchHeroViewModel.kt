package dev.modena.heroes.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.modena.heroes.repository.HeroRepository
import dev.modena.heroes.shared.NetworkConnection
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun initialize() {
        checkConnection()
        initialRequest()
    }

    fun checkConnection() {
       _hasInternet.value = network.isInternetAvailable()
    }

    fun initialRequest() {
        viewModelScope.launch {
            repository.getCharactersMarvel().collect {
                Log.d("RequestTest", it.getOrNull()?.toString() ?: "isNull")
            }
        }

    }

}