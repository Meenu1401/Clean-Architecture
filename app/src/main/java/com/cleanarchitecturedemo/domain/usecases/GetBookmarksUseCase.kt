package com.cleanarchitecturedemo.domain.usecases

import com.cleanarchitecturedemo.domain.repositories.BooksRepository
import javax.inject.Inject

class GetBookmarksUseCase @Inject constructor(private val booksRepository: BooksRepository) {
    suspend operator fun invoke() = booksRepository.getBookmarks()
}