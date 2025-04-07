package com.lambao.mrbeast.domain.usecase

import com.lambao.base.data.pref.PreferenceRepository
import com.lambao.mrbeast.domain.model.playback.RepeatMode
import com.lambao.mrbeast.utils.Constants
import javax.inject.Inject

class GetRepeatModeUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
) {
    operator fun invoke(): RepeatMode {
        return RepeatMode.fromKey(
            preferenceRepository.getInt(Constants.Preference.REPEAT, RepeatMode.NONE.key)
        )
    }
}