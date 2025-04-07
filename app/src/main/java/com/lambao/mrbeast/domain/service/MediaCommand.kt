package com.lambao.mrbeast.domain.service

import com.lambao.mrbeast.domain.model.Song
import com.lambao.mrbeast.domain.model.playback.RepeatMode
import com.lambao.mrbeast.domain.model.playback.ShuffleMode

interface MediaCommand {
    fun execute()
}

class PlayCommand(
    private val manager: MediaPlayerManager,
    private val playlist: List<Song>,
    private val startIndex: Int
) : MediaCommand {
    override fun execute() = manager.play(playlist, startIndex)
}

class PauseCommand(private val manager: MediaPlayerManager) : MediaCommand {
    override fun execute() = manager.pause()
}

class ResumeCommand(private val manager: MediaPlayerManager) : MediaCommand {
    override fun execute() = manager.resume()
}

class StopCommand(private val manager: MediaPlayerManager) : MediaCommand {
    override fun execute() = manager.stop()
}

class PreviousCommand(private val manager: MediaPlayerManager) : MediaCommand {
    override fun execute() = manager.previous()
}

class NextCommand(private val manager: MediaPlayerManager) : MediaCommand {
    override fun execute() = manager.next()
}

class SeekToCommand(
    private val manager: MediaPlayerManager,
    private val position: Long
) : MediaCommand {
    override fun execute() = manager.seekTo(position)
}

class RepeatCommand(
    private val manager: MediaPlayerManager,
    private val repeatMode: RepeatMode
) : MediaCommand {
    override fun execute() {
        manager.setRepeatMode(repeatMode)
    }
}

class ShuffleCommand(
    private val manager: MediaPlayerManager,
    private val shuffleMode: ShuffleMode
) : MediaCommand {
    override fun execute() {

    }
}