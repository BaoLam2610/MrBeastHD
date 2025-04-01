package com.lambao.base.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController


fun Fragment.safeNavigate(
    @IdRes id: Int,
    navOptions: NavOptions? = null,
    args: Bundle? = null,
) {
    val navController = findNavController()
    val currentDestinationClassName =
        (navController.currentDestination as FragmentNavigator.Destination).className

    if (currentDestinationClassName == this::class.java.name) {
        navController.navigate(id, args, navOptions)
    }
}

/**
 * Safely navigates using the provided NavDirections, catching any IllegalArgumentException.
 *
 * @param directions The navigation directions to follow
 * @param navOptions Optional navigation options to customize the transition
 */
fun NavController.navigateSafe(directions: NavDirections, navOptions: NavOptions? = null) {
    try {
        navigate(directions, navOptions)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    }
}

/**
 * Performs a safe navigation from a Fragment using the provided directions.
 *
 * @param directions The navigation directions to follow
 * @param navOptions Optional navigation options to customize the transition
 */
fun Fragment.navigate(directions: NavDirections, navOptions: NavOptions? = null) {
    findNavController().navigateSafe(directions, navOptions)
}

/**
 * Navigates from a Fragment using an action ID and optional arguments.
 *
 * @param actionId The ID of the navigation action to perform
 * @param args Optional Bundle containing navigation arguments
 */
fun Fragment.navigateWithArgs(actionId: Int, args: Bundle? = null) {
    findNavController().navigate(actionId, args)
}

/**
 * Pops the back stack from the current Fragment's NavController.
 * Moves to the previous destination in the navigation stack.
 */
fun Fragment.popBackStack() {
    findNavController().popBackStack()
}

/**
 * Checks if there is a previous destination in the back stack that can be navigated to.
 *
 * @return true if there is a previous destination, false otherwise
 */
fun Fragment.canGoBack(): Boolean {
    return findNavController().previousBackStackEntry != null
}

/**
 * Safely retrieves the NavController for the Fragment, returning null if not available.
 *
 * @return The NavController if found, null if an IllegalStateException occurs
 */
fun Fragment.getNavControllerSafely(): NavController? {
    return try {
        findNavController()
    } catch (e: IllegalStateException) {
        null
    }
}

/**
 * Creates a Fragment instance with arguments configured through a builder function.
 *
 * @param T The type of Fragment to create
 * @param builder Lambda function to configure the Bundle of arguments
 * @return The Fragment instance with configured arguments
 */
inline fun <reified T : Fragment> T.withArgs(builder: Bundle.() -> Unit): T {
    arguments = Bundle().apply(builder)
    return this
}