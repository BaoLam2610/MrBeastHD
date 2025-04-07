package com.lambao.mrbeast.presentation.ui.fragment.play_song

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.SeekBar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.lambao.base.extension.click
import com.lambao.base.extension.getParcelableCompat
import com.lambao.base.extension.getParcelableListCompat
import com.lambao.base.extension.launchWhenCreated
import com.lambao.base.extension.popBackStack
import com.lambao.base.presentation.ui.fragment.BaseVMFragment
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.model.playback.PlaybackEvent
import com.lambao.mrbeast.domain.service.MediaPlayerService
import com.lambao.mrbeast.extension.toTimeString
import com.lambao.mrbeast.presentation.ui.activity.MusicActivity
import com.lambao.mrbeast.utils.Constants
import com.lambao.mrbeast.utils.Constants.Argument.DURATION
import com.lambao.mrbeast.utils.Constants.Argument.PLAYLIST
import com.lambao.mrbeast.utils.Constants.Argument.POSITION
import com.lambao.mrbeast.utils.Constants.Argument.SONG
import com.lambao.mrbeast.utils.Constants.Argument.STATE
import com.lambao.mrbeast.utils.Constants.Broadcast.ACTION_METADATA_CHANGED
import com.lambao.mrbeast.utils.Constants.Broadcast.ACTION_PLAYBACK_STATE_CHANGED
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.FragmentPlaySongBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PlaySongFragment : BaseVMFragment<FragmentPlaySongBinding, PlaySongViewModel>() {

    private lateinit var broadcastReceiver: BroadcastReceiver
    private var isReceiverRegistered = false

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
        setupUI()
        setupBroadcastReceiver()
        viewModel.initializePlaylist(argPlaylist, argStartIndex)
    }

    override fun initObserve() {
        binding.viewModel = viewModel
        viewModel.setSong(argSong)

        launchWhenCreated {
            viewModel.getPlaybackEvent().collectLatest { event ->
                when (event) {
                    PlaybackEvent.PLAY -> MediaPlayerService.play(
                        requireContext(),
                        playlist = viewModel.getPlaylistValue(),
                        startIndex = viewModel.currentSongIndex.value
                    )

                    PlaybackEvent.PAUSE -> MediaPlayerService.pause(requireContext())

                    PlaybackEvent.RESUME -> MediaPlayerService.resume(
                        requireContext(),
                        position = viewModel.getCurrentDurationValue()
                    )

                    PlaybackEvent.SEEK_TO -> MediaPlayerService.seekTo(
                        requireContext(),
                        position = viewModel.getCurrentDurationValue()
                    )

                    PlaybackEvent.PREVIOUS -> MediaPlayerService.previous(requireContext())

                    PlaybackEvent.NEXT -> MediaPlayerService.next(requireContext())

                    else -> Unit
                }
            }
        }

        launchWhenCreated {
            viewModel.getRepeatMode().collectLatest {
                MediaPlayerService.repeat(
                    requireContext(),
                    it
                )
            }
        }

        launchWhenCreated {
            viewModel.getShuffleMode().collectLatest {
                MediaPlayerService.shuffle(
                    requireContext(),
                    it
                )
            }
        }
    }

    private fun setupUI() {
        binding.toolbar.setOnBackClickListener { popBackStack() }
        binding.btnSongAction.click {
            viewModel.togglePlayPause(binding.btnSongAction.isChecked)
        }
        binding.btnPreviousSong.click { viewModel.previousSong() }
        binding.btnNextSong.click { viewModel.nextSong() }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                val position = progress.toLong()
                viewModel.setCurrentPosition(position)
                if (fromUser) {
                    viewModel.seekTo(position)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    ACTION_PLAYBACK_STATE_CHANGED -> handlePlaybackStateChanged(intent)
                    ACTION_METADATA_CHANGED -> handleMetadataChanged(intent)
                }
            }
        }
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            broadcastReceiver,
            IntentFilter().apply {
                addAction(ACTION_PLAYBACK_STATE_CHANGED)
                addAction(ACTION_METADATA_CHANGED)
            }
        )
        isReceiverRegistered = true
    }

    private fun handlePlaybackStateChanged(intent: Intent) {
        val state = intent.getIntExtra(STATE, PlaybackStateCompat.STATE_NONE)
        val position = intent.getLongExtra(POSITION, 0L)
        val duration = intent.getLongExtra(DURATION, 0L)
        when (state) {
            PlaybackStateCompat.STATE_PLAYING -> {
                binding.btnSongAction.isChecked = true
                updateSeekBar(position, duration)
            }

            PlaybackStateCompat.STATE_PAUSED -> {
                binding.btnSongAction.isChecked = false
                updateSeekBar(position, duration)
            }

            PlaybackStateCompat.STATE_STOPPED -> {
                binding.btnSongAction.isChecked = false
                binding.seekBar.progress = 0
                binding.tvTime.text = getString(R.string.default_duration)
            }

            PlaybackStateCompat.STATE_BUFFERING -> showLoading()
            PlaybackStateCompat.STATE_ERROR -> {
                hideLoading()
                showErrorDialog()
            }

            else -> hideLoading()
        }
    }

    private fun handleMetadataChanged(intent: Intent) {
        intent.getParcelableCompat<Song>(SONG)?.let { song ->
            viewModel.setSong(song)
        }
    }

    private fun updateSeekBar(position: Long, duration: Long) {
        if (duration > 0) {
            binding.seekBar.max = duration.toInt()
            binding.seekBar.progress = position.toInt()
            binding.tvTime.text = (position / 1000).toTimeString()
            binding.tvDuration.text = (duration / 1000).toTimeString()
        }
    }

    private fun showErrorDialog() {
        dialogHandler.showAlertDialog(
            title = getString(R.string.error),
            message = getString(R.string.an_error_occurred_while_playing_the_song),
            positiveText = null,
            negativeText = getString(R.string.close)
        )
    }

    override fun onDestroyView() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(requireContext())
                .unregisterReceiver(broadcastReceiver)
            isReceiverRegistered = false
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        (requireActivity() as? MusicActivity)?.showToolbar()
        super.onDestroy()
    }
}