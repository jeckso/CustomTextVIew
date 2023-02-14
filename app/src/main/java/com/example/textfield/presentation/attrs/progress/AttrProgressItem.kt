package com.example.textfield.presentation.attrs.progress

import androidx.annotation.StringRes
import com.example.textfield.presentation.base.BaseFragment

data class AttrProgressItem constructor(
    @StringRes val titleRes: Int,
    val fragment: BaseFragment<*, *>,
)