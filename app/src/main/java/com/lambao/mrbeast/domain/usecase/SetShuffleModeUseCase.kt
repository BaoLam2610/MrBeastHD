package com.lambao.mrbeast.domain.usecase

import com.lambao.base.data.pref.PreferenceRepository
import com.lambao.base.domain.SuspendUseCase
import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider
import com.lambao.mrbeast.domain.model.playback.ShuffleMode
import com.lambao.mrbeast.utils.Constants
import javax.inject.Inject

class SetShuffleModeUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    dispatcherProvider: DispatcherProvider
) : SuspendUseCase<ShuffleMode, Unit>(dispatcherProvider) {
    override suspend fun execute(params: ShuffleMode?) {
        preferenceRepository.setInt(
            Constants.Preference.SHUFFLE,
            params?.key ?: ShuffleMode.OFF.key
        )
    }
}