package com.lambao.mrbeast.presentation.ui.fragment.common

import com.lambao.base.presentation.ui.recycler_view.BaseRecyclerAdapter
import com.lambao.mrbeast.domain.model.Thumbnail
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.ItemSongThumbnailBinding

class SongThumbnailAdapter : BaseRecyclerAdapter<Thumbnail, ItemSongThumbnailBinding>() {

    override fun getLayoutId(viewType: Int) = R.layout.item_song_thumbnail

    override fun bind(
        binding: ItemSongThumbnailBinding,
        item: Thumbnail,
        position: Int
    ) {
        with(binding) {
            this.item = item
        }
    }
}