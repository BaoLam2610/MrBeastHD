package com.lambao.mrbeast.presentation.ui.fragment.play_song

import android.os.Bundle
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.lambao.base.extension.getParcelableListCompat
import com.lambao.base.extension.popBackStack
import com.lambao.base.presentation.ui.fragment.BaseVMFragment
import com.lambao.mrbeast.domain.model.Thumbnail
import com.lambao.mrbeast.extension.toDp
import com.lambao.mrbeast.presentation.ui.activity.MusicActivity
import com.lambao.mrbeast.presentation.ui.fragment.common.SongThumbnailAdapter
import com.lambao.mrbeast.utils.Constants
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.FragmentPlaySongBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaySongFragment : BaseVMFragment<FragmentPlaySongBinding, PlaySongViewModel>() {

    private val songThumbnails by lazy {
        arguments?.getParcelableListCompat<Thumbnail>(Constants.Argument.THUMBNAILS) ?: emptyList()
    }

    private lateinit var thumbnailAdapter: SongThumbnailAdapter

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
        setupViewPager()
    }

    override fun initObserve() {
        binding.viewModel = viewModel
        thumbnailAdapter.submitList(songThumbnails)
    }

    private fun setupViewPager() {
        thumbnailAdapter = SongThumbnailAdapter()
        binding.viewPager.adapter = thumbnailAdapter
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.clipChildren = false
        binding.viewPager.clipToPadding = false
        binding.viewPager.setPageTransformer(getTransformation())
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
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