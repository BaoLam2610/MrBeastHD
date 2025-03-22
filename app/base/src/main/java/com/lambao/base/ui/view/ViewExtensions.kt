package com.lambao.base.ui.view

import android.view.View

fun View.click(func: (v: View) -> Unit) {
    setOnClickListener(
        object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                v?.let { func(it) }
            }
        }
    )
}