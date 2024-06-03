package com.voxeldev.canoe.settings.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.voxeldev.canoe.settings.Settings
import com.voxeldev.canoe.settings.Settings.Output
import com.voxeldev.canoe.settings.store.SettingsStore
import com.voxeldev.canoe.settings.store.SettingsStoreProvider
import com.voxeldev.canoe.utils.extensions.asValue
import com.voxeldev.canoe.utils.parsers.AuthenticationCodeParser
import com.voxeldev.canoe.utils.platform.Platform
import com.voxeldev.canoe.utils.platform.currentPlatform
import com.voxeldev.canoe.utils.providers.string.StringResourceProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author nvoxel
 */
class SettingsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val deepLink: String?,
    private val output: (Output) -> Unit,
) : Settings, KoinComponent, ComponentContext by componentContext {

    private val stateMapper: StateMapper = StateMapper()

    private val authenticationCodeParser: AuthenticationCodeParser by inject()

    private val stringResourceProvider: StringResourceProvider by inject()

    private val store = instanceKeeper.getStore {
        SettingsStoreProvider(
            storeFactory = storeFactory,
            deepLink = deepLink,
            authenticationCodeParser = authenticationCodeParser,
        ).provide()
    }

    override val model: Value<Settings.Model> = store.asValue().map { state -> stateMapper.toModel(state = state) }

    override fun onConnectButtonClicked() {
        if (currentPlatform == Platform.DESKTOP) {
            store.accept(intent = SettingsStore.Intent.OpenLoginDialog)
        } else {
            openBrowserUrl()
        }
    }

    override fun onDisconnectButtonClicked() = store.accept(intent = SettingsStore.Intent.DisconnectAccount)

    override fun onLoginDialogConfirmClicked() {
        openBrowserUrl()
        store.accept(intent = SettingsStore.Intent.OpenCodeDialog)
    }

    override fun onLoginDialogDismissClicked() = store.accept(intent = SettingsStore.Intent.CloseLoginDialog)

    override fun onCodeDialogTextChanged(text: String) = store.accept(
        intent = SettingsStore.Intent.CodeDialogTextChanged(text = text),
    )

    override fun onCodeDialogConfirmClicked() = store.accept(intent = SettingsStore.Intent.LoginWithCode)

    override fun onCodeDialogDismissClicked() = store.accept(intent = SettingsStore.Intent.CloseCodeDialog)

    override fun onReloadButtonClicked() = store.accept(intent = SettingsStore.Intent.ReloadAccount)

    private fun openBrowserUrl() = output(
        Output.Connect(connectUrl = stringResourceProvider.getOAuthAuthorizeUrl()),
    )
}
