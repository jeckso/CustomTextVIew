package com.example.textfield.presentation.attrs.root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.textfield.R
import com.example.textfield.presentation.base.util.*
import com.example.textfield.databinding.FragmentRootAttrBinding
import com.example.textfield.presentation.attrs.AttributesFragment
import com.example.textfield.presentation.attrs.progress.AttrProgressItem
import com.example.textfield.presentation.attrs.progress.AttrsProgressAdapter
import com.example.textfield.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RootAttrFragment constructor() : BaseFragment<RootAttrViewModel, FragmentRootAttrBinding>() {

    companion object {
        const val TAG = "RootAttrFragment"
        fun newInstance(): RootAttrFragment {
            return RootAttrFragment().apply {
            }
        }
    }

    private lateinit var attrsProgressAdapter: AttrsProgressAdapter


    override val viewModel: RootAttrViewModel by viewModels()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentRootAttrBinding {
        return FragmentRootAttrBinding.inflate(inflater, container, false)
    }

    override fun onResume() {
        attrsProgressAdapter = AttrsProgressAdapter(this)
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.stepAction.catch {
                Timber.e(it)
            }.collectLatest {
                if (it == RootAttrViewModel.NEXT_STEP) {
                    nextScreen()
                } else {
                    previousScreen()
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.setupVP()
    }

    private fun FragmentRootAttrBinding.setupVP() {
        attrsProgressAdapter = AttrsProgressAdapter(this@RootAttrFragment)
        attrsProgressAdapter.setupFragments()
        attrProgressVP.adapter = attrsProgressAdapter
        attrProgressVP.isUserInputEnabled = false;
    }


    private fun AttrsProgressAdapter.setupFragments() {
        tabs = listOf(
            AttrProgressItem(
                R.string.first_screen,
                AttributesFragment.newInstance("Input For Field 1")
            ),
            AttrProgressItem(
                R.string.second_screen,
                AttributesFragment.newInstance("Input For Field 2")
            ),
            AttrProgressItem(
                R.string.third_screen,
                AttributesFragment.newInstance("Input For Field 3")
            )
        )
    }

    fun nextScreen() {
        viewBinding.attrProgressVP.nextPage()
    }

    fun previousScreen() {
        viewBinding.attrProgressVP.previousPage()
    }

    fun ViewPager2.nextPage(smoothScroll: Boolean = true): Boolean {
        if ((currentItem + 1) < adapter?.itemCount ?: 0) {
            setCurrentItem(currentItem + 1, smoothScroll)
            return true
        }
        return false
    }

    fun ViewPager2.previousPage(smoothScroll: Boolean = true): Boolean {
        if ((currentItem - 1) >= 0) {
            setCurrentItem(currentItem - 1, smoothScroll)
            return true
        }
        return false
    }

}