package dev.modena.heroes.data.remote

import dev.modena.marvel.service.MarvelService
import retrofit2.Retrofit
import javax.inject.Inject

class RemoteMarvel @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun getInitialCharacters() = retrofit.create(MarvelService::class.java).getCharacters()

    suspend fun getCharactersByName(query: String) =
        retrofit.create(MarvelService::class.java).getCharacters(nameStartsWith = query)
}