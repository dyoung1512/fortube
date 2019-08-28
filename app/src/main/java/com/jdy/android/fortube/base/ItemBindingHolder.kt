package com.jdy.android.fortube.base

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView

open class ItemBindingHolder(private var binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Item) {
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}