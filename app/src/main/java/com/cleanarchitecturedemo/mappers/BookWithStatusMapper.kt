package com.cleanarchitecturedemo.mappers

import com.cleanarchitecturedemo.entities.BookWithStatus
import com.cleanarchitecturedemo.entities.BookmarkStatus
import com.cleanarchitecturedemo.domain.entities.Volume
import com.cleanarchitecturedemo.domain.entities.VolumeInfo
import javax.inject.Inject

class BookWithStatusMapper  @Inject constructor (){
    fun fromVolumeToBookWithStatus(
        volumes: List<Volume>,
        bookmarks: List<Volume>
    ): List<BookWithStatus> {
        val commonElements = volumes.filter { it.id in bookmarks.map { bookmark -> bookmark.id } }
        val booksWithStatus = arrayListOf<BookWithStatus>()
        for (volume in volumes) {
            if (volume in commonElements) {
                booksWithStatus.add(
                    BookWithStatus(
                        volume.id,
                        volume.volumeInfo.title,
                        volume.volumeInfo.authors,
                        volume.volumeInfo.imageUrl,
                        BookmarkStatus.BOOKMARKED
                    )
                )
            } else {
                booksWithStatus.add(
                    BookWithStatus(
                        volume.id,
                        volume.volumeInfo.title,
                        volume.volumeInfo.authors,
                        volume.volumeInfo.imageUrl,
                        BookmarkStatus.UNBOOKMARKED
                    )
                )
            }
        }
        return booksWithStatus.sortedBy { it.id }
    }

    fun fromBookWithStatusToVolume(bookWithStatus: BookWithStatus): Volume {
        return Volume(
            bookWithStatus.id,
            VolumeInfo(bookWithStatus.title, bookWithStatus.authors, bookWithStatus.imageUrl)
        )
    }
}