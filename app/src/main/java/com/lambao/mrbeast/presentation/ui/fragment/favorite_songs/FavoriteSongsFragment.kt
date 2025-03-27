package com.lambao.mrbeast.presentation.ui.fragment.favorite_songs

import android.os.Bundle
import com.lambao.base.presentation.ui.fragment.BaseVMFragment
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.FragmentFavoriteSongsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteSongsFragment: BaseVMFragment<FragmentFavoriteSongsBinding, FavoriteSongsViewModel>() {

    override fun getLayoutResId() = R.layout.fragment_favorite_songs

    override fun getViewModelClass() = FavoriteSongsViewModel::class.java

    override fun onViewReady(savedInstanceState: Bundle?) {

    }

    override fun initObserve() {

    }

}