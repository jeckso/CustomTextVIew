package com.example.textfield.presentation.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.withStarted
import androidx.viewbinding.ViewBinding
import com.example.textfield.presentation.base.field.MutableFieldHolder
import com.example.textfield.presentation.base.util.BackState
import com.example.textfield.presentation.base.util.Failure
import com.example.textfield.presentation.base.util.NavigationState
import com.example.textfield.presentation.base.util.NextScreenState
import com.example.textfield.presentation.base.util.RequestPermissionsState
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment() {

    companion object {
        const val NO_ITEMS = 0
        const val NO_ITEMS_ROOT_READING = 1
    }

    protected abstract val viewModel: VM
    protected lateinit var viewBinding: VB

    private var baseSubscriptionJobs: Job? = null
    private var baseActivity: BaseActivity<*, *>? = null

    protected val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.navigateBack()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = requireActivity() as? BaseActivity<*, *>
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseSubscriptionJobs = lifecycleScope.launch {
            viewModel.navigationState
                .onEach { withStarted { onNavigationChanged(it) } }
                .launchIn(this)
            viewModel.errorState
                .onEach { withStarted { handleErrorMessage(it) } }
                .launchIn(this)
            viewModel.shouldShowProgress
                .onEach { withStarted { shouldShowProgress(it) } }
                .launchIn(this)
        }
    }

    open fun onNightModeChanged(mode: Int) {
        if (isAdded) {
            childFragmentManager.fragments.filterIsInstance<BaseFragment<*, *>>().forEach {
                it.onNightModeChanged(mode)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = menu.clear()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = inflateViewBinding(inflater, container, savedInstanceState)
        return viewBinding.root
    }

    private var job: Job? = null

    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
    }

    override fun onDestroy() {
        baseSubscriptionJobs?.cancel()
        baseSubscriptionJobs = null
        super.onDestroy()
    }

    open fun handleError(error: Throwable) {
        baseActivity?.handleError(error)
    }

    open fun handlePermissionRequestState(requestPermissionsState: RequestPermissionsState) = Unit

    open fun showErrorDialog(message: String) {
        baseActivity?.showErrorDialog(message)
    }

    open fun showInfoDialog(title: String, message: String) {
        baseActivity?.showInfoDialog(title, message)
    }

    override fun onResume() {
        super.onResume()
        onBackPressedCallback.isEnabled = true
    }

    override fun onPause() {
        onBackPressedCallback.isEnabled = false
        super.onPause()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        onBackPressedCallback.isEnabled = !hidden
        childFragmentManager.fragments.forEach { it.onHiddenChanged(hidden) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        childFragmentManager.fragments.forEach { it.onActivityResult(requestCode, resultCode, data) }
    }

    protected open fun shouldShowProgress(isVisible: Boolean) {
    }

    protected open fun onNavigationChanged(navigation: NavigationState) = when (navigation) {
        is BackState -> handleBackNavigation(navigation)
        is NextScreenState -> handleCustomNavigation(navigation)
        is RequestPermissionsState -> handlePermissionRequestState(navigation)
    }

    protected abstract fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): VB

    open fun handleBackNavigation(state: BackState) {
        val count = parentFragmentManager.backStackEntryCount
        if (count > NO_ITEMS) {
            parentFragmentManager.popBackStack()
        } else {
            requireActivity().finish()
        }
    }

    protected open fun handleCustomNavigation(state: NextScreenState) {
        baseActivity?.handleCustomNavigation(state)
    }

    protected fun subscribe(
        editText: TextInputEditText,
        fieldHolder: MutableFieldHolder<String>,
    ): TextWatcher {
        return editText.doAfterTextChanged {
            fieldHolder.setValue(it?.toString())
        }
    }

    protected fun <T> Flow<T>.asObserverJob(
        rootJob: Job? = null,
        state: Lifecycle.State = Lifecycle.State.STARTED,
        block: suspend (T) -> Unit
    ): Job {
        val block: suspend (CoroutineScope.() -> Unit) = {
            repeatOnLifecycle(state) {
                collect(block)
            }
        }
        return when (rootJob) {
            null -> lifecycleScope.launch(block = block)
            else -> lifecycleScope.launch(rootJob, block = block)
        }
    }

    protected fun handleErrorMessage(failure: Failure) {
        when {
            failure.error != null -> handleError(failure.error)
            failure.message != null -> showErrorDialog(failure.message)
        }
    }
}