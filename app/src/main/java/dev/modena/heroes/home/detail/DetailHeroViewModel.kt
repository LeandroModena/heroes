package dev.modena.heroes.home.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.modena.heroes.data.local.entity.Hero
import dev.modena.heroes.repository.HeroRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailHeroViewModel @Inject constructor(
    private val repository: HeroRepository
): ViewModel() {

    private val _share = MutableLiveData<Share>()
    val share: LiveData<Share> = _share

    fun onClickShareHero() {
        _share.value = Share.Image
    }

    fun saveOrDeleteFavoriteHero(isFavorite: Boolean, hero: Hero) {
        viewModelScope.launch {
            repository.saveOrDeleteHeroesMarvel(isFavorite, hero)
        }
    }

}

sealed class Share {
    data object Image : Share()

}