package com.lambao.mrbeast.presentation.ui.fragment.play_song

import android.os.Bundle
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
import com.lambao.mrbeast.extension.toDp
import com.lambao.mrbeast.presentation.ui.activity.MusicActivity
import com.lambao.mrbeast.presentation.ui.fragment.common.SongThumbnailAdapter
import com.lambao.mrbeast.utils.Constants
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.FragmentPlaySongBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaySongFragment : BaseVMFragment<FragmentPlaySongBinding, PlaySongViewModel>() {

    private lateinit var thumbnailAdapter: SongThumbnailAdapter

    private val argSong by lazy {
        arguments?.getParcelableCompat<Song>(Constants.Argument.SONG)
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
            (requireActivity() as? MusicActivity)?.getMediaPlayerService()?.playSong(
               viewModel.song.value ?: return@click
            )
        }
        setupViewPager()
    }

    override fun initObserve() {
        binding.viewModel = viewModel
        with(viewModel) {
            launchWhenCreated {
                songThumbnails.collect {
                    thumbnailAdapter.submitList(it)
                }
            }

            argSong?.let { setSong(it) }
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
            registerOnPageChangeCallback(onPageChangeCallback)
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

    override fun onDestroy() {
        (requireActivity() as? MusicActivity)?.showToolbar()
        super.onDestroy()
    }
}