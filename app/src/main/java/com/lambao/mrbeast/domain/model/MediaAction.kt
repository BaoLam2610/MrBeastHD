package com.lambao.mrbeast.domain.model

enum class MediaAction(val requestCode: Int) {
    PLAY(0),
    PAUSE(0),
    PREVIOUS(0),
    PLAY_PAUSE(1),
    NEXT(2),
    CLOSE(3)
}