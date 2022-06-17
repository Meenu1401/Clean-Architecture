package com.cleanarchitecturedemo.data.mappers

import com.cleanarchitecturedemo.data.api.BooksApiResponse
import com.cleanarchitecturedemo.domain.entities.Volume
import com.cleanarchitecturedemo.domain.entities.VolumeInfo
import javax.inject.Inject

class BookApiResponseMapper @Inject constructor(){
    fun toVolumeList(response: BooksApiResponse): List<Volume> {
        return response.items.map {
            Volume(
                it.id, VolumeInfo(
                    it.volumeInfo.title,
                    it.volumeInfo.authors,
                    it.volumeInfo.imageLinks?.thumbnail?.replace("http", "https")
                )
            )
        }
    }
}