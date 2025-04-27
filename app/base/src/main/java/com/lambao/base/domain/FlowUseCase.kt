package com.lambao.base.domain

import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in Params, out Result>(
    dispatchProvider: DispatcherProvider
) : UseCase(dispatchProvider) {
    protected abstract fun execute(params: Params? = null): Flow<Result>

    operator fun invoke(params: Params? = null): Flow<Result> =
        execute(params).flowOn(coroutineDispatcher)
}