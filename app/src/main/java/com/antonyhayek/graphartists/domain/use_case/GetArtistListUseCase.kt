package com.antonyhayek.graphartists.domain.use_case

import androidx.paging.PagingData
import com.antonyhayek.ArtistQuery
import com.antonyhayek.ArtistsQuery
import com.antonyhayek.graphartists.data.model.Artist
import com.antonyhayek.graphartists.data.networking.Resource
import com.antonyhayek.graphartists.domain.repository.ArtistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArtistListUseCase @Inject constructor(
    private val artistRepository: ArtistRepository
){

    suspend operator fun invoke(query: String) : Flow<PagingData<Artist>> =
        artistRepository.getArtistList(query)
}