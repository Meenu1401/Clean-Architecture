package com.cleanarchitecturedemo.data.books

import com.cleanarchitecturedemo.domain.common.Result
import com.cleanarchitecturedemo.domain.entities.Volume
import com.cleanarchitecturedemo.domain.repositories.BooksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val localDataSource: BooksLocalDataSource,
    private val remoteDataSource: BooksRemoteDataSource
) : BooksRepository {

    override suspend fun getRemoteBooks(author: String): Result<List<Volume>> {
        return remoteDataSource.getBooks(author)
    }

    override suspend fun getBookmarks(): Flow<List<Volume>> {
        return localDataSource.getBookmarks()
    }

    override suspend fun bookmark(book: Volume) {
        localDataSource.bookmark(book)
    }

    override suspend fun unbookmark(book: Volume) {
        localDataSource.unbookmark(book)
    }
}