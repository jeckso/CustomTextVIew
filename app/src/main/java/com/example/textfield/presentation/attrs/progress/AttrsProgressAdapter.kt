package com.example.textfield.presentation.attrs.progress

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.textfield.presentation.base.BaseFragment

class AttrsProgressAdapter constructor(fragment: BaseFragment<*, *>) : FragmentStateAdapter(fragment) {

    private val _tabs: MutableList<AttrProgressItem> = mutableListOf()

    var tabs: List<AttrProgressItem>
        set(value) {
            _tabs.clear()
            _tabs.addAll(value)
            notifyDataSetChanged()
        }
        get() = _tabs

    override fun getItemCount(): Int {
        return _tabs.size
    }

    override fun createFragment(position: Int): Fragment {
        return _tabs[position].fragment
    }


}