package com.lambao.mrbeast.presentation.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.ui.setupWithNavController
import com.lambao.base.extension.click
import com.lambao.base.extension.findNavController
import com.lambao.base.extension.getParcelableCompat
import com.lambao.base.extension.gone
import com.lambao.base.extension.launchWhenCreated
import com.lambao.base.extension.visible
import com.lambao.base.presentation.handler.permission.common.PermissionHandlerFactory
import com.lambao.base.presentation.ui.activity.BaseVMActivity
import com.lambao.base.utils.log
import com.lambao.mrbeast.domain.model.MenuItem
import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.model.playback.PlaybackEvent
import com.lambao.mrbeast.domain.service.MediaPlayerService
import com.lambao.mrbeast.utils.Constants.Argument.DURATION
import com.lambao.mrbeast.utils.Constants.Argument.PLAYLIST
import com.lambao.mrbeast.utils.Constants.Argument.POSITION
import com.lambao.mrbeast.utils.Constants.Argument.SONG
import com.lambao.mrbeast.utils.Constants.Argument.START_INDEX
import com.lambao.mrbeast.utils.Constants.Argument.STATE
import com.lambao.mrbeast.utils.Constants.Broadcast.ACTION_METADATA_CHANGED
import com.lambao.mrbeast.utils.Constants.Broadcast.ACTION_PLAYBACK_STATE_CHANGED
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.ActivityMusicBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MusicActivity : BaseVMActivity<ActivityMusicBinding, MusicViewModel>() {

    private lateinit var broadcastReceiver: BroadcastReceiver
    private var isReceiverRegistered = false

    private val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    private val menuAdapter by lazy {
        DrawerMenuAdapter { menuItem, _ ->
            if (menuItem != MenuItem.LANGUAGE) {
                binding.drawerLayout.close()
            }
            when (menuItem) {
                MenuItem.DISCOVER -> navController.navigate(R.id.onlinePlaylistFragment)
                MenuItem.MY_MUSIC -> navController.navigate(R.id.offlinePlaylistFragment)
                MenuItem.FAVORITE_SONG -> navController.navigate(R.id.favoriteSongsFragment)
                MenuItem.LANGUAGE -> {}
            }
        }
    }

    override fun getLayoutResId() = R.layout.activity_music

    override fun getViewModelClass() = MusicViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.drawerLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupNavigation()
        setupDrawerLayout()
        setupMiniPlayerArea()
        setupBroadcastReceiver()
        setupRequestPermission()
    }

    override fun initObserve() {
        binding.viewModel = viewModel

        launchWhenCreated {
            viewModel.isPlaying().collectLatest { }
        }

        launchWhenCreated {
            viewModel.shouldShowMiniPlayer().collectLatest { }
        }

        launchWhenCreated {
            viewModel.getPlaylist().collect()
        }

        launchWhenCreated {
            viewModel.currentSongIndex.collect()
        }

        launchWhenCreated {
            viewModel.getCurrentDuration().collect()
        }

        launchWhenCreated {
            viewModel.getCurrentDurationInt().collect()
        }

        launchWhenCreated {
            viewModel.getCurrentDurationTime().collect()
        }

        launchWhenCreated {
            viewModel.getTotalDuration().collect()
        }

        launchWhenCreated {
            viewModel.getTotalDurationInt().collect()
        }

        launchWhenCreated {
            viewModel.getTotalDurationTime().collect()
        }

        launchWhenCreated {
            viewModel.getPlaybackEvent().collectLatest { event ->
                when (event) {
                    PlaybackEvent.PLAY -> MediaPlayerService.play(
                        this@MusicActivity,
                        playlist = viewModel.getPlaylistValue(),
                        startIndex = viewModel.currentSongIndex.value
                    )

                    PlaybackEvent.PAUSE -> MediaPlayerService.pause(this@MusicActivity)

                    PlaybackEvent.RESUME -> MediaPlayerService.resume(
                        this@MusicActivity,
                        position = viewModel.getCurrentDurationValue()
                    )

                    PlaybackEvent.SEEK_TO -> MediaPlayerService.seekTo(
                        this@MusicActivity,
                        position = viewModel.getCurrentDurationValue()
                    )

                    PlaybackEvent.PREVIOUS -> MediaPlayerService.previous(this@MusicActivity)

                    PlaybackEvent.NEXT -> MediaPlayerService.next(this@MusicActivity)

                    else -> Unit
                }
            }
        }

        launchWhenCreated {
            viewModel.getRepeatMode().collectLatest {
                MediaPlayerService.repeat(
                    this@MusicActivity,
                    it
                )
            }
        }

        launchWhenCreated {
            viewModel.getShuffleMode().collectLatest {
                MediaPlayerService.shuffle(
                    this@MusicActivity,
                    it
                )
            }
        }
    }

    private fun setupNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            log("Current destination: ${destination.label}")
        }

        binding.navigationView.setupWithNavController(navController)
    }

    private fun setupDrawerLayout() {
        binding.toolbar.setOnBackClickListener {
            binding.drawerLayout.open()
        }

        binding.layoutDrawerBody.btnClose.click {
            binding.drawerLayout.close()
        }

        binding.layoutDrawerBody.rvMenu.adapter = menuAdapter
        menuAdapter.submitList(MenuItem.entries)
    }

    private fun setupMiniPlayerArea() {
        binding.layoutMediaController.root.click {
            navController.navigate(
                R.id.playSongFragment,
                args = bundleOf(
                    START_INDEX to viewModel.currentSongIndex.value,
                    SONG to viewModel.getSongValue(),
                    PLAYLIST to viewModel.getPlaylistValue()
                )
            )
        }
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
        LocalBroadcastManager.getInstance(this).registerReceiver(
            broadcastReceiver,
            IntentFilter().apply {
                addAction(ACTION_PLAYBACK_STATE_CHANGED)
                addAction(ACTION_METADATA_CHANGED)
            }
        )
        isReceiverRegistered = true
    }

    private fun setupRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPermissionHandler(
                android.Manifest.permission.POST_NOTIFICATIONS,
                permissionDescription = getString(com.lambao.base.R.string.notification_permission_description)
            )?.also {
                it.request {
                    log(it.toString())
                }
            }
        }
    }

    private fun handlePlaybackStateChanged(intent: Intent) {
        val state = intent.getIntExtra(STATE, PlaybackStateCompat.STATE_NONE)
        val position = intent.getLongExtra(POSITION, 0L)
        val duration = intent.getLongExtra(DURATION, 0L)
        when (state) {
            PlaybackStateCompat.STATE_PLAYING -> {
                updateSeekBar(position, duration)
            }

            PlaybackStateCompat.STATE_PAUSED -> {
                updateSeekBar(position, duration)
            }

            PlaybackStateCompat.STATE_STOPPED -> {
                viewModel.setCurrentDuration(0)
                viewModel.setPlaybackEvent(PlaybackEvent.STOP)
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
            viewModel.setCurrentDuration(position)
            viewModel.setTotalDuration(duration)
        }
    }

    fun showToolbar() {
        viewModel.setShouldShowMiniPlayer(
            viewModel.isPlaying().value
        )
        binding.toolbar.visible()
    }

    fun hideToolbar() {
        viewModel.setShouldShowMiniPlayer(false)
        binding.toolbar.gone()
    }

    private fun showErrorDialog() {
        dialogHandler.showAlertDialog(
            title = getString(R.string.error),
            message = getString(R.string.an_error_occurred_while_playing_the_song),
            positiveText = null,
            negativeText = getString(R.string.close)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
            isReceiverRegistered = false
        }
        super.onDestroy()
    }
}