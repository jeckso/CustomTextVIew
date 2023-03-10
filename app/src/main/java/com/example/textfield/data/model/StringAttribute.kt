package com.example.textfield.data.model

data class StringAttribute(
    override val name: String,
    override val id: String,
    override val value: String? = null
) : IAttribute