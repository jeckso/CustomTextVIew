package com.example.textfield.presentation.attrs.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.textfield.data.model.IAttribute
import com.example.textfield.databinding.ItemAttributeBinding
import com.example.textfield.presentation.list.BaseRecyclerViewAdapter

class AttributesAdapter constructor(
) : BaseRecyclerViewAdapter<IAttribute, AttributesVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributesVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAttributeBinding.inflate(inflater, parent, false)
        return AttributesVH(binding)
    }

    override fun onBindViewHolder(holder: AttributesVH, position: Int) {
        holder.bind(items[position])
    }
}