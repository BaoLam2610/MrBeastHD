package com.lambao.mrbeast.presentation.ui.fragment.offline_songs

import android.os.Bundle
import com.lambao.base.presentation.ui.fragment.BaseVMFragment
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.FragmentOfflineSongsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfflineSongsFragment: BaseVMFragment<FragmentOfflineSongsBinding, OfflineSongsViewModel>() {

    override fun getLayoutResId() = R.layout.fragment_offline_songs

    override fun getViewModelClass() = OfflineSongsViewModel::class.java

    override fun onViewReady(savedInstanceState: Bundle?) {

    }

    override fun initObserve() {

    }

}