package com.cleanarchitecturedemo.domain.usecases

import com.cleanarchitecturedemo.domain.entities.Volume
import com.cleanarchitecturedemo.domain.repositories.BooksRepository
import javax.inject.Inject

class BookmarkBookUseCase @Inject constructor(val booksRepository: BooksRepository) {
    suspend operator fun invoke(book: Volume) = booksRepository.bookmark(book)
}