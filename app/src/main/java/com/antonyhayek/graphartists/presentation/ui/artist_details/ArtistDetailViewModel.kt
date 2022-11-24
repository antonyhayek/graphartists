package com.antonyhayek.graphartists.presentation.ui.artist_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antonyhayek.ArtistQuery
import com.antonyhayek.ArtistsQuery
import com.antonyhayek.graphartists.data.networking.Resource
import com.antonyhayek.graphartists.domain.use_case.GetArtistDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val getArtistDetailsUseCase: GetArtistDetailsUseCase
) : ViewModel() {

    private val _artistDetailsState = MutableStateFlow<UIEventArtistDetails>(
        UIEventArtistDetails.OnLoading(false)
    )

    var artistDetailsState = _artistDetailsState.asStateFlow()

    fun getArtistDetails(id: String) {
        _artistDetailsState.value = UIEventArtistDetails.OnLoading(true)

        viewModelScope.launch {
            when (val result = getArtistDetailsUseCase.invoke(id = id)) {
                is Resource.Failure -> {
                    _artistDetailsState.value = UIEventArtistDetails.OnLoading(false)
                    _artistDetailsState.value = UIEventArtistDetails.ShowErrorToast(result)
                }
                is Resource.Loading -> UIEventArtistDetails.OnLoading(false)
                is Resource.Success -> {
                    _artistDetailsState.value = UIEventArtistDetails.OnLoading(false)
                    _artistDetailsState.value = UIEventArtistDetails.OnArtistDetailsRetrieved(result.value)
                }
            }
        }
    }


    sealed class UIEventArtistDetails {
        class OnLoading(var onLoading: Boolean) : UIEventArtistDetails()
        class OnArtistDetailsRetrieved(var artistDetails: ArtistQuery.Data) : UIEventArtistDetails()
        class OnError(val resourceFailure: Resource.Failure) : UIEventArtistDetails()
        data class ShowErrorToast(val resourceFailure: Resource.Failure) : UIEventArtistDetails()
    }
}