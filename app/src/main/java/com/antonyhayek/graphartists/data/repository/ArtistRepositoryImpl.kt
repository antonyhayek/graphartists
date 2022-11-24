package com.antonyhayek.graphartists.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.antonyhayek.ArtistQuery
import com.antonyhayek.ArtistsQuery
import com.antonyhayek.graphartists.data.model.Artist
import com.antonyhayek.graphartists.data.networking.Resource
import com.antonyhayek.graphartists.data.paging.ArtistPagingSource
import com.antonyhayek.graphartists.domain.repository.ArtistRepository
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val apolloService: ApolloClient
) : ArtistRepository {

    override suspend fun getArtistList(query: String): Flow<PagingData<Artist>> {

        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = 15,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                ArtistPagingSource(query, apolloService)
            }
        ).flow

    /* lateinit var result: Resource<ArtistsQuery.Data>
        try {
            val response = apolloService.query(ArtistsQuery(query, Optional.presentIfNotNull(lastArtistId))).execute()
            response.let {
                it.data?.let { data -> result = Resource.Success(data)
                }
            }
        } catch (apolloException :ApolloException) {
            Log.e("ArtistRepositoryImpl", "Error: ${apolloException.message}")

            return Resource.Failure(
                false,
                400,
                Exception("Something went wrong")
            )
        }
        return result*/
    }

    override suspend fun getArtist(id: String): Resource<ArtistQuery.Data> {
        lateinit var result: Resource<ArtistQuery.Data>
        try {
            val response = apolloService.query(ArtistQuery(artistId = id)).execute()
            response.let {
                it.data?.let { data -> result = Resource.Success(data)
                }
            }
        } catch (apolloException :ApolloException) {
//            Log.e("ArtistRepositoryImpl", "Error: ${apolloException.message}")

            return Resource.Failure(
                false,
                400,
                Exception("Something went wrong")
            )
        }
        return result
    }


}