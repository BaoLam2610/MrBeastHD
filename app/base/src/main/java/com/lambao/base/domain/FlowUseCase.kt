package com.lambao.base.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in Params, out Result>(
    private val coroutineDispatcher: CoroutineDispatcher
) {
    protected abstract fun execute(params: Params? = null): Flow<Result>

    operator fun invoke(params: Params? = null): Flow<Result> =
        execute(params).flowOn(coroutineDispatcher)
}