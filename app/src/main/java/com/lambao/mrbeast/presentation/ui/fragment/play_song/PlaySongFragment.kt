package com.lambao.mrbeast.presentation.ui.fragment.play_song

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.SeekBar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.lambao.base.extension.click
import com.lambao.base.extension.getParcelableCompat
import com.lambao.base.extension.getParcelableListCompat
import com.lambao.base.extension.launchWhenCreated
import com.lambao.base.extension.popBackStack
import com.lambao.base.presentation.ui.fragment.BaseVMFragment
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.service.MediaPlayerService
import com.lambao.mrbeast.extension.toDp
import com.lambao.mrbeast.extension.toTimeString
import com.lambao.mrbeast.presentation.ui.activity.MusicActivity
import com.lambao.mrbeast.presentation.ui.fragment.common.SongThumbnailAdapter
import com.lambao.mrbeast.utils.Constants
import com.lambao.mrbeast.utils.Constants.Argument.DURATION
import com.lambao.mrbeast.utils.Constants.Argument.POSITION
import com.lambao.mrbeast.utils.Constants.Argument.SONG
import com.lambao.mrbeast.utils.Constants.Argument.STATE
import com.lambao.mrbeast.utils.Constants.Broadcast.ACTION_METADATA_CHANGED
import com.lambao.mrbeast.utils.Constants.Broadcast.ACTION_PLAYBACK_STATE_CHANGED
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.FragmentPlaySongBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PlaySongFragment : BaseVMFragment<FragmentPlaySongBinding, PlaySongViewModel>() {

    private lateinit var thumbnailAdapter: SongThumbnailAdapter
    private lateinit var broadcastReceiver: BroadcastReceiver
    private var isReceiverRegistered = false

    private val argSong by lazy {
        arguments?.getParcelableCompat<Song>(SONG)
    }

    private val argSongList by lazy {
        arguments?.getParcelableListCompat<Song>(Constants.Argument.SONGS) ?: emptyList()
    }

    private val onPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setSong(argSongList[position])
            }
        }
    }

    override fun getLayoutResId() = R.layout.fragment_play_song

    override fun getViewModelClass() = PlaySongViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as? MusicActivity)?.hideToolbar()
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        binding.toolbar.setOnBackClickListener {
            popBackStack()
        }

        binding.btnSongAction.click {
            if (binding.btnSongAction.isChecked) {
                MediaPlayerService.startService(
                    requireContext(),
                    Constants.MediaAction.PAUSE
                )
            } else {
                MediaPlayerService.startService(
                    requireContext(),
                    Constants.MediaAction.PLAY,
                    playlist = viewModel.songList.value,
                    startIndex = viewModel.currentSongIndex.value,
                    position = viewModel.currentDuration.value
                )
            }
        }

        binding.btnPreviousSong.click {
            viewModel.previousSong()
            MediaPlayerService.startService(
                requireContext(),
                Constants.MediaAction.PREVIOUS
            )
        }
        binding.btnNextSong.click {
            viewModel.nextSong()
            MediaPlayerService.startService(
                requireContext(),
                Constants.MediaAction.NEXT
            )
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val position = progress * 1000L
                    viewModel.setCurrentDuration(position)
                    MediaPlayerService.startService(
                        requireContext(),
                        Constants.MediaAction.SEEK_TO,
                        position = position
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        setupViewPager()
        setupBroadcastReceiver()
    }

    override fun initObserve() {
        binding.viewModel = viewModel
        with(viewModel) {
            launchWhenCreated {
                songThumbnails.collect {
                    thumbnailAdapter.submitList(it)
                }
            }

            launchWhenCreated {
                combineIndexInPlaylist.collectLatest {
                    if (it != -1) {
                        setCurrentSongIndex(it)
                    }
                }
            }

            launchWhenCreated {
                currentSongIndex.collect {
                    delay(300)
                    binding.viewPager.setCurrentItem(it, true)
                }
            }

            launchWhenCreated {
                shouldRegisterPageChangeListener.collect {
                    if (it) {
                        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback)
                    } else {
                        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
                    }
                }
            }

            launchWhenCreated {
                shouldPlaySong.collectLatest {
                    if (it) {
                        MediaPlayerService.startService(
                            requireContext(),
                            Constants.MediaAction.PLAY,
                            playlist = songList.value,
                            startIndex = currentSongIndex.value
                        )
                    }
                }
            }

            argSong?.let {
                setSong(it)
            }
            setSongList(argSongList)
        }
    }

    private fun setupViewPager() {
        thumbnailAdapter = SongThumbnailAdapter()
        binding.viewPager.apply {
            adapter = thumbnailAdapter
            offscreenPageLimit = 3
            clipChildren = false
            clipToPadding = false
            setPageTransformer(getTransformation())
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
    }

    private fun getTransformation(): CompositePageTransformer {
        val transform = CompositePageTransformer()
        transform.addTransformer(MarginPageTransformer(12.toDp))
        transform.addTransformer { page, position ->
            page.scaleY = 0.85f + (1 - kotlin.math.abs(position)) * 0.15f
        }
        return transform
    }

    private fun setupBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    ACTION_PLAYBACK_STATE_CHANGED -> {
                        val state = intent.getIntExtra(STATE, PlaybackStateCompat.STATE_NONE)
                        val position = intent.getLongExtra(POSITION, 0L)
                        val duration = intent.getLongExtra(DURATION, 0L)
                        updatePlaybackUI(state, position, duration)
                    }

                    ACTION_METADATA_CHANGED -> {
                        val song = intent.getParcelableExtra<Song>(SONG)
                        updateSongInfoUI(song)
                    }
                }
            }
        }

        val intentFilter = IntentFilter().apply {
            addAction(ACTION_PLAYBACK_STATE_CHANGED)
            addAction(ACTION_METADATA_CHANGED)
        }
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(broadcastReceiver, intentFilter)
        isReceiverRegistered = true
    }

    private fun updatePlaybackUI(state: Int, position: Long, duration: Long) {
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
                binding.tvTime.text = Constants.Media.DEFAULT_TIME
            }

            PlaybackStateCompat.STATE_BUFFERING -> showLoading()
            PlaybackStateCompat.STATE_ERROR -> {
                hideLoading()
                dialogHandler.showAlertDialog(
                    title = getString(R.string.error),
                    message = getString(R.string.an_error_occurred_while_playing_the_song),
                    positiveText = null,
                    negativeText = getString(R.string.close)
                )
            }

            else -> hideLoading()
        }
    }

    private fun updateSongInfoUI(song: Song?) {
        song?.let {
            viewModel.setSong(it)
            val index = argSongList.indexOf(it)
            if (index != -1) {
                viewModel.setCurrentSongIndex(index)
                binding.viewPager.setCurrentItem(index, true)
            }
        }
    }

    private fun updateSeekBar(position: Long, duration: Long) {
        if (duration > 0) {
            val convertDuration = duration / 1000
            val convertPosition = position / 1000
            binding.seekBar.max = convertDuration.toInt()
            binding.seekBar.progress = convertPosition.toInt()
            binding.tvDuration.text = convertDuration.toTimeString()
            binding.tvTime.text = convertPosition.toTimeString()
        }
    }

    override fun onDestroyView() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(requireContext())
                .unregisterReceiver(broadcastReceiver)
            isReceiverRegistered = false
        }
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
        super.onDestroyView()
    }

    override fun onDestroy() {
        (requireActivity() as? MusicActivity)?.showToolbar()
        super.onDestroy()
    }
}