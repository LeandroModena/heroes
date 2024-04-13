package dev.modena.heroes.home.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.modena.heroes.data.local.entity.Hero
import dev.modena.heroes.repository.HeroRepository
import dev.modena.heroes.shared.model.Page
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteHeroViewModel @Inject constructor(
    private val repository: HeroRepository
) : ViewModel() {

    private val _heroes = MutableLiveData<List<Hero>>()
    val heroes = _heroes.asFlow()
    private val _page = MutableLiveData<Page>()
    val page = _page.asFlow()

    fun initialize() {
        viewModelScope.launch {
            repository.getListHeroes().collect {
                _heroes.value = it
                _page.value = Page(
                    offset = 0,
                    limit = 10,
                    count = it.size.toLong(),
                    total = repository.getAllIdsHeroesMarvel().size.toLong()
                )
            }
        }
    }

    fun getHeroesByPage(offset: Long) {
        viewModelScope.launch {
            val heroes = repository.getHeroesByPage(offset.toInt())
            if (heroes.isNotEmpty()) {
                _heroes.value = heroes
                _page.value = Page(
                    offset = offset,
                    limit = 10,
                    count = heroes.size.toLong(),
                    total = repository.getAllIdsHeroesMarvel().size.toLong()
                )
            } else if (_page.value!!.isEnableBackPage()) {
                getHeroesByPage(_page.value!!.backPage())
            } else {
                _heroes.value = heroes
            }
        }
    }

    fun deleteFavoriteHero(hero: Hero) {
        viewModelScope.launch {
            repository.saveOrDeleteHeroesMarvel(true, hero)
            getHeroesByPage(_page.value?.offset ?: 0)
        }
    }

}