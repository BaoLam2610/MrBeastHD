package com.lambao.base.ui.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope

abstract class BaseBottomSheet<B : ViewDataBinding> : BottomSheetDialogFragment() {

    private var _binding: B? = null
    protected val binding: B
        get() = _binding
            ?: throw IllegalStateException("Binding in ${this::class.java.simpleName} is null")

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

    protected fun launchWhenCreated(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launchWhenCreated(block)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}