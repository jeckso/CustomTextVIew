package com.example.textfield.presentation.custom

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.textfield.R
import com.example.textfield.android.CustomTextField
import com.example.textfield.data.model.IAttribute
import com.example.textfield.databinding.FragmentCustomBinding
import com.example.textfield.presentation.attrs.root.RootAttrViewModel
import com.example.textfield.presentation.base.BaseFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.addField()
    }

    private fun FragmentCustomBinding.addField() {
        val customField = CustomTextField(requireContext())
        val set = ConstraintSet()
        customField.id = R.id.custom_text_view1
        customField.bgColor = Triple(0, 255, 0)
        customField.text = SpannableStringBuilder("HEREHEHRHHRHERHEH")
//        customField.borderColor = Triple(255, 0, 0)
        Timber.e("ID OF TEXTVIEW ${customField.id}")
        Timber.e("ID OF TEXTVIEW ${R.id.custom_text_view1}")
        Timber.e("ID OF bgColor ${customField.bgColor}")
        constraint.addView(customField,0)
        set.clone(constraint)
        set.connect(customField.id, ConstraintSet.TOP, constraint.id, ConstraintSet.TOP, 60)
        set.applyTo(constraint)
    }

    private fun pasteAllProperties(customField : CustomTextField, list: List<IAttribute>){
    }
}