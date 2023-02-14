package com.example.textfield.presentation.attrs

import com.example.textfield.data.attributes.impl.AttributesRepository
import com.example.textfield.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class AttributesVM @Inject constructor(private val attributesRepository: AttributesRepository) :
    BaseViewModel() {

    val attrs = flow{
       val attrs = attributesRepository.getAttributes().getOrNull()
        emit(attrs)
    }
}