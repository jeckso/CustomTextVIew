package com.example.textfield.presentation.attrs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.textfield.databinding.FragmentInputBinding
import com.example.textfield.presentation.attrs.list.AttributesAdapter
import com.example.textfield.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttributesFragment : BaseFragment<AttributesVM, FragmentInputBinding>() {


    companion object {
        const val TAG = "AttributesFragment"
        fun newInstance(): AttributesFragment {
            return AttributesFragment()
        }
    }

    private val adapter = AttributesAdapter()

    override val viewModel: AttributesVM by viewModels()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentInputBinding {
        return FragmentInputBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.attrs.asObserverJob {
            adapter.items = it?.list?: listOf()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.setupRecyclerView()

    }

    private fun FragmentInputBinding.setupRecyclerView() {
        rv.adapter = adapter
    }

}