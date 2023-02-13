package com.example.textfield.data.attributes.impl

import com.example.textfield.data.attributes.IAttributesRepository
import com.example.textfield.data.model.Attributes

class AttributesRepository: IAttributesRepository {
    override suspend fun getAttributes(): Result<Attributes> {
        TODO("Not yet implemented")
    }
}