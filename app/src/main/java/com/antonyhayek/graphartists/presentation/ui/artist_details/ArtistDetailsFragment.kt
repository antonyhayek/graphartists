package com.antonyhayek.graphartists.presentation.ui.artist_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.antonyhayek.graphartists.R
import com.antonyhayek.graphartists.databinding.FragmentArtistDetailsBinding
import com.antonyhayek.graphartists.presentation.base.BaseFragment
import com.antonyhayek.graphartists.presentation.ui.artists.ArtistsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistDetailsFragment : BaseFragment<FragmentArtistDetailsBinding>
    (FragmentArtistDetailsBinding::inflate) {
    private val viewModel: ArtistDetailViewModel by viewModels()
    private val args: ArtistDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getArtistDetails(args.id)

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.artistDetailsState.collect{ uiState ->

                    when(uiState) {
                        is ArtistDetailViewModel.UIEventArtistDetails.OnArtistDetailsRetrieved -> {

                            with(binding) {
                                artist = uiState.artistDetails
                                tvArtistName.isVisible = true
                                tvArtistCountry.isVisible = true
                                tvArtistDisambiguation.isVisible = true
                                tvArtistId.isVisible = true
                            }
                        }
                        is ArtistDetailViewModel.UIEventArtistDetails.OnError -> {
                            binding.tvArtistDetailsNotFound.isVisible = true

                        }
                        is ArtistDetailViewModel.UIEventArtistDetails.OnLoading -> {
                            binding.pbDetailsFetchProgress.isVisible = uiState.onLoading
                        }
                        is ArtistDetailViewModel.UIEventArtistDetails.ShowErrorToast -> {
                            Toast.makeText(requireContext(), uiState.resourceFailure.exception.message, Toast.LENGTH_SHORT).show()

                        }
                    }

                }
            }
        }
    }


}