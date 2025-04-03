package com.lambao.mrbeast.presentation.ui.fragment.online_songs

import android.os.Bundle
import androidx.core.os.bundleOf
import com.lambao.base.extension.launchWhenCreated
import com.lambao.base.extension.navigateWithArgs
import com.lambao.base.extension.observe
import com.lambao.base.extension.safeNavigate
import com.lambao.base.presentation.ui.fragment.BaseVMFragment
import com.lambao.mrbeast.presentation.ui.fragment.common.SongInfoAdapter
import com.lambao.mrbeast.utils.Constants
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.FragmentOnlineSongsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class OnlineSongsFragment : BaseVMFragment<FragmentOnlineSongsBinding, OnlineSongsViewModel>() {

    private val songsAdapter by lazy {
        SongInfoAdapter { song, _ ->
            viewModel.setSelectedSong(song)
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

        launchWhenCreated {
            viewModel.selectedSong.collectLatest {
                safeNavigate(
                    R.id.action_onlineSongsFragment_to_playSongFragment,
                    args = bundleOf(
                        Constants.Argument.SONG to it,
                        Constants.Argument.SONGS to viewModel.songs.value
                    )
                )
            }
        }

        viewModel.getOnlineSongs()
    }
}