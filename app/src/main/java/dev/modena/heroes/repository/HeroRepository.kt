package dev.modena.heroes.repository

import dev.modena.heroes.data.local.dao.HeroDao
import dev.modena.heroes.data.local.entity.Hero
import dev.modena.heroes.data.remote.RemoteMarvel
import dev.modena.heroes.shared.arch.BaseRepository
import dev.modena.marvel.model.ResponseMarvel
import dev.modena.marvel.repository.MarvelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HeroRepository @Inject constructor(
    private val remoteMarvel: RemoteMarvel,
    private val heroDao: HeroDao
) : BaseRepository(), MarvelRepository {

    override suspend fun getCharactersMarvel(): Flow<Result<ResponseMarvel>> {
        return requestByFlow { remoteMarvel.getInitialCharacters() }
    }

    override suspend fun getCharactersMarvelByName(query: String): Flow<Result<ResponseMarvel>> {
        return requestByFlow { remoteMarvel.getCharactersByName(query) }
    }

    override suspend fun navigatePage(offset: Long, query: String): Flow<Result<ResponseMarvel>> {
        return requestByFlow { remoteMarvel.navigatePages(offset, query) }
    }

    suspend fun getAllIdsHeroesMarvel(): List<Long> {
        return heroDao.getAllHeroIds()
    }

    suspend fun saveOrDeleteHeroesMarvel(isFavorite: Boolean, hero: Hero) {
        if (isFavorite) heroDao.delete(hero) else heroDao.insert(hero)
    }

    suspend fun getListHeroes() = flow {
        emit(heroDao.getHeroesSortedByName())
    }.flowOn(Dispatchers.IO)

    suspend fun getHeroesByPage(offset: Int): List<Hero> {
        return heroDao.getHeroesByPage(offset)
    }

}