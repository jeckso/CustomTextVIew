package com.example.textfield.data.attributes.impl

import com.example.textfield.data.attributes.IAttributesRepository
import com.example.textfield.data.model.Attributes
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AttributesRepository @Inject constructor() : IAttributesRepository {
    @Provides
    override suspend fun getAttributes(): Result<Attributes> {
        return withContext(Dispatchers.IO) {
            val attributesList = listAttributes
            Result.success(Attributes(attributesList))
        }
    }
}