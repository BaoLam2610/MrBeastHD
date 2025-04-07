package com.lambao.mrbeast.presentation.ui.fragment.online_songs

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
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class OnlinePlaylistFragment : BaseVMFragment<FragmentOnlinePlaylistBinding, OnlinePlaylistViewModel>() {

    private val songsAdapter by lazy {
        SongInfoAdapter { song, _ ->
            viewModel.setSelectedSong(song)
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
            viewModel.songThumbnails.collect()
        }

        launchWhenCreated {
            viewModel.shouldShowEmptyData.collect()
        }

        launchWhenCreated {
            viewModel.shouldFetchInfo.collect()
        }

        launchWhenCreated {
            viewModel.selectedSong.collectLatest {
                navigate(
                    R.id.action_onlinePlaylistFragment_to_playSongFragment,
                    args = bundleOf(
                        Constants.Argument.SONG to it,
                        Constants.Argument.PLAYLIST to viewModel.playlistValue
                    )
                )
            }
        }

        viewModel.getOnlineSongs()
    }
}