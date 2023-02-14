package com.example.textfield.presentation.base.util

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit


val Fragment.rootFragment: Fragment
    get() = parentFragment?.rootFragment ?: this

val FragmentManager.entries
    get() = sequence {
        for (entry in 0 until backStackEntryCount) {
            yield(getBackStackEntryAt(entry))
        }
    }

fun FragmentManager.containsEntry(tag: String): Boolean {
    return entries.any { it.name == tag }
}

fun FragmentManager.add(
    @IdRes container: Int,
    tag: String,
    fragment: Fragment,
    addToBackStack: Boolean = true,
) = execute(tag, addToBackStack) {
    it.add(container, fragment, tag)
}

fun FragmentManager.replace(
    @IdRes container: Int,
    tag: String,
    fragment: Fragment,
    addToBackStack: Boolean = true,
) = execute(tag, addToBackStack) {
    it.replace(container, fragment, tag)
}

fun FragmentManager.execute(
    tag: String,
    addToBackStack: Boolean = true,
    animations: IntArray = intArrayOf(),
    block: (FragmentTransaction) -> Unit
) = commit {
    when (animations.size) {
        2 -> setCustomAnimations(animations[0], animations[1])
        4 -> setCustomAnimations(animations[0], animations[1], animations[2], animations[3])
    }
    if (addToBackStack) {
        addToBackStack(tag)
    }
    block(this)
}