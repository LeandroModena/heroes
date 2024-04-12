package dev.modena.heroes.shared.arch

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

abstract class BaseRepository {
    suspend fun <T> requestByFlow(call: suspend () -> Response<T>): Flow<Result<T>> {
        return flow {
            try {
                val response = call()
                if (response.isSuccessful) {
                    val body = response.body()!!
                    emit(Result.success(body))
                }  else {
                    emit(Result.failure(Exception("Error request code: ${response.code()}")))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

}