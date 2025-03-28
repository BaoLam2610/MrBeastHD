package com.lambao.base.extension

import com.lambao.base.presentation.ui.activity.BaseActivity
import com.lambao.base.presentation.ui.bottom_sheet.BaseBottomSheet
import com.lambao.base.presentation.ui.dialog.BaseDialog
import com.lambao.base.presentation.ui.fragment.BaseFragment
import kotlinx.coroutines.flow.Flow

/**
 * Observes a Flow and executes the provided callback whenever a new value is emitted.
 *
 * @param flow The Flow to observe
 * @param onChanged Callback function that will be invoked with the emitted value
 * @param T The type of data emitted by the Flow
 */
fun <T> BaseActivity<*>.observe(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchWhen(this) {
        onChanged.invoke(it)
    }
}

/**
 * Observes a Flow in a Fragment and executes the provided callback when new values are emitted.
 *
 * @param flow The Flow to observe
 * @param onChanged Callback function that will be invoked with the emitted value
 * @param T The type of data emitted by the Flow
 */
fun <T> BaseFragment<*>.observe(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchWhen(this) {
        onChanged.invoke(it)
    }
}

/**
 * Observes a Flow in a Dialog and executes the callback when new values are emitted.
 *
 * @param flow The Flow to observe
 * @param onChanged Callback function that will be invoked with the emitted value
 * @param T The type of data emitted by the Flow
 */
fun <T> BaseDialog<*>.observe(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchWhen(this) {
        onChanged.invoke(it)
    }
}

/**
 * Observes a Flow in a BottomSheet and executes the callback when new values are emitted.
 *
 * @param flow The Flow to observe
 * @param onChanged Callback function that will be invoked with the emitted value
 * @param T The type of data emitted by the Flow
 */
fun <T> BaseBottomSheet<*>.observe(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchWhen(this) {
        onChanged.invoke(it)
    }
}