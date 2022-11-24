package com.antonyhayek.graphartists.domain.use_case

import com.antonyhayek.ArtistQuery
import com.antonyhayek.graphartists.data.networking.Resource
import com.antonyhayek.graphartists.domain.repository.ArtistRepository
import javax.inject.Inject

class GetArtistDetailsUseCase @Inject constructor(
    private val artistRepository: ArtistRepository
){

    suspend operator fun invoke(id: String) : Resource<ArtistQuery.Data> =
        artistRepository.getArtist(id = id)
}