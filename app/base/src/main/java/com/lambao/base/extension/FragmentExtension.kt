package com.lambao.base.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch


/**
 * Performs a safe navigation from a Fragment using the provided directions.
 *
 * @param actionId The ID of the navigation action to perform
 * @param navOptions Optional navigation options to customize the transition
 * @param args Optional Bundle containing navigation arguments
 */
fun Fragment.navigate(
    @IdRes actionId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
) {
    try {
        val navController = findNavController()
        val currentDestination = navController.currentDestination
        if (currentDestination is FragmentNavigator.Destination &&
            currentDestination.className == this::class.java.name
        ) {
            navController.navigate(actionId, args, navOptions)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Pops the back stack from the current Fragment's NavController.
 * Moves to the previous destination in the navigation stack if possible.
 *
 * @return true if the back stack was popped successfully, false otherwise
 */
fun Fragment.popBackStack(): Boolean {
    return try {
        findNavController().popBackStack()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * Checks if there is a previous destination in the back stack that can be navigated to.
 *
 * @return true if there is a previous destination, false otherwise
 */
fun Fragment.canGoBack(): Boolean {
    return try {
        findNavController().previousBackStackEntry != null
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * Gets the current Fragment instance from the NavController.
 *
 * @return The current Fragment if available, null otherwise
 */
fun Fragment.getCurrentFragment(): Fragment? {
    return try {
        val navController = findNavController()
        val currentBackStackEntry = navController.currentBackStackEntry
        currentBackStackEntry?.let { entry ->
            (parentFragmentManager.findFragmentByTag(entry.id) as? NavHostFragment)
                ?.childFragmentManager?.fragments?.firstOrNull()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Navigates to another Fragment and listens for a result returned from it using StateFlow.
 *
 * @param actionId The ID of the navigation action to perform
 * @param args Optional Bundle containing navigation arguments
 * @param navOptions Optional navigation options to customize the transition
 * @param resultKey The key to identify the result data
 * @param onResult Callback to handle the result when the destination Fragment returns data
 */
fun <T> Fragment.navigateForResult(
    @IdRes actionId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    resultKey: String,
    onResult: (T) -> Unit
) {
    try {
        val navController = findNavController()
        val currentDestination = navController.currentDestination
        if (currentDestination is FragmentNavigator.Destination &&
            currentDestination.className == this::class.java.name) {
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
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Sets a result to be returned to the previous Fragment before popping the back stack.
 *
 * @param resultKey The key to identify the result data
 * @param result The result data to return
 */
fun <T> Fragment.setNavigationResult(resultKey: String, result: T) {
    try {
        val navController = findNavController()
        navController.previousBackStackEntry?.savedStateHandle?.set(resultKey, result)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}