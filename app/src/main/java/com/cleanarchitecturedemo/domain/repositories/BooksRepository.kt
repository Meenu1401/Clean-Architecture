package com.cleanarchitecturedemo.domain.repositories

import com.cleanarchitecturedemo.domain.common.Result
import com.cleanarchitecturedemo.domain.entities.Volume
import kotlinx.coroutines.flow.Flow

interface BooksRepository {

    suspend fun getRemoteBooks(author: String): Result<List<Volume>>

    suspend fun getBookmarks(): Flow<List<Volume>>

    suspend fun bookmark(book: Volume)

    suspend fun unbookmark(book: Volume)
}