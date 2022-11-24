package com.antonyhayek.graphartists.presentation.ui.artists

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.antonyhayek.graphartists.R
import com.antonyhayek.graphartists.data.model.Artist
import com.antonyhayek.graphartists.databinding.FragmentArtistsBinding
import com.antonyhayek.graphartists.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtistsFragment : BaseFragment<FragmentArtistsBinding>(FragmentArtistsBinding::inflate) {
    private val viewModel: ArtistsViewModel by viewModels()
    private lateinit var artistsPagingAdapter: ArtistsPagingAdapter
    private lateinit var artistSearchPagingAdapter: ConcatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /*  viewLifecycleOwner.lifecycleScope.launchWhenResumed {
              viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                  viewModel.artistsState.collect{ uiState ->

                      when(uiState) {
                          is ArtistsViewModel.UIEventArtists.OnArtistListRetrieved -> {

                              *//*Toast.makeText(requireContext(), uiState.artistList.search?.artists?.nodes?.get(0)?.artistBasicFragment?.id, Toast.LENGTH_SHORT).show()
                            if(findNavController().currentDestination?.id == R.id.artistsFragment)
                                findNavController().navigate(ArtistsFragmentDirections.actionArtistsFragmentToArtistDetailsFragment(
                                    uiState.artistList.search?.artists?.nodes?.get(0)?.artistBasicFragment?.id ?: ""
                                ))*//*
                        }
                        is ArtistsViewModel.UIEventArtists.OnError -> {

                        }
                        is ArtistsViewModel.UIEventArtists.OnLoading -> {

                        }
                        is ArtistsViewModel.UIEventArtists.ShowErrorToast -> {
                            Toast.makeText(requireContext(), uiState.resourceFailure.exception.message, Toast.LENGTH_SHORT).show()

                        }
                    }

                }
            }
        }*/

        setLayoutListeners()
        setupRecyclerView()
        fetchArtists()
    }

    private fun fetchArtists() {

        viewModel.viewModelScope.launch {
            binding.pbFetchArtists.isVisible = true

            viewModel.getArtistList("John Mayer").collectLatest {
                binding.pbFetchArtists.isVisible = false
                artistsPagingAdapter.submitData(it)
            }
        }
    }

    private fun setupRecyclerView() {

        artistsPagingAdapter = ArtistsPagingAdapter {
            if (findNavController().currentDestination?.id == R.id.artistsFragment)
                findNavController().navigate(
                    ArtistsFragmentDirections.actionArtistsFragmentToArtistDetailsFragment(
                        it
                    )
                )
        }
        binding.rvArtists.adapter = artistsPagingAdapter

        artistSearchPagingAdapter = artistsPagingAdapter.withLoadStateFooter(
            footer = ArtistsPagingLoadingStateAdapter { artistsPagingAdapter.retry() })
    }

    private fun setLayoutListeners() {

        with(binding) {
            ibSearch.setOnClickListener {
                val query = etSearch.text.toString().trim()
                if (query.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please search for an artist",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                etSearch.clearFocus()
                viewModel.viewModelScope.launch {
                    pbFetchArtists.isVisible = true
                    viewModel.getArtistList(query).collectLatest {
                        pbFetchArtists.isVisible = false
                        artistsPagingAdapter.submitData(it)
                    }
                }

            }
        }
    }

}