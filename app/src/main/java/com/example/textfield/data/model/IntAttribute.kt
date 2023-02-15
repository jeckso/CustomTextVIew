package com.example.textfield.data.model

data class IntAttribute(
    override val name: String,
    override val id: String,
    override val value: Int? = null
) : IAttribute