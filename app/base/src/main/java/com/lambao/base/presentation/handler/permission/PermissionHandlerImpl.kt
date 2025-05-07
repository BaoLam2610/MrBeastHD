package com.lambao.base.presentation.handler.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.lambao.base.R
import com.lambao.base.presentation.handler.dialog.DialogHandler

class PermissionHandlerImpl(
    private val settingsLauncher: ActivityResultLauncher<Intent>,
    private val requestPermissionLauncher: ActivityResultLauncher<Array<String>>,
    private val dialogHandler: DialogHandler,
    private val activity: FragmentActivity
) : PermissionHandler {

    private val permissions = mutableListOf<String>()
    private var onPermissionResult: ((PermissionResult) -> Unit)? = null
    private var settingsMessage: String =
        activity.getString(R.string.required_permissions_have_been_denied_please_enable_in_app_settings)

    override fun getPermissions() = permissions

    override fun requestPermission(
        permission: String,
        settingsMessage: String?,
        onResult: ((PermissionResult) -> Unit)?
    ) {
        if (permission.isEmpty()) {
            return
        }
        requestPermissions(listOf(permission), settingsMessage, onResult)
    }

    override fun requestPermissions(
        permissions: List<String>,
        settingsMessage: String?,
        onResult: ((PermissionResult) -> Unit)?
    ) {
        addPermissions(permissions)

        if (permissions.isEmpty()) {
            onResult?.invoke(PermissionResult.Granted(emptyList()))
            return
        }

        onPermissionResult = onResult
        settingsMessage?.let { this.settingsMessage = it }

        val notGrantedPermissions = permissions.filter { !checkPermissionGranted(it) }

        if (notGrantedPermissions.isEmpty()) {
            onResult?.invoke(PermissionResult.Granted(permissions))
            return
        }

        launchPermissionRequest(notGrantedPermissions)

        /*
        // Remove comment if you want to show rationale dialog
        val rationalePermissions = notGrantedPermissions.filter {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }

        if (rationalePermissions.isNotEmpty()) {
            dialogHandler.showAlertDialog(
                title = "Yêu cầu quyền",
                message = "Ứng dụng cần các quyền sau để hoạt động: ${rationalePermissions.joinToString()}.",
                positiveText = "Cấp quyền",
                negativeText = "Hủy",
                onPositiveListener = { launchPermissionRequest(notGrantedPermissions) }
            )
        } else {
            launchPermissionRequest(notGrantedPermissions)
        }*/
    }

    private fun addPermissions(permissions: List<String>) {
        this.permissions.clear()
        this.permissions.addAll(permissions)
    }

    private fun launchPermissionRequest(permissions: List<String>) {
        requestPermissionLauncher.launch(permissions.toTypedArray())
    }

    override fun promptOpenSettings() {
        dialogHandler.showAlertDialog(
            message = settingsMessage,
            positiveText = activity.getString(R.string.settings),
            negativeText = activity.getString(R.string.cancel),
            onPositiveListener = { openAppSettings() }
        )
    }

    private fun openAppSettings() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", activity.packageName, null)
            }
            settingsLauncher.launch(intent)
        } catch (e: Exception) {
            dialogHandler.showAlertDialog(activity.getString(R.string.cannot_open_app_settings_please_open_manual))
        }
    }

    override fun checkPermissionGranted(permission: String): Boolean {
        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun isPermissionsGranted(): Boolean {
        return permissions.all { checkPermissionGranted(it) }
    }

    override fun onPermissionResult(result: PermissionResult) {
        onPermissionResult?.invoke(result)
    }
}