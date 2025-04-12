package com.lambao.base.presentation.handler.permission.host

import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner

class ActivityPermissionHandlerHost(private val activity: FragmentActivity) :
    PermissionHandlerHost {
    override fun getContext(): Context = activity

    override fun getLifecycleOwner(): LifecycleOwner = activity

    override fun getResultRegistry(): ActivityResultRegistry = activity.activityResultRegistry

    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    override fun getPackageName(): String = activity.packageName
}