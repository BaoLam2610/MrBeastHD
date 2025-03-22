package com.lambao.base.ui.recycler_view

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseRecyclerViewHolder<B : ViewDataBinding>(
    val binding: B,
) : RecyclerView.ViewHolder(binding.root)