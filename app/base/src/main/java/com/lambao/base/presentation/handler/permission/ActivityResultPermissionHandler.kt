package com.lambao.base.presentation.handler.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.lambao.base.presentation.handler.dialog.DialogHandler
import com.lambao.base.presentation.handler.permission.host.PermissionHandlerHost


class ActivityResultPermissionHandler(
    private val host: PermissionHandlerHost,
    private val dialogHandler: DialogHandler
) : PermissionContract, DefaultLifecycleObserver {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var onPermissionResult: ((PermissionResult) -> Unit)? = null
    private var customSettingsMessage: String =
        "Required permissions have been denied. Please enable them in app settings to use this feature."

    init {
        // Register as lifecycle observer to ensure proper setup
        host.getLifecycleOwner().lifecycle.addObserver(this)

        // Check if lifecycle is already beyond INITIALIZED and register immediately if needed
        if (host.getLifecycleOwner().lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED) &&
            !host.getLifecycleOwner().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
        ) {
            registerPermissionLauncher()
        }
    }

    private fun registerPermissionLauncher() {
        permissionLauncher = host.getResultRegistry().register(
            "permission_request_${System.currentTimeMillis()}",
            host.getLifecycleOwner(),
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsResult ->
            val grantedPermissions = permissionsResult.filter { it.value }.keys.toList()
            val deniedPermissions = permissionsResult.filter { !it.value }.keys.toList()

            if (deniedPermissions.isEmpty()) {
                onPermissionResult?.invoke(PermissionResult.Granted(grantedPermissions))
            } else {
                val permanentlyDenied = deniedPermissions.any { permission ->
                    !host.shouldShowRequestPermissionRationale(permission)
                }

                if (permanentlyDenied) {
                    promptOpenSettings()
                }

                onPermissionResult?.invoke(
                    PermissionResult.Denied(
                        deniedPermissions,
                        permanentlyDenied
                    )
                )
            }
        }
    }

    override fun requestPermission(
        permission: String,
        settingsMessage: String?,
        onResult: ((PermissionResult) -> Unit)?
    ) {
        requestPermissions(
            listOf(permission),
            settingsMessage,
            onResult
        )
    }

    override fun requestPermissions(
        permissions: List<String>,
        settingsMessage: String?,
        onResult: ((PermissionResult) -> Unit)?
    ) {
        if (permissions.isEmpty()) {
            dialogHandler.showAlertDialog("At least one permission must be provided")
            return
        }

        onPermissionResult = onResult
        settingsMessage?.let { customSettingsMessage = it }

        val notGrantedPermissions = permissions.filter {
            !checkPermission(it)
        }

        if (notGrantedPermissions.isEmpty()) {
            onResult?.invoke(PermissionResult.Granted(permissions))
            return
        }

        // Check if launcher is initialized before using
        if (!this::permissionLauncher.isInitialized) {
            dialogHandler.showAlertDialog("Permission handler not properly initialized")
            onResult?.invoke(PermissionResult.Denied(notGrantedPermissions, false))
            return
        }

        // Skip rationale dialog and directly request permissions
        launchPermissionRequest(notGrantedPermissions)
    }

    private fun launchPermissionRequest(permissions: List<String>) {
        permissionLauncher.launch(permissions.toTypedArray())
    }

    private fun promptOpenSettings() {
        dialogHandler.showAlertDialog(
            title = "",
            message = customSettingsMessage,
            positiveText = "Settings",
            onPositiveListener = { openAppSettings() },
            negativeText = "Cancel"
        )
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", host.getPackageName(), null)
        }
        host.getContext().startActivity(intent)
    }

    override fun checkPermission(permission: String): Boolean {
        return host.getContext().checkSelfPermission(permission) ==
                PackageManager.PERMISSION_GRANTED
    }

    // Clean up when no longer needed
    override fun onDestroy(owner: LifecycleOwner) {
        host.getLifecycleOwner().lifecycle.removeObserver(this)
    }
}