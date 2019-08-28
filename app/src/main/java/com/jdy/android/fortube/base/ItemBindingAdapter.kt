package com.jdy.android.fortube.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class ItemBindingAdapter: RecyclerView.Adapter<ItemBindingHolder>() {
    open class BindingItem(val item: Item, val layout: Int)

    private val items = ArrayList<BindingItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBindingHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), viewType, parent, false)
        return ItemBindingHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemBindingHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setItems(items: List<BindingItem>) {
        this.items.clear()
        this.items.addAll(items)
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = items[position].layout

    private fun getItem(position: Int): Item = items[position].item
}