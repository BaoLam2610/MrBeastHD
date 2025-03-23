package com.lambao.base.utils

import timber.log.Timber

fun Any.log(message: String) {
    Timber.tag(this::class.java.simpleName).d(message)
}