package com.voxeldev.canoe.settings.integration

import com.voxeldev.canoe.settings.Settings
import com.voxeldev.canoe.settings.store.SettingsStore.State

/**
 * @author nvoxel
 */
internal class StateMapper {

    fun toModel(state: State): Settings.Model =
        Settings.Model(
            isConnected = state.isConnected,
            isLoginDialogVisible = state.isLoginDialogVisible,
            isCodeDialogVisible = state.isCodeDialogVisible,
            codeText = state.codeText,
            errorText = state.errorText,
            isLoading = state.isLoading,
        )
}
