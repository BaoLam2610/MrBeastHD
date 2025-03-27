package com.lambao.mrbeast.presentation.ui.fragment.online_songs

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.lambao.base.extension.observeData
import com.lambao.base.presentation.ui.fragment.BaseVMFragment
import com.lambao.base.utils.log
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.FragmentOnlineSongsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnlineSongsFragment : BaseVMFragment<FragmentOnlineSongsBinding, OnlineSongsViewModel>() {

    override fun getLayoutResId() = R.layout.fragment_online_songs

    override fun getViewModelClass() = OnlineSongsViewModel::class.java

    override fun onViewReady(savedInstanceState: Bundle?) {

    }

    override fun initObserve() {
        observeData(viewModel.songs) {
            log(it.toString())
        }

        viewModel.getOnlineSongs()
    }
}