package com.lambao.base.domain

import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider
import kotlinx.coroutines.withContext

abstract class SuspendUseCase<in Params, out Result>(
    dispatchProvider: DispatcherProvider
) : UseCase(dispatchProvider) {
    protected abstract suspend fun execute(params: Params? = null): Result

    suspend operator fun invoke(params: Params? = null): Result = withContext(coroutineDispatcher) {
        execute(params)
    }
}