package dev.modena.heroes.repository

import dev.modena.heroes.data.RemoteMarvel
import dev.modena.heroes.shared.arch.BaseRepository
import dev.modena.marvel.model.ResponseMarvel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HeroRepository @Inject constructor(
    private val remoteMarvel: RemoteMarvel
) : BaseRepository() {

    suspend fun getCharactersMarvel(): Flow<Result<ResponseMarvel>> {
        return requestByFlow { remoteMarvel.getInitialCharacters() }
    }

}