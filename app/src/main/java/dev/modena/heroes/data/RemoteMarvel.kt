package dev.modena.heroes.data

import dev.modena.marvel.service.MarvelService
import retrofit2.Retrofit
import javax.inject.Inject

class RemoteMarvel @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun getInitialCharacters() = retrofit.create(MarvelService::class.java).getCharacters()


}