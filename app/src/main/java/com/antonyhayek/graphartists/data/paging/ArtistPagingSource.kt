package com.antonyhayek.graphartists.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.antonyhayek.ArtistsQuery
import com.antonyhayek.graphartists.data.model.Artist

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional

class ArtistPagingSource(
    var query: String,
    var client: ApolloClient
) : PagingSource<String, Artist>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Artist> {
        return try {
            val response = client.query(
                ArtistsQuery(
                    query = query,
                    firstCount = /*params.loadSize*/15,
                    lastArtistId = Optional.presentIfNotNull(params.key)
                )
            ).execute()

            LoadResult.Page(
                data = mapNodeToArtist(response.data?.search?.artists?.nodes!!),
                prevKey = if (response.data?.search?.artists?.pageInfo?.hasPreviousPage == true)
                    response.data?.search?.artists?.pageInfo?.startCursor
                else null,
                nextKey = if (response.data?.search?.artists?.pageInfo?.hasNextPage == true)
                    response.data?.search?.artists?.pageInfo?.endCursor
                else null
            )


        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Artist>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPageIndex = state.pages.indexOf(state.closestPageToPosition(anchorPosition))
            state.pages.getOrNull(anchorPageIndex + 1)?.prevKey ?: state.pages.getOrNull(anchorPageIndex - 1)?.nextKey
        }
    }

    private fun mapNodeToArtist(nodes: List<ArtistsQuery.Node?>?): ArrayList<Artist> {

        if (nodes != null) {
            val artistList: ArrayList<Artist> = arrayListOf()
            for (node in nodes) {
                artistList.add(
                    Artist(
                        id = node!!.artistBasicFragment.id,
                        name = node.artistBasicFragment.name,
                        disambiguation = node.artistBasicFragment.disambiguation,
                        country = node.artistBasicFragment.country
                    )
                )
            }
            return artistList
        } else {
            return arrayListOf()
        }
    }
}

