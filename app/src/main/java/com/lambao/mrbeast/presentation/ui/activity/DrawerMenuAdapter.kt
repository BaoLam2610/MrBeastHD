package com.lambao.mrbeast.presentation.ui.activity

import com.lambao.base.presentation.ui.recycler_view.BaseRecyclerAdapter
import com.lambao.mrbeast.data.model.MenuItem
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.ItemDrawerMenuBinding

class DrawerMenuAdapter(
    onItemClickListener: (MenuItem, Int) -> Unit
) : BaseRecyclerAdapter<MenuItem, ItemDrawerMenuBinding>(onItemClickListener) {
    override fun getLayoutId(viewType: Int) = R.layout.item_drawer_menu

    override fun bind(
        binding: ItemDrawerMenuBinding,
        item: MenuItem,
        position: Int
    ) {
        binding.item = item
    }
}