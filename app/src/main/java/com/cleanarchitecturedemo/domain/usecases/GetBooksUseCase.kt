package com.cleanarchitecturedemo.domain.usecases

import com.cleanarchitecturedemo.domain.repositories.BooksRepository
import javax.inject.Inject

class GetBooksUseCase  @Inject constructor(private val booksRepository: BooksRepository) {
    suspend operator fun invoke(author: String) = booksRepository.getRemoteBooks(author)
}