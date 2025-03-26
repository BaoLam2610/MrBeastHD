package com.lambao.base.extension

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Launches a Flow collection tied to a LifecycleOwner's lifecycle, collecting values when in the specified state.
 *
 * @param T The type of data emitted by the Flow
 * @param lifecycleOwner The LifecycleOwner whose lifecycle will control the collection
 * @param lifecycleState The Lifecycle state when collection should be active (defaults to STARTED)
 * @param collect The suspend function to handle each emitted value
 * @return A Job representing the launched coroutine
 */
fun <T> Flow<T>.launchWhen(
    lifecycleOwner: LifecycleOwner,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    collect: suspend (T) -> Unit
) = lifecycleOwner.lifecycleScope.launch {
    lifecycleOwner.repeatOnLifecycle(lifecycleState) {
        collect { collect(it) }
    }
}

/**
 * Launches a coroutine tied to a LifecycleOwner's lifecycle, executing the block when in the specified state.
 *
 * @param lifecycleState The Lifecycle state when the block should be active (defaults to STARTED)
 * @param block The suspend function to execute within the coroutine scope
 * @return A Job representing the launched coroutine
 */
fun LifecycleOwner.launchWhen(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(lifecycleState) {
            block()
        }
    }
}

/**
 * Launches a coroutine tied to a LifecycleOwner's lifecycle, executing the block when in CREATED state.
 *
 * @param block The suspend function to execute within the coroutine scope
 * @return A Job representing the launched coroutine
 */
fun LifecycleOwner.launchWhenCreated(block: suspend CoroutineScope.() -> Unit) {
    launchWhen(Lifecycle.State.CREATED, block)
}