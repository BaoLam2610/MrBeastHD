package com.lambao.base.extension

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.lambao.base.presentation.ui.view.OnSingleClickListener

private const val SUPER_STATE = "SUPER_STATE"
private const val SPARSE_STATE_KEY = "SPARSE_STATE_KEY"

fun View.click(func: (v: View) -> Unit) {
    setOnClickListener(
        object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                v?.let { func(it) }
            }
        }
    )
}

fun ViewGroup.saveChildViewStates(): SparseArray<Parcelable> {
    val childViewStates = SparseArray<Parcelable>()
    children.forEach { child -> child.saveHierarchyState(childViewStates) }
    return childViewStates
}

fun ViewGroup.restoreChildViewStates(childViewStates: SparseArray<Parcelable>) {
    children.forEach { child -> child.restoreHierarchyState(childViewStates) }
}

fun ViewGroup.saveInstanceState(state: Parcelable?): Parcelable? {
    return Bundle().apply {
        putParcelable(SUPER_STATE, state)
        putSparseParcelableArray(SPARSE_STATE_KEY, saveChildViewStates())
    }
}

fun ViewGroup.restoreInstanceState(state: Parcelable?): Parcelable? {
    var newState = state
    if (newState is Bundle) {
        val childrenState =
            newState.getSparseParcelableArrayCompat<Parcelable>(SPARSE_STATE_KEY)
        childrenState?.let { restoreChildViewStates(it) }
        newState = newState.getParcelableCompat(SUPER_STATE)
    }
    return newState
}