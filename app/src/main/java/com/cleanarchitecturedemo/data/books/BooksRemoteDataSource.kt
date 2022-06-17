package com.cleanarchitecturedemo.data.books

import com.cleanarchitecturedemo.domain.common.Result
import com.cleanarchitecturedemo.domain.entities.Volume

interface BooksRemoteDataSource {
    suspend fun getBooks(author: String): Result<List<Volume>>
}