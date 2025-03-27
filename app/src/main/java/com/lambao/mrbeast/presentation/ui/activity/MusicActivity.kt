package com.lambao.mrbeast.presentation.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.lambao.base.extension.click
import com.lambao.base.presentation.ui.activity.BaseActivity
import com.lambao.base.utils.log
import com.lambao.mrbeast.data.model.MenuItem
import com.lambao.mrbeast_music.R
import com.lambao.mrbeast_music.databinding.ActivityMusicBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicActivity : BaseActivity<ActivityMusicBinding>() {

    private lateinit var navController: NavController

    private val menuAdapter by lazy {
        DrawerMenuAdapter { menuItem, _ ->
//            when (menuItem) {
//                MenuItem.DISCOVER -> navController.navigate(R.id.)
//                MenuItem.MY_MUSIC -> navController.navigate(R.id.myMusicFragment)
//                MenuItem.FAVORITE_SONG -> navController.navigate(R.id.favoriteSongFragment)
//                MenuItem.LANGUAGE -> navController.navigate(R.id.languageFragment)
//            }
        }
    }

    override fun getLayoutResId() = R.layout.activity_music

    override fun onViewReady(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupNavigation()
        setupDrawerLayout()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            log("Current destination: ${destination.label}")
        }

        binding.navigationView.setupWithNavController(navController)
    }

    private fun setupDrawerLayout() {
        binding.btnDrawerMenu.click {
            binding.drawerLayout.open()
        }

        binding.layoutDrawerBody.btnClose.click {
            binding.drawerLayout.close()
        }

        binding.layoutDrawerBody.rvMenu.adapter = menuAdapter
        menuAdapter.submitList(MenuItem.entries)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}