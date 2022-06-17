package com.cleanarchitecturedemo.data.mappers

import com.cleanarchitecturedemo.data.entities.BookEntity
import com.cleanarchitecturedemo.domain.entities.Volume
import com.cleanarchitecturedemo.domain.entities.VolumeInfo
import javax.inject.Inject

class BookEntityMapper @Inject constructor() {
    fun toBookEntity(volume: Volume): BookEntity {
        return BookEntity(
            id = volume.id,
            title = volume.volumeInfo.title,
            authors = volume.volumeInfo.authors,
            imageUrl = volume.volumeInfo.imageUrl
        )
    }

    fun toVolume(bookEntity: BookEntity): Volume {
        return Volume(
            bookEntity.id,
            VolumeInfo(bookEntity.title, bookEntity.authors, bookEntity.imageUrl)
        )
    }
}