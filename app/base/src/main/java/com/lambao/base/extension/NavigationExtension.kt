package com.lambao.base.extension

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController


fun NavController.navigateSafe(directions: NavDirections, navOptions: NavOptions? = null) {
    try {
        navigate(directions, navOptions)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    }
}

fun Fragment.navigate(directions: NavDirections, navOptions: NavOptions? = null) {
    findNavController().navigateSafe(directions, navOptions)
}

fun Fragment.navigateWithArgs(actionId: Int, args: Bundle? = null) {
    findNavController().navigate(actionId, args)
}

fun Fragment.popBackStack() {
    findNavController().popBackStack()
}

fun Fragment.canGoBack(): Boolean {
    return findNavController().previousBackStackEntry != null
}

fun Fragment.getNavControllerSafely(): NavController? {
    return try {
        findNavController()
    } catch (e: IllegalStateException) {
        null
    }
}

inline fun <reified T : Fragment> T.withArgs(builder: Bundle.() -> Unit): T {
    arguments = Bundle().apply(builder)
    return this
}