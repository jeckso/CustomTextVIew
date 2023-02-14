package com.example.textfield.presentation.attrs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.textfield.databinding.FragmentInputBinding
import com.example.textfield.presentation.attrs.list.AttributesAdapter
import com.example.textfield.presentation.attrs.root.RootAttrViewModel
import com.example.textfield.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttributesFragment : BaseFragment<RootAttrViewModel, FragmentInputBinding>() {


    companion object {
        const val TAG = "AttributesFragment"
        const val TITLE_TAG = "title.tag"
        const val TITLE_ORDER = "title.order"
        fun newInstance(title: String, order: Int): AttributesFragment {
            return AttributesFragment().apply {
                arguments = bundleOf(TITLE_TAG to title)
            }
        }
    }

    private val adapter = AttributesAdapter()

    override val viewModel: RootAttrViewModel by viewModels(ownerProducer = { requireParentFragment() })


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
            val order = arguments?.getInt(TITLE_ORDER) ?: 0
            adapter.items = it[order]?.list ?: listOf()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.setupRecyclerView()
        viewBinding.buttonNext.setOnClickListener {
            viewModel.navigateNext()
        }
        viewBinding.title.text = arguments?.getString(TITLE_TAG) ?: ""

    }

    private fun FragmentInputBinding.setupRecyclerView() {
        rv.adapter = adapter
    }

}