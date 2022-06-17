package com.cleanarchitecturedemo.domain.usecases

import com.cleanarchitecturedemo.domain.entities.Volume
import com.cleanarchitecturedemo.domain.repositories.BooksRepository
import javax.inject.Inject

class UnbookmarkBookUseCase  @Inject constructor(private val booksRepository: BooksRepository) {
    suspend operator fun invoke(book: Volume) = booksRepository.unbookmark(book)
}