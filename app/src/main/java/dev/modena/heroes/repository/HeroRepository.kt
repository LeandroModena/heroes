package dev.modena.heroes.repository

import dev.modena.heroes.data.local.dao.HeroDao
import dev.modena.heroes.data.local.entity.Hero
import dev.modena.heroes.data.remote.RemoteMarvel
import dev.modena.heroes.shared.arch.BaseRepository
import dev.modena.marvel.model.ResponseMarvel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HeroRepository @Inject constructor(
    private val remoteMarvel: RemoteMarvel,
    private val heroDao: HeroDao
) : BaseRepository() {

    suspend fun getCharactersMarvel(): Flow<Result<ResponseMarvel>> {
        return requestByFlow { remoteMarvel.getInitialCharacters() }
    }

    suspend fun getCharactersMarvelByName(query: String): Flow<Result<ResponseMarvel>> {
        return requestByFlow { remoteMarvel.getCharactersByName(query) }
    }

    suspend fun navigatePage(offset: Long, query: String): Flow<Result<ResponseMarvel>> {
        return requestByFlow { remoteMarvel.navigatePages(offset, query) }
    }

    suspend fun getAllIdsHeroesMarvel(): List<Long> {
        return heroDao.getAllHeroIds()
    }

    suspend fun saveOrDeleteHeroesMarvel(isFavorite: Boolean, hero: Hero) {
        if (isFavorite) heroDao.delete(hero) else heroDao.insert(hero)
    }

}