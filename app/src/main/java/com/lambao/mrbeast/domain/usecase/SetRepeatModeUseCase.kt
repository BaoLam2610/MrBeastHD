package com.lambao.mrbeast.domain.usecase

import com.lambao.base.data.pref.PreferenceRepository
import com.lambao.base.domain.SuspendUseCase
import com.lambao.base.presentation.handler.dispatcher.DispatcherProvider
import com.lambao.mrbeast.domain.model.playback.RepeatMode
import com.lambao.mrbeast.utils.Constants
import javax.inject.Inject

class SetRepeatModeUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    dispatcherProvider: DispatcherProvider
) : SuspendUseCase<RepeatMode, Unit>(dispatcherProvider) {
    override suspend fun execute(params: RepeatMode?) {
        preferenceRepository.setInt(Constants.Preference.REPEAT, params?.key ?: RepeatMode.NONE.key)
    }
}