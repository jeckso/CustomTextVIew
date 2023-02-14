package com.example.textfield.presentation.base.field

interface MutableFieldHolder<T : Any> : FieldHolder<T> {

    fun setValue(value: T?)
}