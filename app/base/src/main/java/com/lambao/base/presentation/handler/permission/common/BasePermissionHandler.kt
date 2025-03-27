package com.lambao.base.presentation.handler.permission.common

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.lambao.base.R
import com.lambao.base.presentation.handler.dialog.DialogHandler

/**
 * An abstract base class for handling Android permissions with a consistent and reusable approach.
 * This class provides a foundation for requesting permissions, checking their status, and displaying
 * rationale dialogs when necessary. It leverages the [ActivityResultContracts.RequestMultiplePermissions]
 * API for modern permission handling and integrates with a [DialogHandler] for user interaction.
 *
 * Subclasses must provide a specific [permissionDescription] to customize the rationale message
 * displayed to the user.
 *
 * @param activity The [FragmentActivity] instance used to check permissions, launch requests,
 *                 and start intents. It provides the context for permission operations.
 * @param dialogHandler The [DialogHandler] instance responsible for showing rationale dialogs
 *                      when permissions require explanation or are permanently denied.
 */
abstract class BasePermissionHandler(
    protected val activity: FragmentActivity,
    private val dialogHandler: DialogHandler
) : PermissionHandler {

    /**
     * A description of the permission(s) being requested, to be provided by subclasses.
     * This string is used in rationale dialogs to explain why the permissions are needed.
     * For example, "camera access" or "storage access".
     */
    abstract val permissionDescription: String

    /**
     * Launcher for requesting multiple permissions using the Activity Result API.
     * It triggers the permission request and delivers the result to [currentCallback].
     */
    private val requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        currentCallback?.invoke(result)
        handlePermissionResult(result)
        currentCallback = null
    }

    /**
     * Temporary storage for the callback provided in [requestPermissions], invoked when the
     * permission request result is available.
     */
    private var currentCallback: ((Map<String, Boolean>) -> Unit)? = null

    /**
     * Requests the specified permissions from the user. If all permissions are already granted,
     * the [onResult] callback is invoked immediately with a map of permissions set to true.
     * Otherwise, it either shows a rationale dialog (if needed) or launches the permission request.
     *
     * @param permissions An array of permission strings (e.g., [Manifest.permission.CAMERA]).
     * @param onResult A callback invoked with a map of permissions and their grant status.
     */
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
            dialogHandler.showRationaleDialog(
                title = activity.getString(R.string.permission_required),
                permissionDescription = permissionDescription,
                positiveText = activity.getString(R.string.grant),
                negativeText = activity.getString(R.string.deny),
                onPositiveListener = { requestPermissionLauncher.launch(permissionsToRequest) },
                onNegativeListener = { onResult(permissionsToRequest.associateWith { false }) }
            )
        } else {
            requestPermissionLauncher.launch(permissionsToRequest)
        }
    }

    /**
     * Checks whether a specific permission is granted for the app.
     *
     * @param permission The permission string to check (e.g., [Manifest.permission.CAMERA]).
     * @return True if the permission is granted, false otherwise.
     */
    override fun checkPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

    /**
     * Determines whether a rationale should be shown for a specific permission, typically when
     * the user has previously denied it but not permanently.
     *
     * @param permission The permission string to evaluate (e.g., [Manifest.permission.CAMERA]).
     * @return True if a rationale should be shown, false otherwise.
     */
    override fun shouldShowRationale(permission: String): Boolean =
        ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

    /**
     * Handles the result of a permission request. If any permissions are denied and no rationale
     * should be shown (indicating permanent denial), it displays a dialog prompting the user
     * to enable the permissions in Settings.
     *
     * @param result A map of permissions and their grant status (true if granted, false if denied).
     */
    private fun handlePermissionResult(result: Map<String, Boolean>) {
        val deniedPermissions = result.filterValues { !it }.keys
        if (deniedPermissions.isNotEmpty() && deniedPermissions.none { shouldShowRationale(it) }) {
            dialogHandler.showRationaleDialog(
                title = activity.getString(R.string.permission_denied),
                permissionDescription = permissionDescription,
                positiveText = activity.getString(R.string.settings),
                negativeText = activity.getString(R.string.cancel),
                onPositiveListener = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", activity.packageName, null)
                    }
                    activity.startActivity(intent)
                }
            )
        }
    }
}