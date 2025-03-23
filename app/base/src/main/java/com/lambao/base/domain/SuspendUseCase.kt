package com.lambao.base.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class SuspendUseCase<in Params, out Result>(
    private val coroutineDispatcher: CoroutineDispatcher
) {
    protected abstract suspend fun execute(params: Params? = null): Result

    suspend operator fun invoke(params: Params? = null): Result = withContext(coroutineDispatcher) {
        execute(params)
    }
}