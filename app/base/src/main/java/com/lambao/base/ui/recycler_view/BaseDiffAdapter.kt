package com.lambao.base.ui.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.lambao.base.ui.view.click

abstract class BaseDiffAdapter<T : Any, B : ViewDataBinding>(
    areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    private val onItemClickListener: ((item: T, position: Int) -> Unit)? = null
) : ListAdapter<T, BaseRecyclerViewHolder<B>>(
    BaseDiffItemCallBack<T>(
        diffItems = areItemsTheSame,
        diffContents = areContentsTheSame
    )
) {

    @LayoutRes
    protected abstract fun getLayoutId(viewType: Int): Int

    protected abstract fun bind(binding: B, item: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<B> {
        val binding: B = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getLayoutId(viewType),
            parent,
            false
        )
        return BaseRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<B>, position: Int) {
        bind(holder.binding, getItem(position), position)
        onItemClickListener?.let { listener ->
            holder.itemView.click {
                listener.invoke(getItem(position), position)
            }
        }
    }
}
