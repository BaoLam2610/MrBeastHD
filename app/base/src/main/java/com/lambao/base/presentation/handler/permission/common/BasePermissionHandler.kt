package com.lambao.base.presentation.handler.permission.common

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.lambao.base.presentation.handler.dialog.DialogHandler

abstract class BasePermissionHandler(
    private val activity: FragmentActivity,
    private val dialogHandler: DialogHandler,
    private val rationaleHandler: PermissionRationaleHandler
) : PermissionHandler {
    private val requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        currentCallback?.invoke(result)
        handlePermissionResult(result)
        currentCallback = null
    }
    private var currentCallback: ((Map<String, Boolean>) -> Unit)? = null

    override fun requestPermissions(
        permissions: Array<String>,
        onResult: (Map<String, Boolean>) -> Unit
    ) {
        val permissionsToRequest = permissions.filter { !checkPermission(it) }.toTypedArray()
        if (permissionsToRequest.isEmpty()) {
            onResult(permissions.associateWith { true })
            return
        }

        currentCallback = onResult
        val shouldShowRationale = permissionsToRequest.any { shouldShowRationale(it) }
        if (shouldShowRationale) {
            rationaleHandler.showRationale("this feature") {
                requestPermissionLauncher.launch(permissionsToRequest)
            }
        } else {
            requestPermissionLauncher.launch(permissionsToRequest)
        }
    }

    override fun checkPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

    override fun shouldShowRationale(permission: String): Boolean =
        ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

    private fun handlePermissionResult(result: Map<String, Boolean>) {
        val deniedPermissions = result.filterValues { !it }.keys
        if (deniedPermissions.isNotEmpty() && deniedPermissions.none { shouldShowRationale(it) }) {
            dialogHandler.showMessageDialog("this feature")
            /*AlertDialog.Builder(activity)
                .setTitle("Permission Denied")
                .setMessage("Please enable permissions in Settings.")
                .setPositiveButton("Go to Settings") { _, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", activity.packageName, null)
                    }
                    activity.startActivity(intent)
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .show()*/
        }
    }
}