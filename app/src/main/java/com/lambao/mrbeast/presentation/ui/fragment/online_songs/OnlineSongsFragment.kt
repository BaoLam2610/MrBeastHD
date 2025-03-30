package com.lambao.mrbeast.presentation.ui.fragment.online_songs

import android.os.Bundle
import androidx.core.os.bundleOf
import com.lambao.base.extension.launchWhenCreated
import com.lambao.base.extension.navigateWithArgs
import com.lambao.base.extension.observe
import com.lambao.base.presentation.ui.fragment.BaseVMFragment
import com.lambao.mrbeast.presentation.ui.fragment.common.SongInfoAdapter
import com.lambao.mrbeast.utils.Constants
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.FragmentOnlineSongsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class OnlineSongsFragment : BaseVMFragment<FragmentOnlineSongsBinding, OnlineSongsViewModel>() {

    private val songsAdapter by lazy {
        SongInfoAdapter { song, _ ->
            navigateWithArgs(
                R.id.action_onlineSongsFragment_to_playSongFragment,
                bundleOf(
                    Constants.Argument.SONG to song,
                    Constants.Argument.THUMBNAILS to viewModel.songThumbnails.value
                )
            )
        }
    }

    override fun getLayoutResId() = R.layout.fragment_online_songs

    override fun getViewModelClass() = OnlineSongsViewModel::class.java

    override fun onViewReady(savedInstanceState: Bundle?) {
        binding.rvSongs.adapter = songsAdapter
    }

    override fun initObserve() {
        binding.viewModel = viewModel

        observe(viewModel.songs) {
            songsAdapter.submitList(it)
        }

        launchWhenCreated {
            viewModel.songThumbnails.collect()
        }

        launchWhenCreated {
            viewModel.shouldShowEmptyData.collect()
        }

        viewModel.getOnlineSongs()
    }
}