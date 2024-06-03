package com.voxeldev.canoe.settings.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.voxeldev.canoe.settings.integration.GetAccessTokenFromCodeUseCase
import com.voxeldev.canoe.settings.integration.GetAccessTokenFromStorageUseCase
import com.voxeldev.canoe.settings.integration.RevokeAccessTokenUseCase
import com.voxeldev.canoe.settings.store.SettingsStore.Intent
import com.voxeldev.canoe.settings.store.SettingsStore.State
import com.voxeldev.canoe.utils.extensions.getMessage
import com.voxeldev.canoe.utils.integration.BaseUseCase
import com.voxeldev.canoe.utils.parsers.AuthenticationCodeParser

/**
 * @author nvoxel
 */
internal class SettingsStoreProvider(
    private val storeFactory: StoreFactory,
    private val deepLink: String?,
    private val authenticationCodeParser: AuthenticationCodeParser,
    // private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics,
    private val getAccessTokenFromCodeUseCase: GetAccessTokenFromCodeUseCase = GetAccessTokenFromCodeUseCase(),
    private val getAccessTokenFromStorageUseCase: GetAccessTokenFromStorageUseCase = GetAccessTokenFromStorageUseCase(),
    private val revokeAccessTokenUseCase: RevokeAccessTokenUseCase = RevokeAccessTokenUseCase(),
) {

    fun provide(): SettingsStore =
        object :
            SettingsStore,
            Store<Intent, State, Nothing> by storeFactory.create(
                name = STORE_NAME,
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        data class LoginDialogVisibilityChanged(val isVisible: Boolean) : Msg()
        data class CodeDialogVisibilityChanged(val isVisible: Boolean) : Msg()
        data class CodeDialogTextChanged(val text: String) : Msg()
        data class AccessTokenLoaded(val isConnected: Boolean) : Msg()
        data object AccessTokenLoading : Msg()
        data class Error(val message: String) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Nothing>() {

        override fun executeAction(action: Unit, getState: () -> State) {
            getAccessTokenFromStorage()
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            val state = getState()

            when (intent) {
                is Intent.OpenLoginDialog -> dispatch(message = Msg.LoginDialogVisibilityChanged(isVisible = true))
                is Intent.CloseLoginDialog -> dispatch(message = Msg.LoginDialogVisibilityChanged(isVisible = false))

                is Intent.OpenCodeDialog -> {
                    dispatch(message = Msg.LoginDialogVisibilityChanged(isVisible = false))
                    dispatch(message = Msg.CodeDialogVisibilityChanged(isVisible = true))
                }

                is Intent.CloseCodeDialog -> dispatch(message = Msg.CodeDialogVisibilityChanged(isVisible = false))
                is Intent.CodeDialogTextChanged -> dispatch(message = Msg.CodeDialogTextChanged(text = intent.text))

                is Intent.LoginWithCode -> getAccessTokenFromCode(uri = state.codeText)

                is Intent.DisconnectAccount -> revokeAccessToken()
                is Intent.ReloadAccount -> getAccessTokenFromStorage()
            }
        }

        private fun getAccessTokenFromStorage() {
            dispatch(Msg.AccessTokenLoading)
            getAccessTokenFromStorageUseCase(params = BaseUseCase.NoParams, scope = scope) { result ->
                result.fold(
                    onSuccess = { dispatch(message = Msg.AccessTokenLoaded(isConnected = true)) },
                    onFailure = {
                        deepLink?.let { getAccessTokenFromCode(it) } ?: dispatch(message = Msg.AccessTokenLoaded(isConnected = false))
                    },
                )
            }
        }

        private fun getAccessTokenFromCode(uri: String) {
            authenticationCodeParser.getAuthenticationCode(uri)?.let { code ->
                // val trace = Firebase.performance.startTrace(trace = CustomTrace.AuthenticationLoadTrace)
                dispatch(message = Msg.AccessTokenLoading)
                getAccessTokenFromCodeUseCase(params = code, scope = scope) { result ->
                    result.fold(
                        onSuccess = {
                            // firebaseAnalytics.logEvent(event = CustomEvent.Login)
                            dispatch(message = Msg.AccessTokenLoaded(isConnected = true))
                            dispatch(message = Msg.CodeDialogVisibilityChanged(isVisible = false))
                        },
                        onFailure = { dispatch(message = Msg.Error(message = it.getMessage())) },
                    )
                    // .also { trace.stop() }
                }
            }
        }

        private fun revokeAccessToken() {
            // val trace = Firebase.performance.startTrace(trace = CustomTrace.AuthenticationLoadTrace)
            dispatch(Msg.AccessTokenLoading)
            revokeAccessTokenUseCase(params = BaseUseCase.NoParams, scope = scope) { result ->
                result
                    .fold(
                        onSuccess = {
                            // firebaseAnalytics.logEvent(event = CustomEvent.Logout)
                            dispatch(message = Msg.AccessTokenLoaded(isConnected = false))
                        },
                        onFailure = { dispatch(message = Msg.Error(message = it.getMessage())) },
                    )
                // .also { trace.stop() }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.LoginDialogVisibilityChanged -> copy(isLoginDialogVisible = msg.isVisible)
                is Msg.CodeDialogVisibilityChanged -> copy(isCodeDialogVisible = msg.isVisible)
                is Msg.CodeDialogTextChanged -> copy(codeText = msg.text)
                is Msg.AccessTokenLoaded -> copy(isConnected = msg.isConnected, errorText = null, isLoading = false)
                is Msg.AccessTokenLoading -> copy(isLoading = true)
                is Msg.Error -> copy(errorText = msg.message, isLoading = false)
            }
    }

    private companion object {
        const val STORE_NAME = "SettingsStore"
    }
}
