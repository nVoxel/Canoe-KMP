package com.voxeldev.canoe.leaderboards.store

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.voxeldev.canoe.leaderboards.api.LeaderboardEntry
import com.voxeldev.canoe.leaderboards.api.LeaderboardsModel
import com.voxeldev.canoe.leaderboards.api.LeaderboardsRequest
import com.voxeldev.canoe.leaderboards.integration.GetLeaderboardsAsyncUseCase
import com.voxeldev.canoe.leaderboards.paging.LeaderboardsPagingSource
import com.voxeldev.canoe.leaderboards.paging.LeaderboardsPagingSource.Companion.PAGING_MAX_SIZE
import com.voxeldev.canoe.leaderboards.paging.LeaderboardsPagingSource.Companion.PAGING_PAGE_SIZE
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.Intent
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.State
import com.voxeldev.canoe.utils.analytics.CommonAnalytics
import com.voxeldev.canoe.utils.analytics.CustomEvent
import com.voxeldev.canoe.utils.extensions.getMessage
import kotlinx.coroutines.flow.Flow

/**
 * @author nvoxel
 */
internal class LeaderboardsStoreProvider(
    private val storeFactory: StoreFactory,
    private val commonAnalytics: CommonAnalytics,
    private val getLeaderboardsAsyncUseCase: GetLeaderboardsAsyncUseCase = GetLeaderboardsAsyncUseCase(),
) {

    fun provide(): LeaderboardsStore =
        object :
            LeaderboardsStore,
            Store<Intent, State, Nothing> by storeFactory.create(
                name = STORE_NAME,
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        data class LeaderboardsLoaded(val leaderboardsModel: LeaderboardsModel) : Msg()
        data object LeaderboardsLoading : Msg()
        data class LeaderboardsPagerLoaded(val flow: Flow<PagingData<LeaderboardEntry>>) : Msg()
        data class Error(val message: String) : Msg()
        data class LanguageChanged(val language: String?) : Msg()
        data class HireableChanged(val hireable: Boolean?) : Msg()
        data class CountryCodeChanged(val countryCode: String?) : Msg()
        data class FilterBottomSheetToggled(val active: Boolean) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Nothing>() {
        private var lastFilterStateHash: Int? = null

        override fun executeAction(action: Unit, getState: () -> State) = loadLeaderboards(state = getState())

        override fun executeIntent(intent: Intent, getState: () -> State) {
            val state = getState()

            when (intent) {
                is Intent.LoadMore -> {
                    state.leaderboardsModel?.let { model ->
                        if (model.page < model.totalPages) {
                            loadLeaderboards(state = state, resetPage = false)
                        }
                    }
                }

                is Intent.SetLanguage -> dispatch(message = Msg.LanguageChanged(language = intent.language))
                is Intent.SetHireable -> dispatch(message = Msg.HireableChanged(hireable = intent.hireable))
                is Intent.SetCountryCode -> dispatch(message = Msg.CountryCodeChanged(countryCode = intent.countryCode))
                is Intent.ReloadLeaderboards -> {
                    resetFilters()
                    loadLeaderboards(state = getState())
                }

                is Intent.ResetFilters -> resetFilters()

                is Intent.ToggleFilterBottomSheet -> {
                    val isActive = state.filterBottomSheetState.active

                    // reload list only when necessary
                    if (isActive && state.filterBottomSheetState.hashCode() != lastFilterStateHash) {
                        loadLeaderboards(state = state)
                    }

                    lastFilterStateHash = state.filterBottomSheetState.copy(active = !isActive).hashCode()

                    dispatch(message = Msg.FilterBottomSheetToggled(active = !isActive))
                }
            }
        }

        private fun loadLeaderboards(state: State, resetPage: Boolean = true) {
            if (resetPage) {
                dispatch(message = Msg.LeaderboardsLoading)
            }

            getLeaderboardsAsyncUseCase(
                params = getLeaderboardsRequest(state = state, resetPage = resetPage),
                scope = scope,
            ) { result ->
                result
                    .fold(
                        onSuccess = { loadedLeaderboards ->
                            if (resetPage) {
                                commonAnalytics.logEvent(event = CustomEvent.LoadedLeaderboards)
                            }

                            dispatch(
                                message = Msg.LeaderboardsLoaded(
                                    leaderboardsModel = if (resetPage || state.leaderboardsModel == null) {
                                        loadedLeaderboards
                                    } else {
                                        mergeLeaderboards(
                                        previous = state.leaderboardsModel,
                                        next = loadedLeaderboards,
                                    )
                                    },
                                ),
                            )
                            loadLeaderboardsPaging(state = state)
                        },
                        onFailure = { dispatch(message = Msg.Error(message = it.getMessage())) },
                    )
            }
        }

        private fun mergeLeaderboards(previous: LeaderboardsModel, next: LeaderboardsModel): LeaderboardsModel {
            val data = mutableListOf<LeaderboardEntry>()
            data.addAll(previous.data)
            data.addAll(next.data)
            return next.copy(data = data)
        }

        private fun loadLeaderboardsPaging(state: State) =
            dispatch(
                message = Msg.LeaderboardsPagerLoaded(
                    flow = Pager(
                        config = PagingConfig(
                            pageSize = PAGING_PAGE_SIZE,
                            maxSize = PAGING_MAX_SIZE,
                        ),
                        pagingSourceFactory = {
                            LeaderboardsPagingSource(
                                state = state,
                                scope = scope,
                            )
                        },
                    ).flow.cachedIn(scope = scope),
                ),
            )

        private fun getLeaderboardsRequest(state: State, resetPage: Boolean): LeaderboardsRequest =
            LeaderboardsRequest(
                language = state.filterBottomSheetState.selectedLanguage,
                isHireable = state.filterBottomSheetState.hireable,
                countryCode = state.filterBottomSheetState.selectedCountryCode,
                page = if (resetPage) {
                    ADD_LEADERBOARDS_PAGES
                } else {
                    (state.leaderboardsModel?.page ?: DEFAULT_LEADERBOARDS_PAGE) + ADD_LEADERBOARDS_PAGES
                },
            )

        private fun resetFilters() {
            dispatch(message = Msg.LanguageChanged(language = null))
            dispatch(message = Msg.HireableChanged(hireable = null))
            dispatch(message = Msg.CountryCodeChanged(countryCode = null))
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.LeaderboardsLoaded -> copy(
                    leaderboardsModel = msg.leaderboardsModel,
                    errorText = null,
                    isLoading = false,
                )

                is Msg.LeaderboardsLoading -> copy(isLoading = true)
                is Msg.LeaderboardsPagerLoaded -> copy(leaderboardsFlow = msg.flow)
                is Msg.Error -> copy(errorText = msg.message, isLoading = false)
                is Msg.LanguageChanged -> copy(
                    filterBottomSheetState = filterBottomSheetState.copy(selectedLanguage = msg.language),
                )

                is Msg.HireableChanged -> copy(
                    filterBottomSheetState = filterBottomSheetState.copy(hireable = msg.hireable),
                )

                is Msg.CountryCodeChanged -> copy(
                    filterBottomSheetState = filterBottomSheetState.copy(selectedCountryCode = msg.countryCode),
                )

                is Msg.FilterBottomSheetToggled -> copy(
                    filterBottomSheetState = filterBottomSheetState.copy(active = msg.active),
                )
            }
    }

    private companion object {
        const val STORE_NAME = "LeaderboardsStore"

        const val DEFAULT_LEADERBOARDS_PAGE = 0
        const val ADD_LEADERBOARDS_PAGES = 1
    }
}
