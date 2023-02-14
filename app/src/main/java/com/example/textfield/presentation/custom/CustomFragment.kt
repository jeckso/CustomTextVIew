package com.example.textfield.presentation.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.textfield.databinding.FragmentCustomBinding
import com.example.textfield.databinding.FragmentInputBinding
import com.example.textfield.presentation.attrs.AttributesFragment
import com.example.textfield.presentation.attrs.root.RootAttrViewModel
import com.example.textfield.presentation.base.BaseFragment

class CustomFragment : BaseFragment<RootAttrViewModel, FragmentCustomBinding>() {

    companion object {
        const val TAG = "AttributesFragment"
        const val TITLE_TAG = "title.tag"
        fun newInstance(): CustomFragment {
            return CustomFragment()
        }
    }

    override val viewModel: RootAttrViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentCustomBinding {
        return FragmentCustomBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}