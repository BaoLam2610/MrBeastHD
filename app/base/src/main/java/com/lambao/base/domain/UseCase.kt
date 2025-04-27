package com.lambao.base.domain

import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider

abstract class UseCase(
    private val dispatchProvider: DispatcherProvider
) {
    protected open val coroutineDispatcher get() = dispatchProvider.ioDispatcher
}