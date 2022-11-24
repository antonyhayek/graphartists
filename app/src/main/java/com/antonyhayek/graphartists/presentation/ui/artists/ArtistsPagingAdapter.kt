package com.antonyhayek.graphartists.presentation.ui.artists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.antonyhayek.graphartists.data.model.Artist
import com.antonyhayek.graphartists.databinding.ItemArtistBinding
import java.util.concurrent.RecursiveAction

class ArtistsPagingAdapter(
    var onArtistClicked : (String) -> Unit
) : PagingDataAdapter<Artist, ArtistsPagingAdapter.ArtistViewHolder>(ArtistComparator) {

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = getItem(position)
        artist?.let { artist ->
            holder.bind(artist)
            holder.itemView.setOnClickListener {
                onArtistClicked(artist.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArtistBinding.inflate(inflater, parent, false)
        return ArtistViewHolder(binding)
    }



    class ArtistViewHolder(val binding: ItemArtistBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: Artist) {

            binding.artist = artist
            with(binding) {
                if(artist.disambiguation == null)
                    tvArtistDisambiguation.isVisible = false
                else {
                    tvArtistDisambiguation.text = artist.disambiguation
                    tvArtistDisambiguation.isVisible = true
                }

                tvArtistCountry.isVisible = artist.country != null
            }

        }

    }

    object ArtistComparator : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem == newItem
        }

    }
}