package com.lambao.mrbeast.presentation.ui.fragment.play_song

import android.os.Bundle
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import com.lambao.base.extension.getParcelableCompat
import com.lambao.base.extension.getParcelableListCompat
import com.lambao.base.extension.popBackStack
import com.lambao.base.presentation.ui.fragment.BaseVMFragment
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.presentation.ui.activity.MusicActivity
import com.lambao.mrbeast.presentation.ui.activity.MusicViewModel
import com.lambao.mrbeast.utils.Constants
import com.lambao.mrbeast.utils.Constants.Argument.PLAYLIST
import com.lambao.mrbeast.utils.Constants.Argument.SONG
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.FragmentPlaySongBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaySongFragment : BaseVMFragment<FragmentPlaySongBinding, PlaySongViewModel>() {

    private val musicViewModel by activityViewModels<MusicViewModel>()

    private val argStartIndex by lazy {
        arguments?.getInt(Constants.Argument.START_INDEX, 0) ?: 0
    }

    private val argSong by lazy {
        arguments?.getParcelableCompat<Song>(SONG)
    }

    private val argPlaylist by lazy {
        arguments?.getParcelableListCompat<Song>(PLAYLIST) ?: emptyList()
    }

    override fun getLayoutResId() = R.layout.fragment_play_song

    override fun getViewModelClass() = PlaySongViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as? MusicActivity)?.hideToolbar()
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        binding.toolbar.setOnBackClickListener { popBackStack() }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                val position = progress.toLong()
                musicViewModel.setCurrentDuration(position)
                if (fromUser) {
                    musicViewModel.seekTo(position)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun initObserve() {
        binding.viewModel = viewModel
        binding.musicViewModel = musicViewModel
        if (musicViewModel.getSongValue()?.id == argSong?.id) return
        musicViewModel.setSong(argSong)
        musicViewModel.initializePlaylist(argPlaylist, argStartIndex)
    }

    override fun onDestroy() {
        (requireActivity() as? MusicActivity)?.showToolbar()
        super.onDestroy()
    }
}