package com.lambao.mrbeast.presentation.ui.fragment.play_song

import android.os.Bundle
import com.lambao.base.presentation.ui.fragment.BaseVMFragment
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.FragmentPlaySongBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaySongFragment: BaseVMFragment<FragmentPlaySongBinding, PlaySongViewModel>() {

    override fun getLayoutResId() = R.layout.fragment_play_song

    override fun getViewModelClass() = PlaySongViewModel::class.java

    override fun onViewReady(savedInstanceState: Bundle?) {

    }

    override fun initObserve() {

    }
}