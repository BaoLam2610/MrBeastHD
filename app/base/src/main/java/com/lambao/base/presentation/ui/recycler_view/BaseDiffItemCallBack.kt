package com.lambao.base.presentation.ui.recycler_view

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class BaseDiffItemCallBack<T : Any>(
    val diffItems: (T, T) -> Boolean,
    val diffContents: (T, T) -> Boolean
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return diffItems(oldItem, newItem)
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: T,
        newItem: T
    ): Boolean {
        return diffContents(oldItem, newItem)
    }
}