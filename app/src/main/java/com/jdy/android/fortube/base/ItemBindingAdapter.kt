package com.jdy.android.fortube.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView

open class ItemBindingAdapter: RecyclerView.Adapter<ItemBindingHolder>() {
    open class BindingItem(val item: Item, val layout: Int, @Nullable var vm: ViewModel? = null)

    val items = ArrayList<BindingItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBindingHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), viewType, parent, false)
        return ItemBindingHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemBindingHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    fun setItems(items: List<BindingItem>) {
        this.items.clear()
        this.items.addAll(items)
    }

    fun setItems(items: List<Item>, layoutRes: Int) {
        setItems(items, layoutRes, null)
    }

    fun setItems(items: List<Item>, layoutRes: Int, @Nullable vm: ViewModel?) {
        this.items.clear()
        for (item in items) {
            this.items.add(BindingItem(item, layoutRes, vm))
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = items[position].layout

    private fun getItem(position: Int): BindingItem = items[position]
}