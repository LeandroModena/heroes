package dev.modena.marvel.service

import dev.modena.marvel.model.ResponseMarvel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelService {

    @GET("characters")
    suspend fun getCharacters(
        @Query("nameStartsWith") nameStartsWith: String? = null,
        @Query("limit") limit: Long = 10L,
        @Query("offset") offset: Long? = null
    ): Response<ResponseMarvel>

}