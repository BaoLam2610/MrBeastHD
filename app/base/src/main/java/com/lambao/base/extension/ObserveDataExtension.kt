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
    flow.launchCollect(this) {
        onChanged.invoke(it)
    }
}

/**
 * Observes a Flow in an Activity and executes the provided callback with the latest emitted value.
 * Only the most recent value is processed, discarding previous values if the collector cannot keep up.
 *
 * @param flow The Flow to observe
 * @param onChanged Callback function that will be invoked with the latest emitted value
 * @param T The type of data emitted by the Flow
 */
fun <T> BaseActivity<*>.observeLatest(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchCollectLatest(this) {
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
    flow.launchCollect(this) {
        onChanged.invoke(it)
    }
}

/**
 * Observes a Flow in a Fragment and executes the provided callback with the latest emitted value.
 * Only the most recent value is processed, discarding previous values if the collector cannot keep up.
 *
 * @param flow The Flow to observe
 * @param onChanged Callback function that will be invoked with the latest emitted value
 * @param T The type of data emitted by the Flow
 */
fun <T> BaseFragment<*>.observeLatest(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchCollectLatest(this) {
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
    flow.launchCollect(this) {
        onChanged.invoke(it)
    }
}

/**
 * Observes a Flow in a Dialog and executes the provided callback with the latest emitted value.
 * Only the most recent value is processed, discarding previous values if the collector cannot keep up.
 *
 * @param flow The Flow to observe
 * @param onChanged Callback function that will be invoked with the latest emitted value
 * @param T The type of data emitted by the Flow
 */
fun <T> BaseDialog<*>.observeLatest(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchCollectLatest(this) {
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
    flow.launchCollect(this) {
        onChanged.invoke(it)
    }
}

/**
 * Observes a Flow in a BottomSheet and executes the provided callback with the latest emitted value.
 * Only the most recent value is processed, discarding previous values if the collector cannot keep up.
 *
 * @param flow The Flow to observe
 * @param onChanged Callback function that will be invoked with the latest emitted value
 * @param T The type of data emitted by the Flow
 */
fun <T> BaseBottomSheet<*>.observeLatest(
    flow: Flow<T>,
    onChanged: (T) -> Unit
) {
    flow.launchCollectLatest(this) {
        onChanged.invoke(it)
    }
}