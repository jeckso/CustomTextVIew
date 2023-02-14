package com.example.textfield.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.textfield.presentation.base.util.BackState
import com.example.textfield.presentation.base.util.Failure
import com.example.textfield.presentation.base.util.NavigationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel : ViewModel() {

    private val _activeTasks = mutableSetOf<String>()

    private val _activeTaskMutex = Mutex()

    protected val _navigationState: MutableSharedFlow<NavigationState> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    protected val _errorState: MutableSharedFlow<Failure> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1,
    )
    protected val _shouldShowProgress: MutableSharedFlow<Boolean> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    val navigationState: Flow<NavigationState> = _navigationState

    val errorState: Flow<Failure> = _errorState

    val shouldShowProgress: Flow<Boolean> = _shouldShowProgress.transform {
        emit(it or _activeTasks.isNotEmpty())
    }.distinctUntilChanged()

    open fun navigateBack() {
        _navigationState.tryEmit(BackState())
    }

    fun proceedUi(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch {
            try {
                block()
            } catch (ex: Exception) {
                Timber.e(ex)
                _shouldShowProgress.emit(false)
                _errorState.tryEmit(Failure(error = ex))
            }
        }


    protected fun handleException(javaClassString: String?, message: String?, httpCode: Int) {
        val exception = when (javaClassString) {
            ConnectException::class.java.canonicalName -> ConnectException()
            SocketException::class.java.canonicalName -> SocketException()
            UnknownHostException::class.java.canonicalName -> UnknownHostException()
            else -> Exception(message)
        }
        _errorState.tryEmit(Failure(error = exception))
    }

}