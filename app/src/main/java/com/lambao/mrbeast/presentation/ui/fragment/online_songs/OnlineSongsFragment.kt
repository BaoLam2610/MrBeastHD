package com.lambao.mrbeast.presentation.ui.fragment.online_songs

import android.os.Bundle
import com.lambao.base.extension.observeData
import com.lambao.base.presentation.ui.fragment.BaseVMFragment
import com.lambao.base.utils.log
import com.lambao.mrbeast.presentation.ui.fragment.common.SongInfoAdapter
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.FragmentOnlineSongsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnlineSongsFragment : BaseVMFragment<FragmentOnlineSongsBinding, OnlineSongsViewModel>() {

    private val songsAdapter by lazy {
        SongInfoAdapter { song, _ ->
            log(song.toString())
        }
    }

    override fun getLayoutResId() = R.layout.fragment_online_songs

    override fun getViewModelClass() = OnlineSongsViewModel::class.java

    override fun onViewReady(savedInstanceState: Bundle?) {
        binding.rvSongs.adapter = songsAdapter
    }

    override fun initObserve() {
        observeData(viewModel.songs) {
            songsAdapter.submitList(it ?: emptyList())
        }

        viewModel.getOnlineSongs()
    }
}