package com.cleanarchitecturedemo.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleanarchitecturedemo.domain.common.Result
import com.cleanarchitecturedemo.domain.entities.Volume
import com.cleanarchitecturedemo.domain.usecases.BookmarkBookUseCase
import com.cleanarchitecturedemo.domain.usecases.GetBookmarksUseCase
import com.cleanarchitecturedemo.domain.usecases.GetBooksUseCase
import com.cleanarchitecturedemo.domain.usecases.UnbookmarkBookUseCase
import com.cleanarchitecturedemo.entities.BookWithStatus
import com.cleanarchitecturedemo.mappers.BookWithStatusMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BooksViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val bookmarkBookUseCase: BookmarkBookUseCase,
    private val unbookmarkBookUseCase: UnbookmarkBookUseCase,
    private val mapper: BookWithStatusMapper
) : ViewModel() {

    private val _dataLoading = MutableStateFlow(true)
    val dataLoading = _dataLoading.asSharedFlow()

    private val _books = MutableStateFlow<List<BookWithStatus>>(emptyList())
    val books = _books.asSharedFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    private val _errorLv = MutableLiveData<String>()
    val errorLv: LiveData<String> = _errorLv

    private val _remoteBooks = arrayListOf<Volume>()

    // Getting books with uncle bob as default author :)
    fun getBooks(author: String) {
        viewModelScope.launch {
            _dataLoading.emit(true)
            when (val booksResult = getBooksUseCase.invoke(author)) {
                is Result.Success -> {
                    _remoteBooks.clear()
                    _remoteBooks.addAll(booksResult.data)

                    val bookmarksFlow = getBookmarksUseCase.invoke()
                    bookmarksFlow.collect { bookmarks ->
                        _books.value = mapper.fromVolumeToBookWithStatus(_remoteBooks, bookmarks)
                        _dataLoading.emit(false)
                    }
                }

                is Result.Error -> {
                    _dataLoading.emit(false)
                    _books.value = emptyList()
                    val error = booksResult.exception.message ?: "Api execution Error"
                    _error.emit(error)
                    _errorLv.value = error
                }
            }
        }
    }

    fun bookmark(book: BookWithStatus) {
        viewModelScope.launch {
            bookmarkBookUseCase.invoke(mapper.fromBookWithStatusToVolume(book))
        }
    }

    fun unbookmark(book: BookWithStatus) {
        viewModelScope.launch {
            unbookmarkBookUseCase.invoke(mapper.fromBookWithStatusToVolume(book))
        }
    }
}