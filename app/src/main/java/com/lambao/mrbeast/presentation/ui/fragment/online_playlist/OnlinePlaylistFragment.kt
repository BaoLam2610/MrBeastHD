package com.lambao.mrbeast.presentation.ui.fragment.online_playlist

import android.os.Bundle
import androidx.core.os.bundleOf
import com.lambao.base.extension.launchWhenCreated
import com.lambao.base.extension.navigate
import com.lambao.base.extension.observe
import com.lambao.base.presentation.ui.fragment.BaseVMFragment
import com.lambao.mrbeast.presentation.ui.fragment.common.SongInfoAdapter
import com.lambao.mrbeast.utils.Constants
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.FragmentOnlinePlaylistBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class OnlinePlaylistFragment :
    BaseVMFragment<FragmentOnlinePlaylistBinding, OnlinePlaylistViewModel>() {

    private val songsAdapter by lazy {
        SongInfoAdapter { song, index ->
            navigate(
                R.id.action_onlinePlaylistFragment_to_playSongFragment,
                args = bundleOf(
                    Constants.Argument.START_INDEX to index,
                    Constants.Argument.SONG to song,
                    Constants.Argument.PLAYLIST to viewModel.playlistValue
                )
            )
        }
    }

    override fun getLayoutResId() = R.layout.fragment_online_playlist

    override fun getViewModelClass() = OnlinePlaylistViewModel::class.java

    override fun onViewReady(savedInstanceState: Bundle?) {
        binding.rvSongs.adapter = songsAdapter
    }

    override fun initObserve() {
        binding.viewModel = viewModel

        observe(viewModel.playlist) {
            songsAdapter.submitList(it)
        }

        launchWhenCreated {
            viewModel.shouldShowEmptyData.collect()
        }

        launchWhenCreated {
            viewModel.shouldFetchInfo.collect()
        }

        viewModel.getOnlinePlaylist()
    }
}