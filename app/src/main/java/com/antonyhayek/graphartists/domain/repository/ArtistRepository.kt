package com.antonyhayek.graphartists.domain.repository

import androidx.paging.PagingData
import com.antonyhayek.ArtistQuery
import com.antonyhayek.ArtistsQuery
import com.antonyhayek.graphartists.data.model.Artist
import com.antonyhayek.graphartists.data.networking.Resource
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    suspend fun getArtistList(query: String) : Flow<PagingData<Artist>>
    suspend fun getArtist(id: String) : Resource<ArtistQuery.Data>

}