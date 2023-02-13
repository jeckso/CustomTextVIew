package com.example.textfield.data.model

data class DoubleAttribute(
    override val name: String,
    override val value: Double? = null
) : IAttribute