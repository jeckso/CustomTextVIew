package com.example.textfield.android

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import com.example.textfield.R
import com.example.textfield.data.model.Attributes

class CustomTextField(context: Context, attrs: AttributeSet, listAttributes: Attributes): androidx.appcompat.widget.AppCompatEditText(context, attrs) {

    companion object {
        private val STYLE_ATTR = R.styleable.CustomTextField
    }

}