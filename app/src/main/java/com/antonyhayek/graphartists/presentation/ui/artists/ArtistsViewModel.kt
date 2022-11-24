package com.antonyhayek.graphartists.presentation.ui.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.antonyhayek.ArtistsQuery
import com.antonyhayek.graphartists.data.model.Artist
import com.antonyhayek.graphartists.data.networking.Resource
import com.antonyhayek.graphartists.domain.use_case.GetArtistListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val getArtistListUseCase: GetArtistListUseCase
) : ViewModel() {
    private val _artistsState = MutableStateFlow<UIEventArtists>(
        UIEventArtists.OnLoading(false)
    )

    var artistsState = _artistsState.asStateFlow()


   /* init {
        viewModelScope.launch {
            getArtistList("John Mayer")
        }
    }*/

    suspend fun getArtistList(query: String): Flow<PagingData<Artist>> {
       // _artistsState.value = UIEventArtists.OnLoading(true)

        return getArtistListUseCase(query).cachedIn(viewModelScope)

        /*collect{
            when (it) {
                is Resource.Failure -> {
                    _artistsState.value = UIEventArtists.OnLoading(false)
                    _artistsState.value = UIEventArtists.ShowErrorToast(result)
                }
                is Resource.Loading -> UIEventArtists.OnLoading(false)
                is Resource.Success -> {
                    _artistsState.value = UIEventArtists.OnLoading(false)
                    _artistsState.value = UIEventArtists.OnArtistListRetrieved(result.value)
                }
            }
        }*/
    }


    sealed class UIEventArtists {
        class OnLoading(var onLoading: Boolean) : UIEventArtists()
        class OnArtistListRetrieved(var artistList: ArtistsQuery.Data) : UIEventArtists()
        class OnError(val resourceFailure: Resource.Failure) : UIEventArtists()
        data class ShowErrorToast(val resourceFailure: Resource.Failure) : UIEventArtists()
    }
}
