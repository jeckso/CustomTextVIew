package com.example.textfield.data.model

data class BooleanAttribute(
    override val name: String,
    override val id: String,
    override val value: Boolean? = null
) : IAttribute