package com.cleanarchitecturedemo.data.books

import com.cleanarchitecturedemo.data.api.BooksApi
import com.cleanarchitecturedemo.data.mappers.BookApiResponseMapper
import com.cleanarchitecturedemo.domain.common.Result
import com.cleanarchitecturedemo.domain.entities.Volume
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BooksRemoteDataSourceImpl @Inject constructor (
    private val service: BooksApi,
    private val mapper: BookApiResponseMapper
) : BooksRemoteDataSource {
    override suspend fun getBooks(author: String): Result<List<Volume>> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getBooks(author)
                if (response.isSuccessful) {
                    Result.Success(mapper.toVolumeList(response.body()!!))
                } else {
                    Result.Error(Exception(response.message()))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}