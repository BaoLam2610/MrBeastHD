package com.lambao.base.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.launch


/**
 * Helper function to find NavController from an Activity's NavHostFragment.
 *
 * @param navHostFragmentId The ID of the NavHostFragment in the Activity's layout
 * @return The NavController associated with the NavHostFragment
 */
fun AppCompatActivity.findNavController(@IdRes navHostFragmentId: Int): NavController {
    val navHostFragment =
        supportFragmentManager.findFragmentById(navHostFragmentId) as NavHostFragment
    return navHostFragment.navController
}

/**
 * Performs a safe navigation from an Activity using the provided directions.
 *
 * @param navHostFragmentId The ID of the NavHostFragment in the Activity's layout
 * @param actionId The ID of the navigation action to perform
 * @param navOptions Optional navigation options to customize the transition
 * @param args Optional Bundle containing navigation arguments
 */
fun AppCompatActivity.navigate(
    @IdRes navHostFragmentId: Int,
    @IdRes actionId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
) {
    try {
        val navController = findNavController(navHostFragmentId)
        navController.navigate(actionId, args, navOptions)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Pops the back stack from the Activity's NavController.
 * Moves to the previous destination in the navigation stack if possible.
 *
 * @param navHostFragmentId The ID of the NavHostFragment in the Activity's layout
 * @return true if the back stack was popped successfully, false otherwise
 */
fun AppCompatActivity.popBackStack(@IdRes navHostFragmentId: Int): Boolean {
    return try {
        findNavController(navHostFragmentId).popBackStack()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * Checks if there is a previous destination in the back stack that can be navigated to.
 *
 * @param navHostFragmentId The ID of the NavHostFragment in the Activity's layout
 * @return true if there is a previous destination, false otherwise
 */
fun AppCompatActivity.canGoBack(@IdRes navHostFragmentId: Int): Boolean {
    return try {
        findNavController(navHostFragmentId).previousBackStackEntry != null
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * Gets the current Fragment instance from the Activity's NavController.
 *
 * @param navHostFragmentId The ID of the NavHostFragment in the Activity's layout
 * @return The current Fragment if available, null otherwise
 */
fun AppCompatActivity.getCurrentFragment(@IdRes navHostFragmentId: Int): Fragment? {
    return try {
        val navController = findNavController(navHostFragmentId)
        val currentBackStackEntry = navController.currentBackStackEntry
        currentBackStackEntry?.let { entry ->
            (supportFragmentManager.findFragmentById(navHostFragmentId) as? NavHostFragment)
                ?.childFragmentManager?.fragments?.firstOrNull()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Navigates to another Fragment from an Activity and listens for a result returned from it using StateFlow.
 *
 * @param navHostFragmentId The ID of the NavHostFragment in the Activity's layout
 * @param actionId The ID of the navigation action to perform
 * @param args Optional Bundle containing navigation arguments
 * @param navOptions Optional navigation options to customize the transition
 * @param resultKey The key to identify the result data
 * @param onResult Callback to handle the result when the destination Fragment returns data
 */
fun <T> AppCompatActivity.navigateForResult(
    @IdRes navHostFragmentId: Int,
    @IdRes actionId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    resultKey: String,
    onResult: (T) -> Unit
) {
    try {
        val navController = findNavController(navHostFragmentId)
        val currentBackStackEntry = navController.currentBackStackEntry
        currentBackStackEntry?.let { entry ->
            val resultFlow = entry.savedStateHandle.getStateFlow<T?>(resultKey, null)
            lifecycleScope.launch {
                resultFlow.collect { result ->
                    result?.let {
                        onResult(it)
                        entry.savedStateHandle[resultKey] = null // Reset the result
                    }
                }
            }
            navController.navigate(actionId, args, navOptions)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Sets a result to be returned to the previous destination from an Activity before popping the back stack.
 *
 * @param navHostFragmentId The ID of the NavHostFragment in the Activity's layout
 * @param resultKey The key to identify the result data
 * @param result The result data to return
 */
fun <T> AppCompatActivity.setNavigationResult(
    @IdRes navHostFragmentId: Int,
    resultKey: String,
    result: T
) {
    try {
        val navController = findNavController(navHostFragmentId)
        navController.previousBackStackEntry?.savedStateHandle?.set(resultKey, result)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}