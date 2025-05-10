package com.lambao.base.presentation.ui.bottom_sheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lambao.base.data.remote.NetworkException
import com.lambao.base.presentation.handler.dialog.DialogHandler
import com.lambao.base.presentation.handler.dialog.DialogHandlerImpl
import com.lambao.base.presentation.handler.loading.LoadingDialogHandler
import com.lambao.base.presentation.handler.loading.LoadingHandler
import com.lambao.base.presentation.handler.media.CustomPickMultipleVisualMedia
import com.lambao.base.presentation.handler.media.MediaPickerHandler
import com.lambao.base.presentation.handler.media.MediaPickerHandlerImpl
import com.lambao.base.presentation.handler.media.MediaPickerResult
import com.lambao.base.presentation.handler.network_error.NetworkErrorHandler
import com.lambao.base.presentation.handler.network_error.NetworkErrorHandlerImpl
import com.lambao.base.presentation.handler.permission.PermissionHandler
import com.lambao.base.presentation.handler.permission.PermissionHandlerImpl
import com.lambao.base.presentation.handler.permission.PermissionResult

abstract class BaseBottomSheet<B : ViewDataBinding> : BottomSheetDialogFragment() {

    private var _binding: B? = null
    protected val binding: B
        get() = _binding
            ?: throw IllegalStateException("Binding in ${this::class.java.simpleName} is null")

    private val settingsLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (permissionHandler.isPermissionsGranted()) {
                permissionHandler.onPermissionResult(
                    PermissionResult.Granted(permissionHandler.getPermissions())
                )
            }
        }

    private val requestPermissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            val grantedPermissions = permissionsResult.filter { it.value }.keys.toList()
            val deniedPermissions = permissionsResult.filter { !it.value }.keys.toList()
            if (deniedPermissions.isEmpty()) {
                permissionHandler.onPermissionResult(PermissionResult.Granted(grantedPermissions))
            } else {
                val permanentlyDenied = deniedPermissions.any {
                    !ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        it
                    )
                }
                if (permanentlyDenied) {
                    permissionHandler.promptOpenSettings()
                }
                permissionHandler.onPermissionResult(
                    PermissionResult.Denied(deniedPermissions, permanentlyDenied)
                )
            }
        }

    private val customMultipleMediaContract = CustomPickMultipleVisualMedia()

    private val mediaLauncher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                mediaPickerHandler.onResult(MediaPickerResult.Success(listOf(uri)))
            }
        }

    private val multipleMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(customMultipleMediaContract) { uris ->
            if (!uris.isNullOrEmpty()) {
                mediaPickerHandler.onResult(MediaPickerResult.Success(uris))
            }
        }

    protected open val dialogHandler: DialogHandler by lazy {
        DialogHandlerImpl(requireActivity())
    }

    protected open val loadingHandler: LoadingHandler by lazy {
        LoadingDialogHandler(requireActivity())
    }

    protected open val networkErrorHandler: NetworkErrorHandler by lazy {
        NetworkErrorHandlerImpl(
            requireContext(),
            dialogHandler
        )
    }

    protected val permissionHandler: PermissionHandler by lazy {
        PermissionHandlerImpl(
            settingsLauncher,
            requestPermissionLauncher,
            dialogHandler,
            requireActivity()
        )
    }

    protected val mediaPickerHandler: MediaPickerHandler by lazy {
        MediaPickerHandlerImpl(
            mediaLauncher,
            multipleMediaLauncher,
            dialogHandler,
            customMultipleMediaContract,
            requireActivity()
        )
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun onViewReady(savedInstance: Bundle?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewReady(savedInstanceState)
    }

    protected fun setFull(isFull: Boolean) {
        dialog?.let { bottomSheetDialog ->
            val bottomSheet: FrameLayout? =
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { view ->
                val behavior = BottomSheetBehavior.from(view)
                if (isFull) {
                    view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    behavior.peekHeight = resources.displayMetrics.heightPixels
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                } else {
                    view.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                view.requestLayout()
            }
        }
    }

    fun showLoading() {
        loadingHandler.showLoading()
    }

    fun hideLoading() {
        loadingHandler.hideLoading()
    }

    fun handleNetworkError(networkException: NetworkException) {
        networkErrorHandler.handleError(networkException)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideLoading()
        _binding = null
    }
}