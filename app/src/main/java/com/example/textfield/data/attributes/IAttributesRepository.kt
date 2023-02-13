package com.example.textfield.data.attributes

import com.example.textfield.data.model.Attributes

interface IAttributesRepository {
    suspend fun getAttributes(): Result<Attributes>
}