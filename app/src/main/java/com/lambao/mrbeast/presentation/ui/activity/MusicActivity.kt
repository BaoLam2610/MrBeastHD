package com.lambao.mrbeast.presentation.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lambao.base.presentation.ui.activity.BaseActivity
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.ActivityMusicBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicActivity : BaseActivity<ActivityMusicBinding>() {

    override fun getLayoutResId() = R.layout.activity_music

    override fun onViewReady(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}