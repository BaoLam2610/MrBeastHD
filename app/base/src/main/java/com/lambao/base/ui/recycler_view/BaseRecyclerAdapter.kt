package com.lambao.base.ui.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.lambao.base.extension.click

abstract class BaseRecyclerAdapter<T, B : ViewDataBinding>(
    private val onItemClickListener: ((item: T, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<BaseRecyclerViewHolder<B>>() {

    protected val items = mutableListOf<T>()

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
        bind(holder.binding, items[position], position)
        onItemClickListener?.let { listener ->
            holder.itemView.click {
                listener.invoke(items[position], position)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}