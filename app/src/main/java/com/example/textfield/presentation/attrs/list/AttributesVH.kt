package com.example.textfield.presentation.attrs.list

import androidx.recyclerview.widget.RecyclerView
import com.example.textfield.data.model.IAttribute
import com.example.textfield.databinding.ItemAttributeBinding
import com.example.textfield.presentation.list.BaseViewHolder

class AttributesVH(binding: ItemAttributeBinding) :
    BaseViewHolder<IAttribute, ItemAttributeBinding>(binding) {


    override fun bind(item: IAttribute) {
        with(binding) {

            title.text = item.name

        }
    }
}