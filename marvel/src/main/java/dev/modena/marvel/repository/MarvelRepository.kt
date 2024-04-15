package dev.modena.marvel.repository

import dev.modena.marvel.model.ResponseMarvel
import kotlinx.coroutines.flow.Flow


interface MarvelRepository {

    suspend fun getCharactersMarvel(): Flow<Result<ResponseMarvel>>

    suspend fun getCharactersMarvelByName(query: String): Flow<Result<ResponseMarvel>>

    suspend fun navigatePage(offset: Long, query: String): Flow<Result<ResponseMarvel>>

}