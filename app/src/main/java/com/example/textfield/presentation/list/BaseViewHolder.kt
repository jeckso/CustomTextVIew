package com.example.textfield.presentation.list

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T: Any, VB : ViewBinding>(val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {

    open fun bind(item: T) {
    }

    open fun unbind() {
    }
}