package com.antonyhayek.graphartists.presentation.ui.artists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.antonyhayek.graphartists.databinding.ItemLoadStateBinding


class ArtistsPagingLoadingStateAdapter(
    private val retry: () -> Unit,
) : LoadStateAdapter<ArtistsPagingLoadingStateAdapter.ListLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: ListLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ) = ListLoadStateViewHolder.create(parent, retry)

    class ListLoadStateViewHolder(
        private val binding: ItemLoadStateBinding,
        retry: () -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.tvErrorMessage.text = loadState.error.localizedMessage
            }

            binding.pbFetchLoading.isVisible = loadState is LoadState.Loading
            binding.btnRetry.isVisible = loadState is LoadState.Error
            binding.tvErrorMessage.isVisible = loadState is LoadState.Error

//            binding.root.isVisible = loadState is LoadState.Loading || loadState is LoadState.Error
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): ListLoadStateViewHolder {
                val binding = ItemLoadStateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )

                return ListLoadStateViewHolder(binding, retry)
            }
        }
    }
}