package com.lambao.base.ui.recycler_view

import androidx.databinding.ViewDataBinding

abstract class BaseMultiRecyclerAdapter<T>(
    onItemClickListener: ((item: T, position: Int) -> Unit)? = null
) : BaseRecyclerAdapter<T, ViewDataBinding>(onItemClickListener) {
    protected abstract fun getViewType(item: T, position: Int): Int

    override fun getItemViewType(position: Int): Int {
        return getViewType(items[position], position)
    }
}