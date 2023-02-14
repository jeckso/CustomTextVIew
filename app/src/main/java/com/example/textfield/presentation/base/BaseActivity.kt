package com.example.textfield.presentation.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.withStarted
import androidx.viewbinding.ViewBinding
import com.example.textfield.presentation.base.util.BackState
import com.example.textfield.presentation.base.util.Failure
import com.example.textfield.presentation.base.util.NavigationState
import com.example.textfield.presentation.base.util.NextScreenState
import com.example.textfield.presentation.base.util.RequestPermissionsState
import com.example.textfield.presentation.base.util.toBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {


    abstract val viewModel: VM

    protected lateinit var viewBinding: VB
        private set

    private var baseSubscriptionJobs: Job? = null

    abstract fun inflateViewBinding(savedInstanceState: Bundle?): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = inflateViewBinding(savedInstanceState)
        setContentView(viewBinding.root)
        baseSubscriptionJobs = lifecycleScope.launch {
            viewModel.navigationState
                .onEach { withStarted { onNavigationChanged(it) } }
                .launchIn(this)
            viewModel.errorState
                .onEach { withStarted { handleErrorMessage(it) } }
                .launchIn(this)
        }
    }

    override fun onDestroy() {
        baseSubscriptionJobs?.cancel()
        baseSubscriptionJobs = null
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.fragments.forEach {
            it.onActivityResult(
                requestCode,
                resultCode,
                data
            )
        }
    }

    open fun handleBackNavigation(state: BackState) = with(state) {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            setResult(code, Intent().putExtras(toBundle()))
            finish()
        }
    }

    protected fun <T> Flow<T>.asObserverJob(
        rootJob: Job? = null,
        state: Lifecycle.State = Lifecycle.State.STARTED,
        block: suspend (T) -> Unit
    ): Job {
        val block: suspend (CoroutineScope.() -> Unit) = {
            repeatOnLifecycle(state) {
                collect { block(it) }

            }
        }
        return when (rootJob) {
            null -> lifecycleScope.launch(block = block)
            else -> lifecycleScope.launch(rootJob, block = block)
        }
    }

    open fun handleCustomNavigation(state: NextScreenState) {
        when (state) {
            else -> IllegalStateException("Unhandled state $state")
        }
    }

    open fun handlePermissionRequestState(requestPermissionsState: RequestPermissionsState) = Unit

    open fun handleError(error: Throwable) {

    }

    open fun showErrorDialog(message: String) {

    }

    open fun showInfoDialog(title: String, message: String) {

    }

    override fun onNightModeChanged(mode: Int) {
        super.onNightModeChanged(mode)
        supportFragmentManager.fragments.filterIsInstance<BaseFragment<*, *>>().forEach {
            it.onNightModeChanged(mode)
        }
    }

    protected open fun onNavigationChanged(navigation: NavigationState) = when (navigation) {
        is BackState -> handleBackNavigation(navigation)
        is NextScreenState -> handleCustomNavigation(navigation)
        is RequestPermissionsState -> handlePermissionRequestState(navigation)
    }

    private fun handleErrorMessage(failure: Failure) {
        when {
            failure.error != null -> handleError(failure.error)
            failure.message != null -> showErrorDialog(failure.message)
        }
    }
}
