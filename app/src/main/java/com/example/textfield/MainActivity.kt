package com.example.textfield

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.textfield.databinding.ActivityMainBinding
import com.example.textfield.presentation.attrs.AttributesFragment
import com.example.textfield.presentation.base.BaseActivity
import com.example.textfield.presentation.base.util.replace
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainActivityViewModel,ActivityMainBinding>() {



    override val viewModel: MainActivityViewModel by viewModels()

    override fun inflateViewBinding(savedInstanceState: Bundle?): ActivityMainBinding {
        return ActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.replace(
            R.id.fragment_container,
            AttributesFragment.TAG,
            AttributesFragment.newInstance(),
            addToBackStack = false
        )
    }
}