package com.lambao.mrbeast.presentation.ui.fragment.common

import com.lambao.base.presentation.ui.recycler_view.BaseRecyclerAdapter
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.ItemSongInfoBinding

class SongInfoAdapter(onItemClickListener: (Song, Int) -> Unit) :
    BaseRecyclerAdapter<Song, ItemSongInfoBinding>(onItemClickListener) {

    override fun getLayoutId(viewType: Int) = R.layout.item_song_info

    override fun bind(
        binding: ItemSongInfoBinding,
        item: Song,
        position: Int
    ) {
        with(binding) {
            this.item = item
            tvSongTitle.isSelected = true
            tvArtistName.isSelected = true
        }
    }
}