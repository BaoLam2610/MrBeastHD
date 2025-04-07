package com.lambao.mrbeast.domain.usecase

import com.lambao.base.data.pref.PreferenceRepository
import com.lambao.base.domain.SuspendUseCase
import com.lambao.mrbeast.di.IoDispatcher
import com.lambao.mrbeast.domain.model.playback.RepeatMode
import com.lambao.mrbeast.utils.Constants
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SetRepeatModeUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
) : SuspendUseCase<RepeatMode, Unit>(ioDispatcher) {
    override suspend fun execute(params: RepeatMode?) {
        preferenceRepository.setInt(Constants.Preference.REPEAT, params?.key ?: RepeatMode.NONE.key)
    }
}