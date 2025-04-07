package com.lambao.mrbeast.domain.usecase

import com.lambao.base.data.pref.PreferenceRepository
import com.lambao.mrbeast.domain.model.playback.ShuffleMode
import com.lambao.mrbeast.utils.Constants
import javax.inject.Inject

class GetShuffleModeUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
) {
    operator fun invoke(): ShuffleMode {
        return ShuffleMode.fromKey(
            preferenceRepository.getInt(Constants.Preference.SHUFFLE, ShuffleMode.OFF.key)
        )
    }
}