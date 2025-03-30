package com.lambao.mrbeast.domain.usecase

import com.lambao.mrbeast.domain.model.Song
import javax.inject.Inject

class GetIndexInPlaylistUseCase @Inject constructor() {
    operator fun invoke(
        song: Song?,
        playlist: List<Song>
    ): Int {
        return playlist.indexOfFirst { it.id == song?.id }
    }
}