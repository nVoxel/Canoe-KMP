package com.voxeldev.canoe.settings

import com.arkivanov.decompose.value.Value

/**
 * @author nvoxel
 */
interface Settings {

    val model: Value<Model>

    fun onConnectButtonClicked()
    fun onDisconnectButtonClicked()

    fun onLoginDialogConfirmClicked()
    fun onLoginDialogDismissClicked()

    fun onCodeDialogTextChanged(text: String)
    fun onCodeDialogConfirmClicked()
    fun onCodeDialogDismissClicked()

    fun onReloadButtonClicked()

    data class Model(
        val isConnected: Boolean,
        val isLoginDialogVisible: Boolean,
        val isCodeDialogVisible: Boolean,
        val codeText: String,
        val errorText: String?,
        val isLoading: Boolean,
    )

    sealed class Output {
        data class Connect(val connectUrl: String) : Output()
    }
}
