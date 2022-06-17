package com.cleanarchitecturedemo.data.books

import com.cleanarchitecturedemo.domain.entities.Volume
import kotlinx.coroutines.flow.Flow

interface BooksLocalDataSource {
    suspend fun bookmark(book: Volume)
    suspend fun unbookmark(book: Volume)
    suspend fun getBookmarks(): Flow<List<Volume>>
}