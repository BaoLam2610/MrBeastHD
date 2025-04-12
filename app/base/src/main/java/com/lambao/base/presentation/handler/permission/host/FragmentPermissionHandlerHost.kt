package com.lambao.base.presentation.handler.permission.host

import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

class FragmentPermissionHandlerHost(private val fragment: Fragment) : PermissionHandlerHost {
    override fun getContext(): Context = fragment.requireContext()

    override fun getLifecycleOwner(): LifecycleOwner = fragment.viewLifecycleOwner

    override fun getResultRegistry(): ActivityResultRegistry =
        fragment.requireActivity().activityResultRegistry

    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return fragment.shouldShowRequestPermissionRationale(permission)
    }

    override fun getPackageName(): String = fragment.requireActivity().packageName
}