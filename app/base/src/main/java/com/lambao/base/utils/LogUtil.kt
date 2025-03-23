package com.lambao.base.utils

import android.util.Log

class LogUtil private constructor() {
    companion object {
        fun tagFor(clazz: Class<*>): String {
            return clazz.simpleName
        }

        fun log(tag: String, message: String) {
            Log.d(tag, message)
        }

        fun log(message: String, clazz: Class<*>) {
            log(tagFor(clazz), message)
        }
    }
}