package com.jdy.android.fortube.base

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView

open class ItemBindingHolder(private var binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(bindingItem: ItemBindingAdapter.BindingItem, position: Int) {
        bindingItem.vm?.run {
            binding.setVariable(BR.vm, this)
        }
        binding.setVariable(BR.item, bindingItem.item)
        binding.setVariable(BR.position, position)
        binding.executePendingBindings()
    }
}