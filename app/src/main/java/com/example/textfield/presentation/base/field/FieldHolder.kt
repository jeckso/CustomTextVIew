package com.example.textfield.presentation.base.field

import kotlinx.coroutines.flow.Flow

interface FieldHolder<T> {

    val state: Flow<Result<T>>
}