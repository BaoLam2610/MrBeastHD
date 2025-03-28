package com.lambao.base.presentation.ui.state

sealed class ScreenState {
    class Idle : ScreenState()
    class Loading : ScreenState()
    class Success : ScreenState()
    data class Error(val throwable: Throwable) : ScreenState()
}