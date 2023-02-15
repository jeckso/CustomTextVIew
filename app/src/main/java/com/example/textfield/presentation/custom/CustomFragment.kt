package com.example.textfield.presentation.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.generateViewId
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import androidx.fragment.app.viewModels
import com.example.textfield.android.CustomTextField
import com.example.textfield.data.model.IAttribute
import com.example.textfield.databinding.FragmentCustomBinding
import com.example.textfield.presentation.attrs.root.RootAttrViewModel
import com.example.textfield.presentation.base.BaseFragment
import timber.log.Timber
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties


class CustomFragment : BaseFragment<RootAttrViewModel, FragmentCustomBinding>(),
    CustomTextField.CustomTextFieldEventListener {

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
        viewModel.attrs.asObserverJob {
            it.forEachIndexed { index, attributes ->
                viewBinding.addField(attributes?.list!!)

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // viewBinding.addField()

    }

    private fun FragmentCustomBinding.addField(list: List<IAttribute>) {
        val customField = CustomTextField(requireContext())
        val set = ConstraintSet()
        customField.id = generateViewId()
        pasteAllProperties(customField, list)
        constraint.addView(customField, 0)
        set.clone(constraint)
        set.connect(customField.id, ConstraintSet.TOP, constraint.id, ConstraintSet.TOP, 60)
        set.applyTo(constraint)
    }

    private fun pasteAllProperties(customField: CustomTextField, list: List<IAttribute>) {

        list.forEach { item ->
            if (item.id.contains("_r")) {
                Timber.e("ITEM ID ${item.id}")
                val realId = item.id.substring(0, item.id.length - 1)
                Timber.e("realId ${realId}")

                val listRGB = list.filter { it.id.contains(realId) }
                Timber.e("listRGB ${listRGB}")

                val rgbValue = constructColorFromAttributes(listRGB)
                val member = CustomTextField::class.memberProperties.find {
                    it.name == item.id.substring(
                        0,
                        item.id.length - 2
                    )
                }
                if (member is KMutableProperty<*>) {
                    Timber.e("KMutableProperty")
                    member.setter.call(customField, item.value)
                }
            }
            val member = CustomTextField::class.memberProperties.find { it.name == item.id }
            if (member is KMutableProperty<*>) {
                Timber.e("KMutableProperty")
                member.setter.call(customField, item.value)
            }
        }
    }

    private fun constructColorFromAttributes(input: List<IAttribute>): Triple<Int, Int, Int> {
        return Triple(
            input[0].value as Int? ?: 0,
            input[1].value as Int? ?: 0,
            input[2].value as Int? ?: 0
        )

    }

    override fun onNextResponder(nextResponder: String?) {
        setFocusByLocalId(nextResponder!!)    //Write your own logic
    }

    private fun setFocusByLocalId(id: String) {
        with(viewBinding) {
            val count = constraint.childCount
            constraint.children.map {
                if (it is CustomTextField) {
                    if (it.identifier == id) it.requestFocus()
                }
            }
        }

    }


}