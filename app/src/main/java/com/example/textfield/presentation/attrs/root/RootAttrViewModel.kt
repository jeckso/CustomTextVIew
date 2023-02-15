package com.example.textfield.presentation.attrs.root

import com.example.textfield.data.attributes.impl.AttributesRepository
import com.example.textfield.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class RootAttrViewModel @Inject constructor(private val attributesRepository: AttributesRepository) :
    BaseViewModel() {

    companion object {
        const val NEXT_STEP = 1
    }

    private val _stepAction = MutableSharedFlow<Int>()

    val stepAction: Flow<Int> = _stepAction.filterNotNull()


    fun navigateNext() = proceedUi {
        _stepAction.emit(NEXT_STEP)
    }

    val attrs = flow {
        val attrs = attributesRepository.getAttributes().getOrNull()
        val list = listOf(attrs, attrs, attrs)
        emit(list)
    }


}