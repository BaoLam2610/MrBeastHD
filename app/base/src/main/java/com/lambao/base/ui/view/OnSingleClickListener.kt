package com.lambao.base.ui.view

import android.os.SystemClock
import android.view.View

abstract class OnSingleClickListener : View.OnClickListener {

    companion object {
        private const val DELAY_TIME_SINGLE_CLICK = 500
    }

    private var mLastClickTime: Long = 0

    abstract fun onSingleClick(v: View?)
    
    override fun onClick(v: View) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime
        mLastClickTime = currentClickTime
        if (elapsedTime <= DELAY_TIME_SINGLE_CLICK) return
        onSingleClick(v)
    }
}