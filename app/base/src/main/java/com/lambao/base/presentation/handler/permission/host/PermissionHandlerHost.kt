package com.lambao.base.presentation.handler.permission.host

import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.LifecycleOwner

interface PermissionHandlerHost {
    fun getContext(): Context
    fun getLifecycleOwner(): LifecycleOwner
    fun getResultRegistry(): ActivityResultRegistry
    fun shouldShowRequestPermissionRationale(permission: String): Boolean
    fun getPackageName(): String
}